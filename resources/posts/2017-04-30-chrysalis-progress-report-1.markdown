---
title: "Chrysalis Progress Report #1"
date: 2017-04-30 18:25
tags: [Hacking, Keyboard, keyboardio, Shortcut, Chrysalis, Patreon]
---

The past month has been eventful in many ways: we've seen the Twins three times
on ultra sound, made huge progress towards moving to a bigger apartment, and
last but not least, tremendous progress was made on [Chrysalis][chrysalis]. It
even has useful features now, so much so, that an [alpha release][cs:alpha] was
tagged too. You can see a demo video just below, and try the pre-built binaries
yourself, no Kaleidoscope-powered hardware required, either!

 [chrysalis]: https://github.com/algernon/Chrysalis
 [cs:alpha]: https://github.com/algernon/Chrysalis/releases/tag/chrysalis-0.0.1

<video controls width="550" height="295">
 <source src="/assets/asylum/images/posts/chrysalis-progress-report-1/Chrysalis-demo.webm" type="video/webm">
 <source src="/assets/asylum/images/posts/chrysalis-progress-report-1/Chrysalis-demo.mp4" type="video/mp4">
Your browser does not appear to support the `video` tag.
</video>

Those of you who are interested in the details of what works, what does not, and
how, please continue reading.

<!-- more -->

# Chrysalis conceived

One of the goals of [Chrysalis][chrysalis] is to be a cross-platform
application, that works on all three major operating systems. Having no desire
to develop on anything but GNU/Linux, having no access to OSX, and having worked
with native toolkits before, I decided very early on that I will base the
application on [Electron][electron]. It may make it bigger, bloatier, more
resource intensive, and has a whole lot of undesired side-effects, but it has
one property that shadows all else: it is portable, I don't have to deal with
native UIs, or anything silly like that.

 [chrysalis]: https://github.com/algernon/Chrysalis
 [electron]: https://electron.atom.io/

I have not been working on Chrysalis actively until
the [13th of April][cs:april-13], ideas were brewing until then. At that point,
I sketched up a proof of concept in JavaScript, that got as far as detecting
devices, and having a primitive REPL. This was a huge thing for me, because my
biggest, and most complicated JavaScript project up until this point
was... [considerably simpler][gh:ph].

 [cs:april-13]: https://github.com/algernon/Chrysalis/tree/974cf5a2cf8b0033860ab5758aa4cb54030e567c
 [gh:ph]: https://github.com/algernon/potential-happiness

While I got reasonably far with the JavaScript version of Chrysalis, it was a
pain to work with. The way I think is radically different than the language. I
can overcome that when working on firmware, because I spent many years writing
C, and I can bend my will to think the way I need to write C++. The same can't
be said of JavaScript. Even if I learned to write it properly, it would not feel
neither natural, nor convenient. Yet, when it comes to developing user
interfaces, those two properties are a must, otherwise I'll never get anywhere.
You see, I'm not a UI person. I have little experience in the area, and very
little desire to gain more. On the other hand, Chrysalis is a piece of software
that - I hope - will be useful to many people. Even to me. The potential to be
useful for a lot of keyboard users offsets my disdain of developing user
interfaces.

So now that I decided that I want to work on Chrysalis, and it must be based on
Electron, how do I make this work fun?

# Enter ClojureScript

[Clojure][clojure] has been my go-to language for a while, and while I have
written quite a bit of Clojure code, most of it I was not allowed to release as
free software. But with being dissatisfied with JavaScript, I turned
to [ClojureScript][cljs] instead: it compiles to (and inter-operates well with)
JavaScript, has an [awesome library][reagent] for working with [React][react],
and is much closer to the way I think than JavaScript.

 [clojure]: https://clojure.org/
 [cljs]: https://clojurescript.org/
 [reagent]: http://reagent-project.github.io/
 [react]: http://facebook.github.io/react/

I spent about a week writing the initial JavaScript version of Chrysalis, but on
the 23rd of April, I [rewrote it][cs:cljs] in ClojureScript. The initial rewrite
didn't do much, just laid a foundation, and provided me with a test bed to
validate some of the ideas I had. Things started to move fast after the rewrite...

 [cs:cljs]: https://github.com/algernon/Chrysalis/tree/3c9d7785b553894ca14dc6291331002dd38e3c72

## Building a REPL

The first thing I built after a device selector was a REPL, so that I can send
commands to the selected keyboard, and display the results. A kind of advanced
mode, one could say. I built this first, because this required the least amount
of user interface: it's just an input box, and a history of past commands laid
out below. Not rocket science.

 [![Chrysalis REPL](/assets/asylum/images/posts/chrysalis-progress-report-1/repl.png)](/assets/asylum/images/posts/chrysalis-progress-report-1/repl.full.png)

There are a number of interesting things about it, however, mostly things that
try to make it easier to use it. One such thing is the built-in help: press the
gear icons by the prompt, select help, and you'll have the commands supported by
the keyboard pop up in a window, like the `help` output you screen on the
screenshot, too. The history items can be collapsed, or the command repeated
(pasted into the input box, so you can edit it), among other things.

Under the hood, there is an extensible framework, that makes it possible to
create custom commands, augment the input, post-process the output, and change
how the results are displayed. All of these can be easily hooked into, and it is
how the `help` formatting has been done, too.

I'm quite proud how this all turned out, both the user interface, and the
supporting framework behind it. Nowhere near perfect, and I expect that it will
be redone a few times before we hit the first stable release of Chrysalis, but
it's looking good, nevertheless.

## Selecting a device

Funnily, the device selector was tuned later than the REPL: there was a
placeholder early on, but I started to polish it long after the REPL. It used to
be a much more complex part of Chrysalis, but recently, I moved a lot of code
out of it into the core application.

 [![Chrysalis Device Selector](/assets/asylum/images/posts/chrysalis-progress-report-1/devices.png)](/assets/asylum/images/posts/chrysalis-progress-report-1/devices.full.png)

By this point, it does not do much, just selects the device. It does not try to
open it, nor communicate with it or anything like that.

## Firmware flashing

Likely the first useful feature for new users is the firmware flashing page:
select a firmware, hit the upload button, and you're done! Okay, you'll have to
push the program button on the keyboard too, or something similar, but it is
still a million times easier than trying to figure out the correct incantation
to call `avrdude` with.

 [![Chrysalis Firmware Flasher](/assets/asylum/images/posts/chrysalis-progress-report-1/firmware.png)](/assets/asylum/images/posts/chrysalis-progress-report-1/firmware.full.png)

# Chrysalis 0.0.1

Earlier today, I released [Chrysalis 0.0.1][cs:alpha], the first alpha release
of the application, with binaries built for all major platforms (but only tested
on GNU/Linux).

 [cs:alpha]: https://github.com/algernon/Chrysalis/releases/tag/chrysalis-0.0.1

Testing and bug reports would be most appreciated! You don't even need a
Kaleidoscope-powered device, as there is a virtual device shipped with the
application, with which one can test a few things.

## Features

### Device auto-detection

Chrysalis can detect the Keyboardio Model 01, and the Shortcut, and will also
offer a virtual device. It does not notice when devices are plugged in or out
yet, but one can trigger a new scan by selecting the `Scan devices` option from
the `Chrysalis` menu in the header.

### Firmware flashing

There is some rudimentary firmware flashing support: one can select a file and
flash it. However, if the flashing fails, Chrysalis tends to end up in a
confused state, and may need to be restarted. This is a known bug.

### Read-eval-print-loop

Send commands to the keyboard! This is likely the most polished part of the
application at this point. A few commands have special display routines, but
most just display as received from the keyboard. Still early stages, most of
fundamentals are there, we just need to build the user interface, and polish it
a little.

# What's next?

You can follow development on [GitHub][chrysalis], we even have a [project set
up][gh:cs:project] to track the tasks and issues remaining until the first
stable release. I'm also posting updates on [the fediverse][fediverse:chrysalis]
and on [Patreon][patreon] too.

 [gh:cs:project]: https://github.com/algernon/Chrysalis/projects/1?fullscreen=true
 [fediverse:chrysalis]: https://trunk.mad-scientist.club/tags/chrysalis
 [patreon]: https://www.patreon.com/algernon

# Thanks

I want to thank all my [patrons][patreon], and all of the people on Twitter and
elsewhere, who are following the Chrysalis development along. Without them this
little post may have never been written.
