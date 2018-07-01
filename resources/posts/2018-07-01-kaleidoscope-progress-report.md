---
title: "Kaleidoscope progress report: 2017-12-01 - 2018-07-01"
date: 2018-07-01 22:35
tags: [Hacking, Keyboard, keyboardio]
---

It's [been a while][blog:progress-report-1] I wrote about what's happening with
[Kaleidoscope][k], and I've been putting off writing this post for so long, that
I can't ignore it anymore. I've been putting it off because a lot of things
happened, and many more are under development. It's a huge amount of work, even
to summarize. Fortunately, there's a lot of good stories to tell.

 [blog:progress-report-1]: /blog/2017/12/02/kaleidoscope-progress-report/
 [k]: https://github.com/keyboardio/Kaleidoscope

<!-- more -->

<a id="section/suspend"></a>
# Suspend / resume

Last time I wrote that I found a way to respond to host suspend and resume
events, but wasn't able to make the keyboard able to wake the host up - but felt
that the solution is right in front of my nose. I was right, it was right there.
Funnily enough, the solution is closely linked to our next topic - boot
protocol.

You see, for a keyboard to be able to wake the host up, it needs to signal in
its configuration that it is capable of doing so. We do that in Kaleidoscope,
and have been doing so since day one. Yet, it did not work. We also need to do
some USB magic to wake the host, which we have been doing since day one too.
Yet, it did not work.

On the other hand, my other keyboards were able to wake the host, so I went and
compared what they do, and what we do differently. Turns out, that to wake the
host, we have to support the boot protocol too. We did that too, since December,
but waking the host up still did not work. Not by default.

It turns out that we could tell the operating system (be that Windows, Linux, or
MacOS) that *yes, we want this keyboard to wake the host!*. But we had to tell
them, they did not default to it. They did not, because they only allow devices
that implement a boot keyboard to wake the host up. With proper fallback and
support for the boot protocol, we have that, and wakeing up works by default
too!

It took a while to get here, we had a lot of dead ends, but I think we ended up
with something reasonable, given our constraints.

<a id="section/boot-proto"></a>
# Boot report protocol

Oh, boot protocol, my old nemesis! Back in December, I thought I'm done with it,
but it wasn't meant to be. I'd rather not explain the whole story, because it
still haunts me. We had to go as far as buying me a Mac Mini so I can test on
OSX, and figure out what goes wrong (and I *don't* like OSX; I like even Windows
better, and I have a history with that OS). It was that bad.

The gist of it is that a lot of BIOSes, and even some operating systems, or boot
loaders do not parse HID descriptors at all, nor do they set the protocol to
boot mode. They just expect the descriptors to be the same as an example in the
spec. Our descriptor wasn't the same, it was a bit further optimized, which
rendered `BootKeyboard` useless with anything that didn't parse the HID
descriptors.

So we switched the descriptors, redid the whole fallback mechanism, and even
made it possible for the user to switch protocols forcibly, without reconnecting
the keyboard (see [Model01-Firmware#55][mf/55] for details).

 [mf/55]: https://github.com/keyboardio/Model01-Firmware/pull/55

The result? We now properly support the boot protocol, and the keyboard works
fine under BIOSes, GRUB, OSX's FileVault, the Windows disk password prompt, and
so on. Most of the time, out of the box. For the few rare times it does not, one
can forcibly change the protocol.

An interesting part of this whole story is that it turns out, OSX is the only
operating system that behaves according to spec: it sets the boot protocol
explicitly when in FileVault, and sets it to report once it fully loads (and
keeps setting it back if it sees the keyboard change back to boot). Neither
Linux nor Windows do this. Surprising, to be honest, but happy that there's at
least one operating system that does at least one thing according to spec!

<a id="section/main-loop-speed"></a>
# Main loop speedup

We made a very simple change to how keyswitch events get handled, which resulted
in a huge speedup in the vast majority of cases. We used to call the keyswitch
event handlers for each and every plugin, every cycle, even if the keys were
idle. This took a lot of time, and most plugins weren't interested in the idle
state at all, anyway.

So we simply removed this part of the code. When a keyswitch is idle, we won't
be calling any event handlers. Due to keys being idle is how they spend the vast
majority of their time (it is fairly unusual to have more than 10 keys pressed
at a time, and even in that case, we have 54 more idle), this has been a
tremendous boost to our cycle speed, shaving off more than a full millisecond
when the keyboard is idle, and close to a millisecond with fast (close to
100WPM) typing.

<a id="section/ergodox-port"></a>
# ErgoDox port

I wanted to use Kaleidoscope on my [ErgoDox EZ][ez] for a long, long time now,
even had a few attempts at porting before, but always ran into issues and hit a
dead-end. One day, when I wanted to relax and do something different, to let my
mind wander, let it do something else for a change, I attempted another port.
Because I learned a lot about keyboards since my previous attempt, this port was
quick, and rewarding.

We can now use Kaleidoscope on the ErgoDox EZ, and any other ErgoDox - or
ergodox-like keyboard - that is compatible with it, like the original ErgoDox,
or [Dactyl][t:dactyl].

 [t:dactyl]: https://twitter.com/joedevivo/status/997874840005632005
 [ez]: https://ergodox-ez.com/

Kaleidoscope running on the [Dactyl][dactyl] makes me incredibly happy, because
the Dactyl was one major reason [I started looking][b:looking] into mechanical
keyboards.

 [dactyl]: https://github.com/adereth/dactyl-keyboard
 [b:looking]: /blog/2015/11/20/looking-for-a-keyboard/

<a id="section/hid-facade"></a>
# Pluggable HID adaptors

Probably not too interesting for most people, but we made the HID layer mostly
pluggable. This means that if one wants to use a different HID library, this is
now possible. This also opens up the way to implementing a Bluetooth keyboard.

<a id="section/new-plugin-api"></a>
# New plugin API

Thanks to [@noseglasses][noseglasses], we have a new plugin API and hook system.
While Jesse and myself made a lot of changes with regards to naming and code
structure, the core idea remained the same.

 [noseglasses]: https://github.com/noseglasses

This is one of the most exciting developments lately, at least for myself, and
probably other developers, because the new system is far more efficient than the
old. We no longer keep a statically allocated array for hooks - saving
significant amounts of RAM. We no longer need plugins to register their event
handlers and loop hooks - they just need to re-implement a few methods inherited
from `kaleidoscope::Plugin`.

The new system is not only more efficient, it is also considerably lighter. It
made adding new hooks *cheap*, and new hooks we will have.

Not only that, but the naming is - in my opinion at least - a lot better. The
new API should be easier for developers to use.

Just don't look at the code implementing it. That's a smaller ball of mud. But
sometimes small balls of mud can do wonders.

<a id="section/deprecations"></a>
# Deprecations and Upgrading

There have been a couple of deprecations, bigger and smaller. When compiling
code that uses deprecated interfaces, the compiler should loudly warn about
them, with pointers to an upgrade path. We prepared an
[UPGRADING.md][upgrading.md] document, and a [forum post][f:upgrading] to aid
with the upgrades.

 [upgrading.md]: https://github.com/keyboardio/Kaleidoscope/blob/master/UPGRADING.md#kaleidoscope-upgrade-notes
 [f:upgrading]: https://community.keyboard.io/t/user-visible-major-breaking-changes-coming-in-kaleidoscope/1665?u=algernon

Going forward, we'll be keeping both of these updated, to make it easier for
both end-users and developers to upgrade their code.

<a id="section/miscellaneous"></a>
# Miscellaneous

Apart from the big changes I mentioned above, there have been plenty of
bugfixes, some new features, even new plugins! Too many things to list here,
really, so if you are interested, have a look at my [work log][worklog].

 [worklog]: https://github.com/algernon/keyboardio-worklog/blob/master/worklog.md#readme

<a id="section/thanks"></a>
# Thanks

Many thanks to [@noseglasses][noseglasses], [Michael
Richters][gedankenexperimenter], and [Jesse][obra] for their contributions,
ranging from ideas, brainstorming, through code, reviews, to naming things. Huge
parts of our progress since last December would not have been possible without
them.

 [gedankenexperimenter]: https://github.com/gedankenexperimenter
 [obra]: https://github.com/obra

<3
