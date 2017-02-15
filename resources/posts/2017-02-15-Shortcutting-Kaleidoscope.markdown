---
title: "Shortcutting Kaleidoscope"
date: 2017-02-15 22:00
tags: [Hacking, Keyboard, Shortcut, keyboardio]
---

Last time I talked about [firmware stuff][blog:horde], I mentioned that there's
a new package coming my way. Well, it arrived, and that set the course for the
next week or two at least. And things unfolded **rapidly**. We've implemented
some very important pieces of the puzzle, solved problems we have not noticed
before, and I created plugins I was commissioned to write. All in all, the past
two weeks were eventful, and easily the busiest and almost the most productive
part of my firmware work so far.

 [blog:horde]: /blog/2017/01/30/Unleashing-the-Horde/
 
Today, we'll have a look at what happened with [Kaleidoscope][fw:kaleidoscope]
(née, `KeyboardioFirmware`) since the [Shortcut][sc] prototype arrived at my
door.

 [fw:kaleidoscope]: https://github.com/keyboardio/Kaleidoscope
 [sc]: http://shortcut.gg/
 
<!-- more -->

# Kaleidoscope

One of the most obvious changes is that the firmware has a new
name: [Kaleidoscope][fw:kaleidoscope]! I think it is a great name, a splendid
choice by Jesse! This was in the queue for a while; Jesse said it in the past
that he wants the firmware to support other hardware than the
Keyboardio [Model 01][m01], and having the vendor in the name is not the best
way to promote that idea. So a new name was needed, but as it was not ported
elsewhere yet, it wasn't all that urgent. This changed (see below!), and Jesse
chose a new name.

 [fw:kaleidoscope]: https://github.com/keyboardio/Kaleidoscope
 [m01]: https://shop.keyboard.io/

But that meant that *all* of the plugins had to be updated, and all of them had
to be renamed too. Well, we could have kept the old names, but having
`Keyboardio`, `Akela`, and `Kaleidoscope` as plugin prefixes, while they all are
for the same core, would have caused terrible confusion. So everything under the
sun was updated. This was no small task, there were lots of plugins. And while
there, I also made slight adjustments, to make it clear that the plugins are
independent (unless stated otherwise).

Nevertheless, it's done, and the new name is awesome. I just typed
*Kaleidoscope* so many times that it is easily the fastest word I can type at
this point. That's a win too, right?

# Firmware porting efforts

## ErgoDox EZ

I had a working proof of concept of Kaleidoscope running on the ErgoDox EZ. I
pretty much lifted out half of [QMK][qmk] and wrapped it into a few objects to
make this possible. It worked, but it was bypassing most of the good parts of
Arduino. It was also an inefficient, ugly, unmaintainable mess. In the long run,
this would not have worked, so I'm not publishing it, either. I don't want my
name to be associated with that code.

 [qmk]: https://github.com/qmk/qmk_firmware

Another attempt was made later to port Kaleidoscope to Teensyduino, but that is
sufficiently different from what Kaleidoscope expects that I ultimately gave up
on the effort too. It is certainly possible to do such a port, but I had neither
time, nor motivation to do so.

[QMK][qmk] works perfectly fine on my ErgoDox EZ. There are some things that
could be better, but they are not important enough to justify the port.

## Shortcut

The [Shortcut][sc] is a whole different beast. I was sent a prototype with the
explicit purpose of porting Kaleidoscope to it, and proving that this is not
only possible, but beneficial too.

 [sc]: http://shortcut.gg/
 
I'm happy to report that the initial port was a success: in just a few hours, I
had half the keyboard working, and all that was left is figuring out how to lay
out some C structures. If I weren't tired, I would have been done with it in a
night.

It helped a lot that the Shortcut is using an Arduino-compatible MCU, so the
biggest part of the port was to port the scanner, and figure out a way to make
it easier for the end-user to specify a keymap layer. Cleaning up the code, and
wrapping it in an interface that Kaleidoscope expects was almost trivial.

As a result, the Shortcut is now supported by Kaleidoscope, and once a few minor
things are ironed out, the hardware support library will be part of the official
collection of Kaleidoscope plugins. Jesse already blessed the inclusion.

### What does this mean for the Shortcut?

There are many benefits of using Kaleidoscope, whether you are an end-user, a
firmware developer, or a hardware person. I will not go over all the reasons
here, that's for a separate post. I'll try to focus on the most important
benefit for each case instead.

For end-users, this means that all they have to install is the Arduino IDE.
Everything else will be installable from there (as soon as we submit the plugin
libraries for inclusion, but that's just administrativa). No need to fiddle with
installing GCC, or Linux in a VM, or anything silly like that. Just the IDE, and
from there, the plugins they need. Not to mention that work is under way that
will allow one to upload a new keymap, and settings, without having to compile
anything. That's not quite there yet, but it will be coming.

For firmware developers (and advanced users), Kaleidoscope provides a very small
(about 500 lines) core, and a rich set of plugins, hooks to make third party
plugins easy to write. These are nicely composeable, and lend themselves for
customisation very well. Instead of a monolithic beast, you get a range of
mostly stand-alone plugins. It is a lot easier to understand how each part
works, when they are properly isolated.

For hardware people, Kaleidoscope provides a portable, modular core, where they
don't have to maintain their fork, and porting is as easy as implementing a
small library that talks with the keyboard hardware. The ugly bits of USB and
the rest are already done for you. Once the hardware library is in place, the
rest comes for free, and you benefit from all the work done for other keyboards
too. You'll be able to read a great example of this just below.

# Major firmware improvements

## Speed

While testing the Shortcut, I noticed that the scan cycles are unusually long.
As in, about 10 scan cycles a second. That is absolutely, utterly, and
unacceptably slow. Especially for a keyboard aimed at gamers where speed is of
crucial importance. For a little while, I was getting really worried that there
is some fundamental flaw in how Kaleidoscope does things. But lets start at the
beginning!

I was debugging some bugs in the matrix (no, not that one!), and lacking an
on-board - or even remote - debugger, I resorted to good old `Serial.print`
calls. That's when I noticed that even though I do a `Serial.println` each scan
cycle, I only see about 10 lines a second. Yikes.

Well, I just finished up the first proof of concept of the scanner, and it was
doing some very dumb stuff, so perhaps I screwed up badly there? I patched those
up, but no noticeable difference. Damn.

Next, I disabled the key event processing, to see if it is the scanner that
slows things down, or the key event handling. It was definitely the key event
handling. This was bad news, as I thought that this part of the firmware was
rock solid and as complete and finished as it can get!

I started investigating which part of it is slow, first by narrowing down the
actions to one half of the keyboard: boom, noticeably higher speed! Much more
than twice! Interesting... what if we skip the layers, and just do a direct
lookup, as if we were on the base layer? Boom. Now the lines scroll by like a
million little ants, as they should.

So the problem is with looking up a key in a layer. The way we did that, was
that every time we called `Layer.lookup`, it started walking backwards from
layer 31, checking if the layer is active, and if so, looking up the keycode for
the give position. If that was transparent, we continued, otherwise we bailed
out early. This was done for every key, in every cycle, even if the key has not
changed state. On the Model01, assuming we are on the default layer, 31 useless
checks for every single key. That's a non-trivial amount.

The first band-aid applied was to cache the highest active layer, and start from
there, instead of 31. This improved performance dramatically, but was just a
band-aid: there were fundamental problems with the approach.

First of all, the cycles spent finding a key was not only way too much, it was
unpredictable, and not even uniform! If we had 7 layers active, one key may have
been found on layer 6, the other on layer 0, and their lookup speed would differ
drastically. As a consequence, the length of a scan cycle was dependent on how
many layers we have, and how they are set up. This was simply not acceptable. We
want predictability, and ideally, O(1) lookup.

To achieve these goals, we introduced another change: whenever a layer changes,
we scan the keymap, from highest active layer down to the default one, and
create an index of where each key can be found. This way, `Layer.lookup()`
becomes a lookup in two arrays only, an O(1) operation, and a fast one at that
too. The downside is that we use one byte per key of precious memory, and that
whenever there is a layer change, we need to create this index.

Thankfully, layer changes happen orders of magnitudes less than lookups. Each
scan cycle looks up 64 keys on the Model01. On a typical day, I change layers
maybe a few dozen times a *day*. But even if I would be a heavier user of
layers, a layer change more than once every second is something that would be
very, very rare. And even then, we do a thousand times more lookups.

So this is an acceptable trade-off, and the scan cycle appears to operate at a
much more respectable speed. I've yet to measure it, though, but with the two
changes above, it is easily orders of magnitudes faster.

Phew! Crisis averted.

## Timing is harder than anticipated

When I originally implemented my plugins, I had very little idea about what the
hardware, and what the Arduino environment can provide. I was afraid to use
anything I have not seen in the code before, so I rather implemented my own
solutions for the issues I faced.

One requirement that a number of my plugins needed, were timers. Since I did not
read the Arduino reference, nor the hardware specs, I ended up coding my own
fake timers: counting main loop iterations. This was based on the assumption
that the main loop always runs in the same amount of time, or at least, the
variation is negligible. All the timers in my plugins were tuned according to
this assumption, and to the speed of the scan cycle on the Model 01 prototype.

With the Shortcut added to the fold, which had less keys, the length of the scan
cycles were shorter, so my finely tuned timeouts were suddenly inadequate. I
wasn't looking forward to tweaking the defaults for each and every hardware
Kaleidoscope may get ported to...

And then came the issue explained above, and the scan cycle becoming blazingly
fast. There was no way I could tune the existing fake-timers to that: I'd have
to up the timers from 8-bits to 16, and carefully try to tune them again. This
would just not do.

So, I started to use `millis()`, a real timer, for all timing needs. This did
increase code size a little, and data use too, but in return, we get much more
accurate timings, regardless of hardware. No need to do per-keyboard tuning, and
milliseconds are a concept much easier to imagine than scan cycle lengths. I can
roughly estimate how long 250 milliseconds last, but I have no idea how long 100
scan cycles take. Not to mention that scan cycles are still hardware-dependent,
even with the fixes we applied previously: there's simply more - or less - keys
to scan and deal with.

As such, all plugins that have timers, now use millisecond timers. Most often,
the resolution will still be limited to a scan cycle's length, because we check
the timers once per cycle, usually.

This has been a major headache, to rework everything to use timers. I should
have done this from the start. Lesson learned: read the docs, the specs, and
design with that knowledge in mind. You'll save yourself a lot of trouble down
the road.

## Exile of the mouse

Kaleidoscope had mouse key support for a long time, but recently, it saw some
very welcome improvements.

In the past, the plugin used separate acceleration for the horizontal and
vertical axis, which meant that if you held the `Mouse Up` key, then started
holding `Mouse Right` a little later, the cursor drew a curve, instead of going
diagonally as one would expect (or at least how I'd expect). This made it
non-trivial to use mouse keys, because it was hard to predict how the cursor
would move, because you'd have to track two separate accelerations in your head.
Now it uses a single accelerator, which makes it easier to position the mouse.
On the down side, this makes it a bit harder to draw curves.

But an even more important change is that the speed of the cursor, the speed of
the acceleration, and the delay between these is now adjustable at run-time. You
want your mouse to be precise when you are aiming, but you want it to be blazing
fast when you are spinning around. You can do both now, by using a few macros
that adjust the various properties.

The need for this arose from the scan cycle speedup: suddenly the cursor was
moving way too fast, and accelerating even faster. Now you can set it to
accelerate by two, every 100ms while any mouse key is active, continuously.

Mousekeys now work so well, that when I use the Shortcut, I rarely have to use
my trackball. I even tried a couple of games with it, and with a bit of getting
used to, they were comfortably playable, and I enjoyed not having to move my
hand for mousing purposes.

# The Shortcut

I talked a lot about the firmware side of the [Shortcut][sc], but I have not
talked about the hardware yet, and my initial impressions. For reference, here's
how the prototype I received looks (keycaps are mine, these are what I had
around). Keep in mind that this is a prototype, in no way representative of how
the final product will look like.

 ![Shortcut](/assets/asylum/images/posts/shortcutting-kaleidoscope/shortcut.jpg)
 
As you can see, it has very few keys, and has two funny sticks on the thumb on
each half. Those are 4-way switches, not joysticks, or the like. The small
amount of keys, and their arrangement makes it quite a challenge to translate
traditional layouts to the device, but thankfully not impossible. One will have
to make small changes here and there, mind you, but I found it reasonably easy
to adapt.

The small size also means that we'll be using layers a **lot** more (and thus
the importance of lookups being fast is even more important!), which is
something I still have to come to terms with. I'm not a particularly heavy layer
user on my daily drivers, you see.

## Thumb sticks

 ![Close-up on the thumb sticks](/assets/asylum/images/posts/shortcutting-kaleidoscope/shortcut-thumbstick.jpg)

On the other hand, we have the thumb sticks, a very intriguing input solution.
It is almost natural to want arrows and/or mouse movement on these, and both of
these work spectacularly well. On the ErgoDox EZ, and the Model 01 prototype, I
was never really satisfied with mouse keys. Even on the Model 01, which uses the
same firmware, has the same acceleration tuning knobs, etc., mouse keys still
felt weird. On the Shortcut's thumb sticks - not anymore! There is apparently a
huge difference between tapping keys, and pushing, pulling a stick in various
directions. The latter lends itself better to mousing.

And not just mousing! The thumb sticks can also be used in other, surprising
ways! As I was playing with my layout, lacking a better option, I put `Shift`,
and `One-shot Symbol Layer Switch` onto the same thumb stick (to the left and
down directions, respectively). Now, because Kaleidoscope's one-shot plugin
allows chaining one-shots (as in, one-shots do not cancel each other), I can
pull down the stick, move it to the left, and have both effects applied, without
having to hold the stick in that diagonal position. That is, I can do a swiping
motion with my thumb, and have a modifier and a one-shot layer switch active at
the same time. Tremendously useful, especially on a small keyboard! Comfortable,
intuitive, and an incredibly good use-case for one-shots!

## Wrist pad

The wrist pad on the Shortcut is, simply put, great. Unlike with my daily
drivers, I don't see myself tenting the shortcut, at least not too much, so it
will be sitting firmly on my desk. As such, a built-in pad, with a small... what
do you call it? Pillow? Cushion? Whatever. Those things are sweet. It happens to
be at the right distance for my hand, has just the right firmness, so it feels
perfect.

## The key arrangement

The key arrangement is a tough property to judge. On one hand, being able to
reach every key without having to move my hand is a remarkably comfortable
experience. I can reach all keys without moving the base of my hand, without
lifting it from the wrist pad, and I can do all this without any sense of
discomfort.

On the other hand, I can't put a traditional Dvorak layout on it, there aren't
enough keys on each side, so I had to improvise a little. The arrangement also
makes the learning curve a bit steeper, but I feel it's not too bad to get used
to the layout. I have not typed enough on it yet to be able to see how hard it
is to adapt properly.

## Layers

Because there are so few keys, one needs many layers. Getting used to layers is
one thing - I have a few of them on my daily drivers too! -, but getting used to
using layers a **lot** is an entirely different art, one that I have not
mastered yet. But I'm willing to try, because I have this lingering feel that it
will be worth it in the end.

## Gaming experience

I have tried to play a few games with nothing else but the Shortcut. Obvious
ones that need no mouse or controller input work like a charm. Those that need a
mouse, well, I guess we'll need a dedicated layer for each game, because what
works for Tomb Raider, utterly fails for Oklhos: Omega, or for Diablo 3.

I only spent about an hour on this, though, but the experience is very positive
so far, there are a lot of ways in which the Shortcut can enhance my gaming
experience.

## Use for coding

Like I said before, I'm not used to relying on layers all that much, and the
Shortcut would require me to change that. As such, I don't see myself using the
device for coding, except if I'm traveling: the Shortcut is much smaller, and a
lot easier to transport than either the ErgoDox, or the Model 01.

For this reason, I'll sit down and learn to rely on layers more, it may even
improve how I use my other keyboards!

# Model01: Plugins on demand

A funny thing happened a few days ago: I was happily hacking away on one of the
keyboards, when my wife approached me, and commissioned me to create something
for her for Valentine's day, on the Model 01 prototype. Something that fits the
occasion (there's a heart somewhere), her (her initials appear), and the Model
01 (I have to use the LEDs).

## Kaleidoscope-LED-AlphaSquare

That is how the [Kaleidoscope-LED-AlhpaSquare][fw:alphasquare] was born: it is a
plugin that gives you 4x4 "pixel" fonts, that you can put on the Model 01 LEDs.

 [fw:alphasquare]: https://github.com/keyboardio/Kaleidoscope-LED-AlphaSquare
 
It looks like this:

<video controls>
 <source src="/assets/asylum/images/posts/shortcutting-kaleidoscope/Kaleidoscope-AlphaSquare.webm" type="video/webm">
 <source src="/assets/asylum/images/posts/shortcutting-kaleidoscope/Kaleidoscope-AlphaSquare.mp4" type="video/mp4">
Your browser does not appear to support the `video` tag.
</video>

## Model01-Valentine

Armed with the `LED-AlphaSquare` plugin, I had everything I needed to fulfill
her wishes, for her wish is my command. I ended up doing a bit more than what
she asked for, there's plenty of animation involved:

<video controls>
 <source src="/assets/asylum/images/posts/shortcutting-kaleidoscope/Valentine.webm" type="video/webm">
 <source src="/assets/asylum/images/posts/shortcutting-kaleidoscope/Valentine.mp4" type="video/mp4">
Your browser does not appear to support the `video` tag.
</video>

Of course, this is free software too, and
the [source code is available][fw:valentine].

 [fw:valentine]: https://github.com/algernon/Model01-Valentine

# Future plans

Next up... well. I should find some time to work on [UHK][uhk] too, its long
overdue. On the other hand, there are two important features that would benefit
both the Shortcut, and the Model01 tremendously, something which I want on my
keyboards too, something I've been missing from the ErgoDox EZ for some time
now: bidirectional communication, and a way to store keymaps in EEPROM. At the
moment, one of the biggest objects in my firmware is the keymap, moving that to
EEPROM (along with some other bits of configuration data), would save a lot of
space.

 [uhk]: https://ultimatehackingkeyboard.com/

But, as it is with these things, I can't tell yet what I'll have the time and
motivation for.

However, there is one thing I know for sure, that affects all my future plans:
any firmware work I do, will have to be done before August. The topics of my
posts will drastically change around that time, and will mostly focus on life as
a father of twins. I expect that I will not even be able to get near a keyboard,
let alone type on it for at least the first few weeks, and even after that, my
schedule and daily routine will be drastically different.

Can hardly wait, but I have a lot of code to dish out until then. Thankfully,
with the port to Shortcut, there is now more manpower to work on features
desirable for both products! Whee!

And on this note, I owe many thanks to my lovely wife who lets me do all these
geeky things, and has been a huge supporter all this time! I love you. <3
