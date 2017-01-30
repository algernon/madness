---
title: "Unleashing the Horde"
date: 2017-01-30 19:30
tags: [Hacking, Keyboard, keyboardio]
---

So I had the [Keyboardio Model 01][kbdio] prototype for over a month now, and a
lot has happened since the [last progress report][blog:m01-proto], ranging from
having a colleague re-solder some of the problematic parts of the keyboard,
through fixing a lot of bugs in the plugins, to *Unleashing the Horde*.

 [kbdio]: https://shop.keyboard.io/
 [blog:m01-proto]: /blog/2017/01/03/model01-prototype/
 
We'll go through all of these today, and soon enough, we will see what the horde
I'm referring to, is.

<!-- more -->

# Hardware

## Soldering

Lets start with the scariest part: soldering! I've never done anything like
that, never even seen it, apart form on videos - but never with my own eyes. It
was scary, yet, also looked way too easy. Not going to try it myself, though.

Anyhow, I had to remove the wooden enclosure, unscrew the bottom plate, and a
colleague of mine re-attached the key that fell out during the keyboard's travel
from the US to my doorstep in Hungary. Then he adjusted some of the problematic
parts of the left side, we tried it out, and behold! Everything worked! All 64
keys are now functioning properly, and I can finally use the layout I
originally [came up with][kle:dvorak] for the Model 01.

 [kle:dvorak]: http://www.keyboard-layout-editor.com/#/gists/f938a01e31f6b329364aea02cbda9977

There was one downside of this heroic achievement, however: the wooden enclosure
was very tight, and I had problems removing it. To this day, I have been unable
to put it back, without having the palm keys stuck. This is an issue with the
prototype, mind you, being assembled from junk and spare parts, the final
product will [not have this problem][ks:update:42]. Nevertheless, having the
prototype bare makes it harder to use it, and it is much less comfortable. I
make do, with a couple of tricks, but it was much better with the enclosure on.

 [ks:update:42]: https://www.kickstarter.com/projects/keyboardio/the-model-01-an-heirloom-grade-keyboard-for-seriou/posts/1791204#h:wooden-enclosures
 
I'll keep trying to reattach it, but at this point, I don't have much hope.

## Feelings update

One thing I promised last time, is to report back on how the keyboard feels.
While this whole post was typed on the prototype, I do not feel comfortable
reporting on the feel, because the enclosure was not on it, and that's a vital
part of it.

What I can tell, is that the layout is superb, even though I have trouble
adjusting to the second columns from the outside (R1 and R14). I always hit the
wrong key there. But, I'm a very slow learner, it took my right hand *years* to
unlearn QWERTY, even though my left was able to pick up Dvorak within weeks.

With all keys working, I was able to reach 40WPM in a matter of hours, with over
95% accuracy, which is not so bad, all things considered. It's about half my
speed on the ErgoDox, but the ErgoDox started much worse, and as I said, my
hands are terribly slow to learn new layouts.

# Core firmware

## Layer handling

While working on other parts of the firmware, I accidentally ran into an issue
with layer toggles: there was no way to toggle them off. Once a layer was
toggled on, it stayed there. This has been broken since my layer refactor a
month or two ago - apologies!

Adding a couple of lines of code allowed the firmware to toggle layers off when
the same key is pressed again, and now both momentary layers and layer toggles
work flawlessly!

## Pluginification

In an attempt at making the firmware easier to port in the near future, parts
that are not necessarily part of the core, are slowly moving into plugins. LEDs
being the latest one.

This makes the firmware easier to port, easier to extend and compose with other
plugins, and also much lighter by default.

## Porting

Jesse expressed his desire to have the firmware support keyboards other than the
Model 01, eventually. This is a worthy goal, and one I'm very interested in too,
for a number of reasons.

While there is nothing official yet, I can tell you this: the firmware can be
made to work on the ErgoDox EZ, with not too many changes. The way I
accomplished that is one of the worst hacks I ever made, but it proved the
point, that this is an attainable goal!

Expect news on this front too in the next couple of weeks.

## Core firmware state

While the above changes may seem substantial, they are not. As far as
functionalities go, the core firmware changed little: most of the work was
reorganisation.

The structure was already great to work with, but with more things moving to
plugins, it's even better! From an end-user point of view, these things matter
very little, but from a developer one, these changes are incredibly welcome.

# Plugins

Another big area of work are the plugins. Last time, barely anything worked, and
I was just getting started. Today, almost everything works flawlessly, and there
are only some corner cases to iron out. Nothing shows this better than being
able to type a whole blog post, with all the markup and whatnot, on the
prototype itself.

All the functionality I wanted for daily use, are there, and they all behave
like I wanted them to. The plugins which I don't personally use, are in a good
shape too: I've been testing those extensively, too. But the big test will be
once they reach the hands of real users. I expect there will be some bugs to
iron out still.

## The Horde

The major news this time, apart from the vast majority of plugins nearing a
stable, reliable state, is that Akela is kind of no more. Again.

### The beginning of the end

It all started with Jesse asking me which one or two plugins I'd consider worthy
of being made more official. For this to work, the plugins had to be lifted out
of the monolithic Akela repository. Each and every plugin gained its own place,
and during this, a few more plugins were born, and thus, the Horde of Plugins
were unleashed!

### Intermission

Once the plugins were split out into **20** different repositories, the Akela
project became an umbrella, that pulled them together as sub-modules, provided
some build tools, and examples, and a few other things like this. It was used as
a way to compile the plugins in Travis, to make sure they always at least
compile.

### The end the umbrella

Soon after, the decision was made to transfer all these plugins to the
Keyboardio organisation on GitHub, and make them part of
a ["recommended" set of libraries][gh:kbdio-libs]. All other plugins that were
previously in `KeyboardioFirmware` were moved here too, and some more
functionality that used to be part of the core, were also extracted into
libraries.

 [gh:kbdio-libs]: https://github.com/keyboardio/keyboardio-libraries

### Summary

All of this has a lot of benefits:

* It makes it easier to install the libraries: just clone the umbrella repo, and
  you get them all! 
* It will make it easier to install the plugins via the Arduino Library Manager!
* Firmware sketches are now even more composable.

## New plugins

There were a few new plugins introduced into the fold recently, one of them was
even demoed in the latest [kickstarter backer update][ks:blazing]!

 [ks:blazing]: https://www.kickstarter.com/projects/keyboardio/the-model-01-an-heirloom-grade-keyboard-for-seriou/posts/1791204#h:next-update

### LED-ActiveModColor

A very simple plugin, that lights up the LED in white under any active modifier,
for the duration of its activity. Also supports one-shots.

### LED-Stalker

Demoed in the backer update, this adds an effect that *stalks* your keys:
whenever a key is pressed, the LED under it lights up, and the slowly fades away
once the key is released. This provides a kind of trailing effect.

There are two color schemes currently: **Haunt**, which is a white-ish, ghostly
color that follows your fingers, and *BlazingTrail*, demoed in the video, which
lights your keyboard on fire. It looks much better in real life.

### GhostInTheFirmware

While preparing the video for the backer update, I came to the conclusion that
trying to hold the camera in one hand, and then typing with the other is not
only hard and uncomfortable, it is also pointless. Not only does my hand
obstruct the video by hiding keys, there are much better ways to demo an effect:
automate it!

This is what `GhostInTheFirmware` does: it plays a pre-recorded sequence, by
emulating key presses, just as if the user physically pressed them. Frees up a
hand, is repeatable, and looks better too!

# Next up...

I do not yet know what is next. I need to do some work on the [UHK][uhk], there
is plenty of work left with the Keyboardio too (although considerably less
now!), and I have a package incoming next week. We will see what happens!
 
 [uhk]: https://ultimatehackingkeyboard.com/
