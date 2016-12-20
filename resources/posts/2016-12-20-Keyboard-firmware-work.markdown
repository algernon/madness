---
title: "Keyboard firmware work"
date: 2016-12-20 17:45
tags: [Hacking, Keyboard, UHK, keyboardio, ErgoDox]
---

I have been playing with an [UHK][uhk] prototype lately, obviously not on any of
the hardware bits, but on the firmware. It's an interesting thing, poking around
in the brain of a keyboard, especially when its the third, very different kind
of brain. 

You see, I have started my firmware journey with [QMK][qmk] on
the [ErgoDox EZ][ergodox-ez], which is a mature piece of work, used by quite a
many different keyboards. Compared to QMK, [KeyboardioFirmware][kbdio:fw] was a
whole different world: different build system, different way of handling
keyboard events, just to name a few things. And the [UHK firmware][uhk:firmware]
turned out to be different than both of the former firmwares I had experience
with. They all share some similarities, but the goals, the reasons, and some of
the internals are nothing alike.

It's such an interesting topic, that I'll talk more about it today. I will also
briefly run over the things I've been working on for the UHK firmware, but
first, lets talk about the major differences between keyboard firmwares!

 [kbdio:fw]: https://github.com/keyboardio/KeyboardioFirmware
 [uhk]: https://ultimatehackingkeyboard.com/
 [qmk]: https://github.com/jackhumbert/qmk_firmware
 [uhk:firmware]: https://github.com/UltimateHackingKeyboard/firmware
 [ergodox-ez]: https://ergodox-ez.com/

<!-- more -->

 [kbdio:fw]: https://github.com/keyboardio/KeyboardioFirmware
 [qmk]: https://github.com/jackhumbert/qmk_firmware
 [uhk:firmware]: https://github.com/UltimateHackingKeyboard/firmware

# Keyboard firmware comparison

## Setting up the environment

The first difference anyone who wants to build either of these firmware will see
is the way the environment has to be set up. 

[QMK][qmk] uses its own build system, and is a self contained monolith. You
don't need anything else but a cross-compiler and make, and you are ready to
rock.

[KeyboardioFirmware][kbdio:fw] uses the Arduino build system, so one has to
install the Arduino IDE and tools first (which conveniently include a
cross-compiler). Then one can use either the IDE, or the command-line tools to
compile the firmware, after letting the tools know where to find the auxiliary
libraries: `KeyboardioFirmware` is not a monolith, it reuses existing libraries
whenever possible, and itself is split into multiple ones (`KeyboardioHID`,
`KeyboardioScanner`, `KeyboardioFirmware`, etc).

[UHK][uhk:firmware] is made up of a combination of the above: it is a self
contained monolith, yet, it requires other tools than a cross-compiler: the
Kinetis Design Studio, in particular. There is a cmake-based build system in
place too, as an alternative, but I did not manage to make that work yet.

## Extension philosophy

Once we have the core firmware compiled, the natural thing any curious hacker
would do is to figure out how to change, extend, or otherwise fiddle with the
firmware. Herein lies the next major difference, which all three of them do
differently. Very differently.

### QMK

[QMK][qmk], being a monolith, does not make it easy to create easily reusable
plugins of any sort. If you add new functionality, it either has to live in your
own keymap, or in QMK core. This is good and bad: good, because it encourages
submitting your stuff upstream, and everyone benefits. Bad, because if you want
quick hacks that's useful for very few people only, the overhead of maintaining
a fork, or submitting the patch upstream far outweighs the benefits. Or you can
have it in your keymap only, but that makes sharing a bit harder. On the other
hand, there are *tons* of user-contributed keymaps one can borrow ideas from.
But the fact remains that extending the firmware is possible only if you touch
the core, or constrain yourself to your own keymap.

The philosophy appears to center around creating custom keymaps, rather than
making it easier to extend the firmware itself. Mind you, the core firmware
still provides hooks and opportunities to extend it - from custom keymaps.

### Keyboardio

[KeyboardioFirmware][kbdio:fw] is very different. Originally, it was a similar
monolith - a much smaller one, but still a monolith -, but we managed to insert
a few hooks which make it possible to extend the firmware with plugins. Plugins
that do not need to be part of the core firmware: they can be separate
libraries, which fit in well with the Arduino ecosystem, make code sharing and
reuse easier, and maintenance cheaper too. This comes at a cost of a bit of
extra work required to create the boilerplate, and to make the library
accessible to the Arduino tools. But these efforts are considerably smaller than
maintaining a fork, or trying to push everything into the core firmware.

The philosophy here is modularity, and giving enough rope, as the saying goes.
Custom keymaps, external plugins are all first class citizen.

### UHK

[UHK][uhk:firmware] is the black sheep in the family, because it has an entirely
different philosophy: you don't program your own firmware. You don't extend it,
you don't write code, you don't do any of these things. You remap your layout
using a tool they call the `Agent`. There are no easy hooks one can use to plug
into the firmware, no obvious way to make your own keymap, unless you maintain a
fork (assuming that you want to do things the `Agent` does not let you do). This
is a bit more friendly towards the casual user, but to a hacker who wants to
customize their firmware fully, this is a major obstacle. If you want to add new
functionality, you'll have to fork the firmware, and either maintain a fork, or
submit your changes upstream. In either case, you'll also have to change the
`Agent`, if you want to be able to use it still to configure your keyboard.

The philosophy here appears to cater for the casual user, making it considerably
easier to configure the keyboard, rearrange the layout, and so on, without
having to write a single line of code. But this comes at a cost of having strict
limitations on what the firmware can do, and how extensible it is. Nevertheless,
to achieve the goal of maximum user friendliness, this approach is the best. And
most keyboard users are not hackers, so it is a reasonable goal.

## Keyboard event handling

So we want to understand the firmware, how it works, how it implements some of
the most interesting features. These things being keyboards, a good starting
point would be to see what happens when we press a key. Interestingly, this is
the single place where the three do not differ wildly. At least, not all three.

To understand the biggest difference between these three firmware, I have to
explain one thing quickly: how a key pressed on the keyboard ends up on your
screen. Or, how the computer and the keyboard communicate over USB.

### Keyboard reports

When you press a key, it is not immediately sent to the computer. The computer
will ask the keyboard every once in a while (about every 5ms, if I remember
correctly) to report its state. Then, the keyboard will send a report about all
the keys that are pressed at that moment. It sends scancodes, not symbols.
Scancodes are then interpreted by the operating system, and it turns them into
the symbols we see on the screen.

So when you press a key, instead of being sent immediately, it is recorded in a
report. And the report is sent in bulk, whenever the host computer asks the
keyboard to do so.

### QMK

With `QMK`, we end up in a function that processes key presses and releases, and
dispatches to a number of other functions that implement the special
functionality. In the end, if none of them decided to handle the event, it asks
its TMK-inherited handler to please do so. It also provides a hook to override
all of these with a custom handler in the user keymap.

But the core idea is that there are two states when an action triggers: when a
key is pressed, and when it is released. You can't trivially detect a hold, not
without using some external tracking, likely involving a timer. You see, to
detect a hold, we have to remember that the key is pressed, and in a completely
different part of the event chain (in the part after scanning the matrix), we
can check if the keys that should be held, are still held, and do some action
then. This makes it a bit awkward to follow the exact chain of events if our
feature requires more than a trigger on key press and release.

From this follows that press and release events must be tracked accurately,
otherwise the report will become out of sync with reality. When we press a key,
it is added to the report. When we release one, it is removed. If we fail to add
or remove one from the report, then a key will be reported pressed, even though
it isn't, or it will fail to register, even though we hold it down.

This kind of flow gives a lot of explicit control to the firmware, at the cost
of complexity.

### Keyboardio

The `KeyboardioFirmware` is similar in that it will use a chain of functions to
handle each event. But, it differs from `QMK` in that it has more states than
just pressed and held: it tracks the previous and the current state separately,
which makes it possible to represent the *held* and *not touched at all* states,
too. This, in turn, makes it easier to deal with held keys, and allows the
report to be generated in a very different way.

Instead of tracking key presses and releases explicitly, `KeyboardioFirmware`
clears the report every scan cycle, and starts from scratch. While a key is
pressed (instead of only when it toggles on!), an action is triggered, and the
key is re-added to the report. For releases, it simply does not add them to the
report. Doing things this way makes the code a lot simpler, and less error
prone. And also allows us to deal with held keys more easily: we can handle them
at the same place as all other events, and don't need to hook into a different
part of the control flow.

### UHK

The `UHK` firmware does not have hooks at all, so it is even simpler than
`KeyboardioFirmware`: there is only one single place where actions are
implemented.

It also clears the report each scan cycle, just like `KeyboardioFirmware`.

## Feature comparsion

As far as the core firmware is concerned, `QMK` is the clear winner: it has tons
of features built-in, ranging from macros, to dual-use keys, layer toggles and
momentary switchers, one-shot keys, LED control, and so on and so forth.

`KeyboardioFirmware` is much more conservative about what goes into the core
firmware, and tries to delegate as many things as possible to plugins. So much
so that even macros are implemented as a plugin, and so are mouse keys (even
though they - for now - live in the same repository). Outside of the core
firmware, plugins implement pretty much everything `QMK` has, and sometimes even
more.

The firmware of the `UHK` is similarly restricted, because its features need to
be in sync with the `Agent`, and the serialization format they use to remap keys
on the keyboard. The firmware supports mouse keys, dual-use keys (which they
call long-press keys), macros and layer switches.

If we compare the features of the core firmware only, `QMK` is the clear winner,
followed by `UHK` a few laps later, and `KeyboardioFirmware` a dozen meters
behind it for the third place. However, if we also consider plugins, then
`KeyboardioFirmware` takes the lead, inches ahead of `QMK`, and `UHK` is left
laps behind.

# UHK firmware progress

I did [quite a number of things][uhk:prs] for the UHK firmware, it was an
exciting adventure too. Not only is the philosophy behind the keyboard and its
firmware so much different than any other I have worked with, the architecture
it runs on is nothing like the 8-bit AVRs I had experience with. The UHK has an
ARM processor, and orders of magnitudes more space for code, data, and EEPROM.
This allows the firmware to be less compact, and less aggressively optimised,
and to support features the other cannot easily do.

 [uhk:prs]: https://github.com/UltimateHackingKeyboard/firmware/pulls?utf8=%E2%9C%93&q=is%3Apr%20author%3Aalgernon

For example, as the UHK uses 32 bits for each key, it is possible to have
dual-use keys where the single-tap action is a compound action. As in, you can
have a key that acts as `Ctrl` when held, and other keys are pressed while it is
down, but acts as `Alt+Shift+Tab` when pressed in isolation. This is an
interesting feature. Using more bits to represent key actions also makes it a
tiny bit easier to set up the structures in C. They look nicer, and the code
that works with them is considerably easier to understand.

But lets start at the beginning!

At first, I wanted something easy, something I can use to test that flashing a
new firmware works, and that I can add new features. So I made it possible
to [set the brightness of all the LEDs][uhk:led-pr] on the keyboard, and used
this as a way to see how holding a key works, and similar things. This is also
useful when one wants to control the backlighting of the keyboard: just adjust
the LED brightness!

 [uhk:led-pr]: https://github.com/UltimateHackingKeyboard/firmware/pull/13
 
Then, I originally wanted to use 16 bits to describe each key, as the other
firmware I worked with do, but following some
discussion, [ended up with 32 bits][uhk:layout-handling-pr].

 [uhk:layout-handling-pr]: https://github.com/UltimateHackingKeyboard/firmware/pull/14
 
Mind you, this was quickly followed
by [another restructuring][uhk:big-restructure-pr], made possible by the earlier
work. Following this, we had the layer keys working normally, and they could be
positioned anywhere (previously, their position was hard-coded into the
firmware). This also laid down the foundation for other work.

 [uhk:big-restructure-pr]: https://github.com/UltimateHackingKeyboard/firmware/pull/18
 
I [updated the default layout][uhk:def-layout-pr] to follow the layout the
keyboard is meant to ship with more closely; added support for pretty much all
the keys on both the `Mod` and `Fn` layers. Even
made [layer handling][uhk:layer-pr] act as it was meant to act on the UHK.

 [uhk:def-layout-pr]: https://github.com/UltimateHackingKeyboard/firmware/pull/24
 [uhk:layer-pr]: https://github.com/UltimateHackingKeyboard/firmware/pull/21
 
Oh, and as a nice side-effect, the [icons now light up][uhk:layer-led-pr] on the
keyboard's display panel when switching layers. Small thing, but makes one feel
so much better when all the tiny pieces all start to fall into place.

 [uhk:layer-led-pr]: https://github.com/UltimateHackingKeyboard/firmware/pull/23
 
But it's not all small things and refactoring: [mouse keys][uhk:mouse-keys] now
also work on the ARM hardware too. This is something I had a lot of trouble
with, because the mouse keys were meant to accelerate as they are being held,
which is a kind of behaviour I find highly annoying. Implementing something that
drives you mad is not simple, so I didn't do it: mouse movement keys currently
move at a constant speed (but that speed can be adjusted with other keys).
Still, the core functionality is in place!

 [uhk:mouse-keys]: https://github.com/UltimateHackingKeyboard/firmware/pull/26
 
There is still much work left to do, and I'm working on some code that will
allow one to upload a configuration from the `Agent`, and have it applied to the
keyboard. The bits I'm working on is parsing the serialized configuration within
the firmware, and updating the layout. It's almost working, but the code is
terrible, and needs a lot of cleanup. That will likely happen between the
holidays only.

My goal is to have the keyboard be able to load its keymap from EEPROM by the
end of the year. I likely won't have neither the time, nor the resources to work
with its firmware after the holidays. Thankfully, there is still plenty of time,
and the hardest parts of the parser are already done, just waiting for a bit of
cleaning up.

## UHK impressions

While working on the firmware, I had a lot of time to play with and experiment
with the prototype itself. It's an interesting design. While split staggered
keyboards are nothing new (there is at least [VE.A][vea] and
the [Mistel Barocco][barocco] off the top of my head), the UHK has a built-in
display, and two keys below the `Space` and `Mod` keys, below the bottom row.
Those two keys are a useful innovation for a staggered keyboard without a thumb
cluster (the UHK will also have the option of a thumb cluster, but that's an
add-on). These keys makes the thumb a lot more useful, while still retaining the
shape and form factor of a traditional keyboard. A good fit for those that do
not want to switch to a more exotic keyboard, but still would like some improved
ergonomics.

 [vea]: https://www.massdrop.com/buy/ve-a?mode=guest_open
 [barocco]: http://www.mistelkeyboard.com/keyboards

The display is something I don't find all that useful, I rarely look at my
keyboard, and at least on the ErgoDox EZ I have, the small LEDs are enough for
my needs. I do not need a three-letter display to show the active keymap, nor do
I need icons for the layers I use: colors for me are easier to recognise. I
don't even need to look at the keyboard, I see the LEDs light up from the corner
of my eye, and the color tells me what I need to know. For icons, I'd need to
look, or pay attention to the position of the light.

On the other hand, my wife liked the display, and the icons too. She also liked
that the keyboard can be connected, and have a traditional form. The horizontal
stagger is also something she likes more than the column stagger, or a matrix
layout. And that brings us to the next topic...

# Building a computer for my mother

My mother has used my father's computer for a long time, and that has obvious
problems, such as the two of them not being able to get their computer-y stuff
done at the same time, to name just one. She wanted to have her own computer for
years now, but due to a whole host of reasons, we never got around to build one
for her.

Part of the problem is that she has very specific needs, ones that are
non-trivial to satisfy. First of all, she wants something small (both in size
and weight), because she'll carry it out into the living room, and back to a
storage room when not used (she'll use it maybe once a week). But she hates
laptops with a passion, so those are out from the start. She wants as few cables
as possible, but no bluetooth. It should have enough USB ports for a keyboard, a
mouse (or a trackball), and a scanner at least, preferably a fourth for an
external hard disk. The monitor should be reasonably small too, but not too
small: about 17 inches or so, anything bigger is too wide. She does not intend
to do anything GPU-heavy, so a built-in cheap video card is fine. Same goes for
audio. She also needs network, wired, no wi-fi (the wi-fi signal in the living
room is poor).

So, we are looking at something small, average CPU, GPU and audio, four USB
ports (though less is OK, she's ok with an USB hub, but would prefer not to use
one). Size-wise, something like 45x30x12 or 12x30x45, easy to carry, fits on the
dining table.

But, the topic is keyboards, so lets talk keyboards! She wants a compact
keyboard too, definitely not a full-size one. She has no use for the numpad, nor
for multimedia keys, or the like. When we talked about this, I said I could give
her one of my older TypeMatrix 2030 keyboards, fully expecting she'll say no. I
was wrong. Turns out she'd love something so small, and legends and layout
doesn't matter, because she learned to touch-type from my grandmother. She may
need to re-learn, but if you could touch-type in the past, it will come back
fast. So I'll be encouraging her to learn Dvorak, while at it.

But if she can touch type, I have much better options for her than the
TypeMatrix! She could use a split keyboard too! A long bet, I thought, but I was
surprised again to learn that she would not mind having a keyboard that can be
split apart, but can be combined too, when she needs to carry it.

That's where the [UHK][uhk] comes into the picture: it is compact enough for her
needs, can be split, can be combined, and she'll be able to remap keys with the
`Agent`! She does not need the extensibility I need, and she's fine with the
horizontal stagger too, because that's what she used before, so it is more
familiar. The display will be useful to her, and the backlighting too. The plan
is that next time she visits, she'll try the prototype, and we'll see how she
likes it.

 [uhk]: https://ultimatehackingkeyboard.com/

It is an incredibly proud and rewarding feeling when one introduces their
parents to the wonderful world of mechanical keyboards. Especially to ones one
worked on the firmware of.
