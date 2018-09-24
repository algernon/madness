---
title: "The quest for the perfect trackball"
date: 2017-11-15 23:45
tags: [Hacking, Ommatidia, Ergonomics]
---

For the past year and a little more, I have been consciously building a better
working environment at home. I bought an [adjustable standing/sitting
desk][bekant], because I find standing more comfortable, it allows me to walk
around easily (which I do often, while thinking), and being adjustable, if I get
tired, or if I'm playing games, I just lower it and sit.

 [bekant]: http://www.ikea.com/gb/en/products/desks/reception-desks/bekant-reception-desk-sit-stand-birch-veneer-white-spr-19061201/

I invested into a second monitor, so I can use one when standing, and another
when sitting (the other becomes a secondary then). I did this so I can have a
monitor at the proper height whether sitting or standing. I opted for a
two-monitor setup instead of an adjustable arm because this way I can use the
other screen as a secondary, for some less important things.

Because I work with computers, and my hobby is them too, I [needed a better
keyboard][blog:keyboard-start], so I got myself an [ErgoDox EZ][ez], while
waiting for the [Keyboardio Model01][m01] to ship (and recently switched to the
Model01 prototype as my daily driver at home). A split, tentable, tiltable
keyboard is by far the best investment I ever made. It transformed my life, in
many ways. The EZ I tented with its own kit, but for the Model01, I bought
[custom stands][tripod-mount], and it is oh so sweet! For a long time I wanted
to experiment with very high degrees of tenting, but with the EZ, I was limited
to about fifteen degrees. With the Model01, not anymore, and I'm typing this at
around fourty degrees tented. The feeling is wonderful. I imagined I'd like it,
but this much? That never crossed my mind.

 [blog:keyboard-start]: /blog/2015/11/20/looking-for-a-keyboard/
 [ez]: https://ergodox-ez.com/
 [m01]: https://shop.keyboard.io/
 [tripod-mount]: https://uk.hama.com/i/004009/hama-ball-mini-tripod-l-silver

The last piece of the puzzle is the pointing device. I don't like mice, because
my coordination with them is less than stellar, I hate moving my hand around,
and would much prefer the pointing device between the two halves of my keyboard.
This ruled out any mice, so I was looking at trackballs, trackpads, and a few
other solutions. In the end, I went with a [Kensington Orbit][orbit], which is a
great trackball, but not perfect.

 [orbit]: https://www.kensington.com/us/us/4493/k72337us/orbit-trackball-with-scroll-ring

This story is about trackballs, and about my quest to find the best one for me.

<!-- more -->

# Problems with the Orbit

While I love my Orbit dearly, there are a number of issues with it that can't
easily be fixed.

First of all, I can't reprogram it. This may not sound like a big deal, what
else does a trackball need than to be able to move, scroll and click? Well, what
else does a *keyboard* need than being able to input symbols? Don't worry, I'll
tell you about why customisable firmware for my trackball is so important for
me.

It does not have a stand by default, and isn't built neither to be used between
two highly tented keyboard halves, nor to be tilted to match the angle of a
keyboard half. While I could build a stand with reasonable ease, tilting is a
bit more involved. At least during the transition period while I figure out the
right angle. And I may need two, because I prefer different angles when sitting
than when I'm standing.

It seems to me that the trackball was designed to be gripped, the two buttons
are on the two sides, and the frame is such that one's hand can rest on it. This
is sweet if the trackball is flat on the desk. It is less so when you have it
tilted.

The scroll ring is much better than traditional scroll wheels, or solutions that
involve pushing the ball (as opposed to rolling it), but it does not feel
flawless. I can't really put my finger on *why*, but it doesn't, and that's
mighty frustrating.

It only has two buttons, which are enough for most things, but I wouldn't mind
one or two more. Talking about buttons: am I really supposed to press the right
one with my ring finger? That is... not convenient. Not for me.

As you can imagine, certain tasks involve much more hand movement than I am
really comfortable with.

# How it all began

This whole idea of finding a better pointing device started with a picture:

 [![Custom trackball](/assets/asylum/images/posts/quest-for-the-perfect-trackball/custom-ltrac.jpg)][custom-ltrac]

 [custom-ltrac]: https://imgur.com/a/hAOC8

This is not mine, but the wooden frame matches my Keyboardio so very well! I
have a thing for wood, you see, and seeing this picture made me want to explore
what I want from a trackball.

## Ergonomics

I want a wood frame, to match my keyboard. It's that simple. Wood feels good,
looks good, so this is where I'll be starting from.

Unlike the pictured custom device and my Orbit, I want the two main buttons on
my thumb, fairly close, so I can push both of them at the same time if I want
to. My thumb is my strongest digit, I learned to love it typing on ergonomic
keyboards, so I want to make more use of it while using a pointing device too.

The way I intend to use the device is either between the keyboard halves, in
which case it will be raised, but not tented. It may be a bit tilted, to allow
easy resting for my hand, while I operate it. Or, I might place it to the right
of the keyboard, angled similarly, so I can just slide my hand down. For this to
work, I need a frame on which I can comfortably rest my hand.

## Buttons

I need at least two buttons on my thumb, for left and right click, and perhaps a
third for middle clicking, arranged in an arc much like on the Model01. My thumb
and my index finger are my two most dexterous digits, the rest is okay for
typing, but not for clicking mouse buttons. As in, I can do that, and have been
doing it, but... it is not something I like doing. Thankfully I rarely use
anything but the left button, so the situation is not too bad. Nevertheless,
fixing the problem is worth doing, if I'm designing a trackball anyway.

And if I'm resting my hand on the frame anyway, I might as well introduce a palm
key, borrowing from the Model 01. I could use this palm key to toggle between
movement and scrolling, for example. This gets us to our next topic, in a few
moments.

For keys, the arcade-style buttons (as in the picture above) seem good, except
for the palm key. I'm not sure about that one, yet.

## Scrolling

Ideally, I do not want to lift my hand to scroll. I do not want to reposition
either. I want to use the ball to scroll, but I don't want to push the ball. I
want to roll it, like I do for movement. For this, I'll need a key to toggle
between scrolling and moving (I never do both at the same time anyway). Doing
this with a palm key would allow me to easily hold it (or toggle it, or one-shot
it, and so on), and the two button keys would change function too: one would
force scrolling to go horizontal only, the other to vertical. By default, we'd
scroll the direction the ball's spinning the fastest.

## Firmware

To do all of the above, I will need custom firmware, because to the best of my
knowledge, no trackball supports the kind of scrolling I want. Furthermore, I
want to turn the palm key into a kind of one-shot key: it would toggle scrolling
until the ball is spinning, then revert back to movement. This way I won't have
to hold the key, can just tap it.

Another reason for wanting custom firmware, is because I am a big fan of free
software, and of the GPL in particular. Using devices with closed source
firmware is something I'd like to avoid whenever possible. Doubly so on devices
that I use directly, like keyboards and mice.

# The Plan

I do not like hardware. Okay, that is not entirely correct, because I do like
them, just not working with them. I never even assembled my own PC, and that's
easy compared to building a device from scratch. I never soldered, never wired
anything. It's just not my thing. Why am I thinking about building something
then? I could always hire someone to work with me on this. It would yield better
results, faster, and may very well end up being cheaper too. Why then the desire
to do it myself? I suppose it is because that allows me to have shorter feedback
loops, and if I ever end up pushing this idea further than a mere dream, I'd
like to experiment fast. I don't want to wait days or more to have the next
iteration in hand.

But! So far I managed to resist the temptation with saving throws, and all of
this remains an idea. However, in case I fail a saving throw, the next step
would be to draw a trackball, position the keys, and try if I like the layout.
Once I have validated (or invalidated) my assumptions, I can start prototyping
from... uhhh... something. Cardboard, maybe? And when that's over and done, I
shall first build a frame, a big one, so I can handwire the stuff. Then build
the electronics, and program the firmware.

All of these will be documented herein, and everything I make, code, schematics,
whatever, will be released under the GPL. I may end up building the firmware on
top of [Kaleidoscope][kaleidoscope] - so that like my keyboard can have mouse
keys, my trackball would be able to have keyboard keys too.

 [kaleidoscope]: https://github.com/keyboardio/Kaleidoscope

# Blame^WThanks

This post was made possible by Jesse Vincent of Keyboardio, because he made me
get the "hardware bug". If this project ever gets past the dream stage, it will
be entirely his fault. For some reason I think we would be happy with that
outcome.
