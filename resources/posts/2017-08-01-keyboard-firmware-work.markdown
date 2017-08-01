---
title: "Keyboard Firmware work"
date: 2017-08-01 21:00
tags: [Hacking, Keyboard, keyboardio, ErgoDox]
---

It has been a while I wrote about keyboards - or anything at all, really -, so
it is high time I do that, because there has been a lot of progress made on
various fronts. We will touch a number of topics today, ranging
from [Kaleidoscope][kaleidoscope] to [QMK][qmk] related ones. As a teaser, we
will talk about [hid-io][hid-io], feedback from the Keyboardio PVT run so far,
and Emacs.

 [kaleidoscope]: https://github.com/keyboardio/Kaleidoscope
 [qmk]: https://github.com/qmk/qmk_firmware
 [hid-io]: https://github.com/hid-io/hid-io

<!-- more -->

## [hid-io][hid-io]

 [kaleidoscope]: https://github.com/keyboardio/Kaleidoscope
 [qmk]: https://github.com/qmk/qmk_firmware
 [hid-io]: https://github.com/hid-io/hid-io

`HID-IO` is a low-level protocol that will implement bi-directional
communication between keyboard and host, similar in spirit
to [Focus][kaleidoscope:focus], but only in spirit. Both set out to make it
possible to talk to the keyboard, and do things we wouldn't otherwise be able
to, but they took a different approach. While `Focus` is implemented over a
virtual serial port, `HID-IO` is built on top of raw HID. `Focus` is a
text-based protocol, `HID-IO` is binary. `Focus` is based on convention,
`HID-IO` on a spec. `Focus` is easily extensible by anyone, `HID-IO` works
strictly by the spec.

 [kaleidoscope:focus]: https://github.com/keyboardio/Kaleidoscope-Focus

Both approaches have their ups and downs: `HID-IO` is more compact, and faster
to transfer over the wire, and has a specification implementations should adhere
to; but it is harder to debug (there are more tools that can talk serial than
rawhid), harder to extend, and absolutely requires a host-side application.
`Focus` is more extensible, easier to debug, at the cost of being more verbose,
and less strict.

These may seem like conflicting efforts, and in some way they are, yet, both
have reasons to exist, and can coexist happily: we can use `HID-IO` to implement
what the spec allows, and use `Focus` to add ad-hoc functionality on top.

The biggest reason I started looking at hid-io was its promise to make
cross-platform unicode input possible via the host-side application. Turns out
that this is a feature desired by others too, so much so, that we agreed with
Erez Zukerman (of ErgoDox EZ) and Jack Humbert (of OLKB) that I'll be spending
some time on helping develop `HID-IO`, and improve [QMK][qmk] to support it on
the device side.

Over the past few weeks, I made some progress in this regard: first by
submitting feedback and questions about the spec proposal, then by implementing
a [small library][libhid-io] that I plan to use in both [QMK][qmk]
and [Kaleidoscope][kaleidoscope] to implement the device-side of the protocol.

 [libhid-io]: https://github.com/algernon/libhid-io

At the time of this writing, it is not wired up to either firmware, as I'm
fighting with creating a host-side program that I can use to talk to the
keyboard in a convenient way. Once I have that, I'll wire up [QMK][qmk] to use
the library, and start implementing useful stuff like layer switching.

Progress reports on this front will be posted on this blog whenever there is
something interesting to share. Expect an update in a few days!

## [Model 01][model01] PVT feedback

 [model01]: https://shop.keyboard.io/

A few people started to receive their Model 01s from the PVT run, and as
expected, a [few][issues:140] [issues][issues:145] [here and there][issues:149],
most of them have either been fixed by now, or have a patch pending.

 [issues:140]: https://github.com/keyboardio/Kaleidoscope/issues/140
 [issues:145]: https://github.com/keyboardio/Kaleidoscope/issues/145
 [issues:149]: https://github.com/keyboardio/Kaleidoscope/issues/149

Of these, the biggest - and most useful - change done by Jesse is that the
firmware will now report its touchpad interface on a separate node. This means
that it will no longer appear as a keyboard + mouse + touchpad device, but as a
keyboard + mouse on one node, and touchpad on another. This has the huge
advantage that this works out of the box on all major operating systems (the
previous setup required a [libinput patch][libinput:patch] on Linux).

 [libinput:patch]: https://bugs.freedesktop.org/show_bug.cgi?id=99914

I haven't done much work on Kaleidoscope or plugins, just a few small things,
bugfixes mostly.

## Emacs

Based on an idea I received over [twitter][twitter:evil-flash], I ended up
writing an [emacs package][kaleidoscope.el] that makes it easier to talk to
Kaleidoscope-powered devices. As an example, the `kaleidoscope-evil-state-flash`
package will flash the keyboard in the color of the Evil state we are switching
to. In other words, whenever we change Evil state in Emacs, the keyboard will
display its color briefly.

 [twitter:evil-flash]: https://twitter.com/leoj3n/status/890404448815730689
 [kaleidoscope.el]: https://github.com/algernon/kaleidoscope.el

A video showing how it works can be seen below (in terrible quality):

<video controls width="640" height="360">
 <source src="/assets/asylum/images/posts/keyboard-firmware-work/2017-08-01/kaleidoscope.el-demo.webm" type="video/webm">
 <source src="/assets/asylum/images/posts/keyboard-firmware-work/2017-08-01/kaleidoscope.el-demo.mp4" type="video/mp4">
Your browser does not appear to support the `video` tag.
</video>

## Chrysalis

There has been a [Chrysalis][chrysalis] release in June, which I have not talked
about yet. While the new release has a number of interesting features, by far
the most important one is that its inner workings were completely rebuilt on top
of the [re-frame][re-frame] library. This fixed a whole lot of issues and
shortcomings of the initial design, and paves the way for easier development in
the future.

 [chrysalis]: https://github.com/algernon/chrysalis
 [re-frame]: https://github.com/Day8/re-frame

There was some polish in various areas of the application, but none of those are
all that interesting compared to the internal redesign.

## Next up

For the next few weeks, my main focus will be on `HID-IO`, as in the past few. I
do have a few ideas for [Chrysalis][chrysalis] too, and have a new Kaleidoscope
plugin in the works as well, but I can't tell yet if I'll have the resources to
work on those too.

Nevertheless, I will shift my focus to Chrysalis again by the end of August
(Twins permitting), so that we can bring it to a useful state by the time the
rest of the Model 01s start to ship to backers.
