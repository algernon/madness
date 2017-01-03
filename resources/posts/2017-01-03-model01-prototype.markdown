---
title: "Keyboardio Model 01 prototype"
date: 2017-01-03 09:15
tags: [Hacking, Keyboard, keyboardio]
---

A day before Christmas, I [received a package][blog:package], in which was a
prototype of the [Keyboardio Model 01][kbdio], for the purpose of testing, and
fixing the [plugins][akela] I'm developing for its firmware. And to work on the
firmware too, as a side-effect.

Despite the device being a janky prototype, with all kinds of issues one would
expect from a thing hand-assembled from spare parts, that traveled around the
world in harsh conditions, it is a surprisingly usable keyboard. If you don't
count a missing key, and the left side being a bit weird with the palm key only
working every once in a while, and half the keys also triggering the key in the
top right corner of the half. But these are all issues one can work around
somewhat, and are definitely not an obstacle for testing and fixing plugins.
Jesse says that most of these issues are due to his poor soldering skills. I
think half of the issues was due to the prototype catching a cold on the flight
across the world. In the end, it doesn't matter much, though.

Today, I'll be talking about the progress I made so far with [Akela][akela], and
my experience with typing on the prototype. There will be pictures, but keep in
mind that what you see, and what I'm testing, is not much more than a very, very
crude prototype. It does not reflect how the finished product will look, feel,
and behave.

(Mind you, it is still of a much higher quality than some other products I had
the misfortune to own in past years.)

 [akela]: https://algernon.github.io/Akela/
 [blog:package]: /blog/2016/12/24/the-package/
 [kbdio]: https://shop.keyboard.io/

<!-- more -->

# Akela progress

## Akela-the-firmware

For the fun of it, I gave the obsolete incarnation of Akela, the one that was a
complete firmware on its own, a try on the prototype. It did not work, at all. I
entertained the idea of diving deeper, to figure out how much work would be
needed to bring it to a state where at least the basics work, but then did not
take that route. That project was a dead-end anyway, why waste time on it now?

It would have been very satisfying if at least some things worked, because then
I could say I wrote something usable for hardware I have not even seen. But
alas, this will have to remain on by bucket list for a while longer.

## First steps

At first, hardly anything worked, for a whole host of reasons, ranging from
buggy event flow, through misunderstanding how C++ works, to simple things like
typos. But after a few days, I had most of the easier plugins in a reasonable
condition. The more complex ones... those gave me - and continue giving me -
quite a bit of headache.

## Lessons learned

I learned that `static` is not always a good choice. It makes some things
simple, but I have no control over the initialization order, and that is an
issue. It's also not the best fit for the job: I want singleton objects. For
those, an `extern` is a better fit. So one of the first things I had to do was
to redesign the way plugins are initialized. The new method is much more
reliable, but sadly a tiny bit less end-user friendly. Once everything else is
ironed out, I'll try to figure out ways to make it easier to include plugins in
a Model 01 firmware sketch.

I also learned that compilers and linkers can be overly optimistic, and the
Arduino build tools try their best to remove unused parts of a library. This bit
me hard. You see, there was a part of [KeyboardioFirmware][kbdio:fw], that
implemented hook registration. For a while, this was used from within
`KeyboardioFirmware`, too, and all was well. But a recent change removed that
use, but left the hook registration in, so that plugins can use them
nevertheless. Sadly, while compiling the library, and linking it into a static
ELF object, these functions were removed, as they were not referenced by the
library itself. Thankfully, there are a number of workarounds for this, but it
was a very annoying thing to figure out and eventually fix.

 [kbdio:fw]: https://github.com/keyboardio/KeyboardioFirmware

## Status report

There was no single plugin that worked out of the box, because all of them had
to change due to the hook changes. There were a few which required little - but
all the more embarassing - fixes. For example, the `LEDEffects` plugin had the
RGB colors mixed up, with R and B swapped. Oops. Or the `Leader` plugin, which
did an assignment instead of a comparison for inequality, because I forgot an
`!`, and the compiler did not warn me about it. But this was the only change -
apart from the hook registration stuff - that needed change in the `Leader`
plugin, which is pretty darn awesome.

I also managed to make `DualUse`, `SpaceCadet` and `ShapeShifter` plugins work
reasonably easily. `OneShot` gave me a lot of headache, and while it works now,
it still has some odd behaviour here and there.

The `MagicCombo` plugin also works, but its usage is awkward, so it may need to
be redesigned.

But my biggest regret so far is the `TapDance` plugin, which does not work at
all. It was one of the last plugins I wrote, I was hoping it would mostly work,
because I reimplemented tap-dance more than four times by now, and really didn't
look forward to debug it any further. But alas, I had no such luck.

All in all, things are looking good, and there is a very high chance that I'll
be able to finish all of the currently existing plugins by the time the first
PVT models are due. No promises, however.

As for the stock firmware, that is now in a state that looks and feels solid.
Hopefully there won't be a need to make substantial changes to its core
features.

# The prototype experience so far

The hardware itself shows, as I described above, the kind of issues one would
expect from a prototype assembled from spare parts. The bottom plate does not
have legs, nor the mount hole, so no easy tenting or tilting is possible, and
the center bar's slider on each side makes the inner half of the keyboard higher
than the outer (which is fine, mind you). One of the palm keys is unreliable,
and I had to hack the firmware to ignore the `R0C6` key, because pretty much
every second key pressed on the left side also triggers that key, too. (I said
it is a janky prototype!)

But with a few hacks to make the behaviour more reliable, the keyboard itself is
surprisingly usable:

 ![The Prototype](/assets/asylum/images/posts/model01-prototype/prototype.jpg)

I do not have the wood enclosure and the rest of the keyboard screwed together
on the left side, because I need access to the reset button under the wood
sometimes. I have not been able to figure out the exact conditions where the
`Prog` button stops working, but there's a good chance that this happens because
the left side is in a very bad shape, hardware wise.

As an experiment, I started typing things on the prototype, to get a feel of the
switches, the keycaps, and the layout. Overall, I am positive that the finished
product will live up to the expectations, and in many ways, even exceed them.

## Switches

The prototype has [Matias Quiet Click][matias:quiet] switches, as far as I can
tell. The switch is one of the reasons I was - and am - looking forward to
the [Model 01][kbdio]: I'm using Gateron Browns currently, and while I like
them, I wouldn't mind something a little bit heavier, a more pronounced bump,
and something quieter. The browns are quiet, except when I bottom out - which I
do all the time. The Quiet Click promised to be everything I wanted.

 [matias:quiet]: http://matias.ca/switches/quiet/
 [kbdio]: https://shop.keyboard.io/

Having typed a few thousand words on the prototype so far, I like the switches.
The tactile bump is something I'm a bit torn about, not sure it meets my
expectations, but it is certainly at least on the level of the Gateron Browns.
Will have to type a bit more to get a better feel. But the weight and the lack
of noise is just perfect.

I have a new favourite switch.

## Keycaps

The custom sculpted keycaps were something I was both enthusiastic about, and a
little bit afraid of. Finger length, width, and so on, are not universal: what
fits one person, may not fit another, and the same goes for comfort. The promise
of keycaps that guide my fingers sounded awesome, but there was this lingering
feel that getting that right would be next to impossible.

I'm happy to report that Keyboardio succeeded, and the keycaps are amazing. I
never want to type on anything else, and the final product can't come soon
enough.

They really do guide your fingers, and the feeling of that is incredible, it's
transcendental. They are just the right size and shape, well laid out, so the
keys are comfortable to reach too. It's a joy.

## LEDs

LEDs are something I cared little about. They are interesting, and fun, but not
something I'd use daily, as I do not look at the keyboard often. The three small
LEDs my [ErgoDox EZ][ergodox:ez] has are enough for me to indicate the most
interesting bits. But LEDs under each key make it possible to have better
indicators, since their light is usually strong enough to see even when I'm
looking at the monitor.

They are also incredibly useful while working on the firmware and the plugins,
as I can use them to signal various states, which is a lot easier to understand
than a spew of logs that I need to parse. Visual feedback is king when doing
quick iterations. Makes it a lot easier to debug certain events.

So while I still believe I'll have little use for the LEDs during normal daily
use (except maybe for some backlighting), they are still useful when working on
the firmware. They are also a lot of fun to play with: my wife loved the idea of
macros lighting up the keys they are pressing. Not to mention the `Chase` and
`RainbowWave` effects. Those were instant favourites of hers.

## Layout

Having used an [ErgoDox EZ][ergodox:ez] for almost a year, I thought getting
used to another split layout would not be all that hard. Boy, was I wrong!

 [ergodox:ez]: https://ergodox-ez.com/
 
The thumb arc is very different from the thumb cluster, with an access pattern
different from what I imagined based on the pictures and videos I saw. I know
now that I'll have to rethink how my keys are laid out across the arc.
Initially, I though that the two keys in the middle would be the easiest to
reach, but experience so far seems to suggest that it's the two inner keys
instead. This means I'll have to shift things around to put the more often used
keys there.

The keys on the bottom row on the inner side of each half is also reachable by
the thumb. How easily, depends on posture. I'm not sure yet if I want to reach
them with a thumb, or with an index finger. Both are possible, neither is
particularly hard or uncomfortable, but I've yet to find the perfect posture.
That, on the other hand, will largely depend on the angle of tenting and tilting
I end up with, so not possible with the prototype yet. Thankfully, there are a
number of options, and I'm sure I'll find a comfortable way to set the keyboard
up.

Surprisingly, the issue that caused me the most headache was not the thumb arc,
or these inner keys on the bottom row. It was the `R1C14` key, the `L` symbol on
Dvorak (and to some extent, `R1C1`, or `'` on Dvorak, but that's a symbol I use
less). It's not where my fingers learned to search for, and I still end up a row
off from time to time. However, there's a good reason for this: the key is
easier to reach! Requires a lot less stretching or movement, which is great.
It's just that I got used to the layout on the ErgoDox, even if that's not the
best. Thus, getting used to something better still takes time, but I like better
reachability, so having to re-learn the layout is not going to be a problem.

The rest of the layout - at least what I use from it, as some of the keys I want
do not work yet, due to `TapDance` not being usable yet - feels good so far, I
had little trouble adjusting from, and switching back to the ErgoDox.

## The palm keys

Sadly, the left palm key is not working reliably, so the amount of testing I
could subject the palm keys is limited to only the right one. These are another
thing that will need some time to learn, and I'm sure I'll go through a number
of iterations before I settle on how I want to use them.

I planned to use them as one-shot-kind of keys, as an extension to the thumb
arc, but from the limited amount of testing I squeezed in so far, it seems this
is not the best plan. The palm keys feel easier to hold than to tap and release,
because my hands are naturally resting on the keyboard.

On the other hand, I'm currently using the prototype laid out flat, but that's
not how I will use the final product: that will be tented and tilted heavily.
The way I use the palm keys may change significantly.

While I can't decide yet how I will use the palm keys, I do know that they are -
as hoped - a great addition. Some of the functions I used my thumbs for are now
delegated to the palm, making the load on the thumbs smaller. As someone who
puts **very** frequently used keys (such as `Enter`, `Space`, `Shift` and
`Backspace`) on the thumb arc, a bit less load on them is a big relief.

## The wood

One of the first things that got me hooked about the Model 01 was the wooden
enclosure. I love wood, and wooden things. A lot. And even if the frame of the
prototype is from the factory they abandoned before, and is of far worse quality
than the final product will be, it still feels and looks amazing. I do not need
a separate wrist rest like I do for the ErgoDox EZ, and I do not need to figure
out how to place them well, so that they won't move, are the right distance, and
so on. I'll just lay my hands on the wood. Less logistics, more comfort - what's
not to like?

But as with the tactile bump, these are just initial thoughts. I'll have to use
the prototype more to form a more coherent opinion. The initial impression is
convincing, though.

# Working with Jesse & keyboardio

When writing about the prototype and the firmware, I feel I need to mention
working with Jesse too, because that's an important part of how we got here,
too. Many things could have gone entirely different, were it not for how my
random ramblings, changes and bugs were handled: with enthusiasm,
professionalism, care, respect.

Coming to the firmware as someone with very little clue about what he's doing
(my only relevant experience in the field were some contributions
to [QMK][qmk]), I surely asked or said stupid things. But these were always
answered calmly, and in a way that I never felt dumb. I felt welcome pretty much
from the start, even when I worked against the official firmware by developing
my own.

 [qmk]: https://github.com/jackhumbert/qmk_firmware

When I broke something, we tried to figure things out together, on IRC, or on
GitHub, but most of the time, together. This made me feel much more involved.
Made me feel like being part of the journey, and that's a damned good feeling.

When introducing bigger changes, my reasons were listened to, and I was given
the chance to convince. My ideas that did not originally match Jesse's were not
rejected outright. Sometimes I was convinced, sometimes I convinced - as it
should be.

And it is not limited to code contributions alone. If we look at how the
questions and criticisms are handled on the Backer Update comments, that shows
the same level of care. The keyboardio forums which I had the privilege to be
invited too, are similar. The community building up there and elsewhere is one
of the best ones I have seen so far, on the level of [Hy][hylang]'s, which has
been my favourite community since [Debian][debian].

 [hylang]: http://docs.hylang.org/en/latest/
 [debian]: https://www.debian.org/

Keyboardio is the best example of how a product should be developed, and how the
community should be built. I have huge respect for anyone who can pull these
off, despite all the hardships, setbacks, and all that, and still come out
winning. It should be taught, and shown to anyone who wants to build a
community, or a product (or both), as the greatest example to learn from.

# Next up...

Next time I hope to report more on how the keyboard feels. Once I have
`TapDance` working, I'll try to go a full week with using the prototype
exclusively. That will not only help me get a better feel and understanding, but
will help me refine my layout too.

But for the next few days, or even the rest of the week, I am likely going to
shelf working on the Keyboardio firmware, and focus on [UHK][uhk], because there
is plenty of tasks left there too, and honestly, I could use a break from trying
to fix my own silly bugs, to work on something I'm much less responsible for.

 [UHK]: https://ultimatehackingkeyboard.com/

Oh, and a new season of Diablo 3 will start too, so... err.. yeah. Time will be
short.
