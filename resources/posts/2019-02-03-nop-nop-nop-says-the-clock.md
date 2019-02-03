---
title: "NOP NOP NOP says the clock, on the bug-fuse falls a lock"
date: 2019-02-03 02:00
tags: [Hacking, Keyboard, keyboardio]
---

Lately, I've been porting [Kaleidoscope][k] to keyboards that happened to land
on my desk for one reason or the other. Keyboards such as the [ErgoDox
EZ][kbd:ez], the [Atreus][kbd:atreus], [Splitography][kbd:splitography], and
most recently, [KBD4x][kbd:kbd4x]. In almost every case, I ran into weird issues
I couldn't immediately explain, where the symptoms weren't search-engine
friendly. There wasn't anything obviously wrong with the ported code, either,
because the same code worked on another board. Figuring out what went wrong and
where was an incredibly frustrating process. I'd like to save others from having
to do the same digging, debugging, hair-tearing I did, so we'll look at these
three cases today.

 [k]: https://github.com/keyboardio/Kaleidoscope
 [kbd:ez]: https://ergodox-ez.com/
 [kbd:atreus]: https://atreus.technomancy.us/
 [kbd:splitography]: https://softhruf.love/collections/writers
 [kbd:kbd4x]: https://kbdfans.cn/collections/diy-kit/products/kbd4x-custom-mechanical-keyboard-hot-swap-diy-kit

<!-- more -->

 [k]: https://github.com/keyboardio/Kaleidoscope
 [kbd:ez]: https://ergodox-ez.com/
 [kbd:atreus]: https://atreus.technomancy.us/
 [kbd:splitography]: https://softhruf.love/collections/writers
 [kbd:kbd4x]: https://kbdfans.cn/collections/diy-kit/products/kbd4x-custom-mechanical-keyboard-hot-swap-diy-kit

## A tale of fuses and woe

The first problem I ran into was during the [Splitography][kbd:splitography]
port. It should have been a straightforward port, because it is built on top of
`ATMegaKeyboard`, like the [Atreus][kbd:atreus] port, which has been working
wonderfully. I prepared the port in advance, before the keyboard arrived, and
was eagerly waiting the shipment to try it. I was confident it will work out of
the box. It did not: the left half was dead.

I quickly flashed [QMK][qmk] back to verify that the hardware is fine, and it
was, both halves worked with QMK. What am I doing wrong then? I verified that
the pinout is correct, I checked with a simple key logger that the problem is
not that we don't *act* on key presses, but the firmware doesn't even see them.
This was the first clue, but I wasn't paying enough attention, and went
comparing `ATMegaKeyboard`'s matrix scanning code to QMK. I even copied QMK's
matrix scanner verbatim - to no avail.

 [qmk]: https://github.com/qmk/qmk_firmware

At this point, I looked at the pinout again, and noticed that the left half's
columns are all on `PINF`. Why aren't we able to read from `PINF`? It works on
the Atreus! At this point, I searched for "reading from PINF not working", but
since `PINF` isn't a common search term, my search engine helpfully added
results for "ping not working" too - which I did not notice because I've been
fighting this for over an hour by that point. Eventually, once describing the
problem to Jesse on Discord, he gave me the final clue: JTAG.

The `ATMega32u4` MCU the Splitography has JTAG support enabled in fuses by
default. Most vendors who ship the MCU to end-users disable this, but on the
Splitography, it wasn't disabled. This meant that using `PINF` didn't work,
because the MCU was expecting to use it for JTAG, not as an input pin to read
from. Once this was clear, it didn't take much time to find the solution by
looking at the datasheet, the following sections in particular:

- 2.2.7 (PIN Descriptions; PIN F)
- 7.8.7 (On-chip Debug System)
- 26.5.1 (MCU Control Register – MCUCR)

In the end, the fix was these two lines in the constructor of the Splitography
hardware plugin:

<div class="pygmentize" data-language="c++">
MCUCR |= (1 << JTD);
MCUCR |= (1 << JTD);
</div>

What it does, is it writes to the `JTD` bit of `MCUCR` twice within four cycles,
which disables JTAG at run-time, and makes it possible for us to use `PINF` for
input.

## Time is of the essence

The next problem I faced was when I started to work on [underglow
support][k:underglow] for the KBD4x keyboard. As expected, it didn't quite work
with my port, but ran flawlessly with QMK. So what do I do? Compare the code.

 [k:underglow]: https://github.com/keyboardio/Kaleidoscope/pull/551

The code to drive the LEDs on the KBD4x (pretty common WS2812 leds), I used the
same source library the QMK code is based on. This was strange, because the code
I used for the KBD4x LED strips, I used before for the [Shortcut][kbd:shortcut]
port, and everything was working fine there. Nevertheless, I went and compared
the code, down on the compiled, optimized assembly level. It was exactly the
same.

 [kbd:shortcut]: http://shortcut.gg/

Yet, even though the code was the same, with QMK, I was able to set the LED
colors as I saw fit. With my Kaleidoscope port, no matter what data I sent its
way, the LEDs always ended up being bright white. This is surprisingly hard to
search for, and my searches yielded no useful results. At the end of the day, I
let my frustration out in QMK's discord a bit, and got a little hint from there:
the WS2812 strips are very picky about timing.

After a good night's sleep, I went looking into the QMK sources to see if
there's anything there I do differently in Kaleidoscope, focusing on
time-related things, such as the clock. And that was the key!

The `ATMega32u4` has an option to divide its clock speed, to conserve power.
Like in the case of JTAG, this can be set or unset in fuses. Thankfully, like in
the JTAG case, we can disable this at run-time too, with the following magic
words:

<div class="pygmentize" data-language="c++">
CLKPR = (1 << CLKPCE);
CLKPR = (0 << CLKPS3) | (0 << CLKPS2) | (0 << CLKPS1) | (0 << CLKPS0);
</div>

If the MCU divides its speed to conserve power, it will run slower, and all the
timing the library I work with uses will be totally wrong. No wonder poor LEDs
lit up white!

With the magic incantation added to the keyboard's constructor, I was able to
set colors properly. Why wasn't this a problem on the Shortcut? Because it had
clock division disabled in fuses.

## NOP, NOP, NOP

Many days later, I had a few spare minutes, so I figured I'll add support for
the KBD4x to [Chrysalis][chrysalis]. This was a 15 minute task, and everything
worked fine, yay! I figured I'll build a sketch for my own uses while there, and
that's when I noticed that the first column wasn't working at all.

 [chrysalis]: https://github.com/keyboardio/Chrysalis

Quickly flashing QMK back verified that the issue is not with the hardware. Yay,
I guess?

So the usual thing happens: what went wrong? The pinout is the same as in QMK,
JTAG and clock division are disabled. The first column is on `PIN_F0`, so making
sure JTAG was disabled was my first step. Some other columns were also on
`PINF`, and those worked, so it's not JTAG.

Frustrating. I cry out on Discord, and Jesse tells me immediately he saw
something similar on the [Planck][kbd:planck], and had a fix. We look into
`ATMegaKeyboard`, and indeed, there appears to be a fix there:

 [kbd:planck]: https://olkb.com/planck

<div class="pygmentize" data-language="c++">
uint16_t ATMegaKeyboard::readCols() {
  uint16_t results = 0x00 ;
  for (uint8_t i = 0; i < KeyboardHardware.matrix_columns; i++) {
    // We need to pause a beat before reading or we may read
    // before the pin is hot
    asm("NOP");
    results |= (!READ_PIN(KeyboardHardware.matrix_col_pins[i]) << i);
  }
  return results;
}
</div>

Emphasis on the `asm("NOP")`. That line is supposed to slow us down a bit so
that the pin we're reading has a chance to settle. I had two questions at this
point: why isn't the existing fix enough, and why do I remember my first column
working before?

Turns out, this is related to the previous issue! You see, when I turned clock
division off, the keyboard started to run a bit faster, which meant that a
single `NOP` didn't slow us down enough for the pin to go hot. With clock
division, we were running slow enough for a single `NOP` to be enough.

But how do we fix this? I checked QMK (checking other, similar projects for
clues is such a great thing!), and they delay for 30µs. While that'd work for us
too, we didn't want to add an explicit delay on the fast path. I looked at the
assembly to see if we can do anything smart there, and noticed an interesting
thing: the compiler inlined `.readCols()`, and unrolled the loop too.

What if we didn't inline it? We'd have a function call overhead then, which is
faster than a delay, but slower than being inlined. Adding an attribute that
disables inlining made my first column work. However, I wasn't satisfied,
figured we can do better. What if we allowed inlining, but stopped unrolling the
loop? Checking the loop condition is faster than calling a function, but still
slower than being inlined and unrolled. Turns out, disabling loop unrolling was
enough in this case:

<div class="pygmentize" data-language="c++">
__attribute__((optimize("no-unroll-loops")))
uint16_t ATMegaKeyboard::readCols() {
  // ...
}
</div>

## Summary

In the end, if I read documentation, and truly understood the hardware I'm
working with, I wouldn't have faced any of these issues. But I'm not good at
hardware, never will be. My brain just stops if I try to ingest too much
hardware documentation in one sitting. Which means I'll run into similar issues
again for everyone's benefit, but mine! Yay.

And to think I'm about to build a home security system soon... yikes. I'm
utterly scared about that prospect. So many things *will* go wrong, that the
next post about hardware-related things going awry will be even longer.
