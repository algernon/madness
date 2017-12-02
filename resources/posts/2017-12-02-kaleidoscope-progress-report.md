---
title: "Kaleidoscope progress report: 24th November - 1st December"
date: 2017-12-02 01:20
tags: [Hacking, Keyboard, keyboardio]
---

I have changed jobs recently, for a whole lot of reasons I'm not going to ramble
about publicly, except one of them: I wanted to spend more time hacking on
keyboard firmware, [Kaleidoscope][k] in particular. I had a huge backlog - still
do, but less so -, and many, many things that needed to be done, as soon as
possible. Not to mention I enjoy working on it, so I made arrangements in order
to be able to do so.

I figured I might as well work on some of the issues Jesse deemed important,
such as handling suspend/resume of the host better, allowing the keyboard to
work in old BIOSes that do not support N-key rollover, and a few other things,
big and small. This is the story of my work done so far.

By the way, you can follow my [work log][wlog] too, for less in-depth, but
day-to-day progress.

 [k]: https://github.com/keyboardio/Kaleidoscope
 [wlog]: https://github.com/algernon/keyboardio-worklog

<!-- more -->

<a id="section/misc"></a>
# Miscellaneous, smaller things

I started on a Friday, 24th of November, with getting some small things out of
the way. Not going to name them all, you can find them in my [work log][wlog].
I'll keep to the interesting ones only!

 [wlog]: https://github.com/algernon/keyboardio-worklog

## Consumer (media) keys

There was an issue with Consumer Control keys, which are mostly used for volume
control and similar things: they did not repeat when held. This made changing
volume... awkward and annoying at best. The reason for this was that we did have
repeat initially, and it worked fine under Linux, but OSX went bonkers. I don't
remember the details about *how* it went bonkers, but I remember it was pretty
bad. That's when we changed the behaviour of these keys, so that they'd only
fire on release. That made things bearable, but not having repeat was an
annoying thing.

Playing with it, and looking at the code in order to figure out why things work
under Linux, and why they don't on OSX led to a startling discovery: the
Consumer Control report is very different from the keyboard report! In a
keyboard report, any key will only ever appear once. If I call
`Keyboard.press(Key_A.keyCode)` twice, it will still appear in the report only
once. Not so for consumer control keys! We just put them in the report if
there's any free space there, and do not check duplicity. So when such a key was
held, we continued to add it to the report, until it was released, when we
removed one entry. This made the key both function awkwardly, and it made it get
stuck. For some odd reason, this caused no issues on Linux. No idea why, it
really should have.

Nevertheless, once the root cause was discovered, the fix was real easy: do as
`Keyboard` does, and clear the report every cycle. This way we won't have
duplicates (or at least very few, in extremely rare cases), won't have stuck
keys, and all will be well.

It worked, and we now have [repeat for consumer
keys](https://github.com/keyboardio/Kaleidoscope/pull/251)!

## Mouse buttons getting stuck

Having changed consumer control, it occurred to me that mouse keys [getting
stuck](https://github.com/keyboardio/Kaleidoscope-MouseKeys/issues/10) might be
due to a similar issue. In some ways it was, in others, it was not. I did manage
to reproduce the issue fairly quickly, and that revealed that the report is
fine, we are not duplicating anything. The keys get stuck because we never
release them. You see, in a scenario where I push and hold the `Fn` key, click a
mouse button key and hold it, then release `Fn` before the mouse, we never
really release the mouse key.

While the root cause here was different, the solution was - perhaps not
surprisingly - similar: we shall clear the mouse report every cycle too, just
like in the case of `Keyboard`, and more recently `ConsumerControl`.

And with that, no more stuck mouse buttons!

<a id="section/suspend"></a>
# Suspend / resume

The big topic I was working on is handling the hosts suspend and resume events
better. Like, turning our LEDs off when the host goes to sleep, and being able
to wake the host up by pressing keys. Unfortunately, I was not as successful in
this case as in the others before. I did find a way to turn the LEDs off - or do
any other kind of action - when the host goes to sleep, but that [requires
Arduino core changes][a:6964], but that does not help with waking the host up.

 [a:6964]: https://github.com/arduino/Arduino/pull/6964

This is a topic I will have to revisit in the not too distant future. I have a
feeling that the solution is right in front of my nose, I just can't see it. A
few days - or weeks - of resting the issue usually gives new perspective.
Fingers crossed!

<a id="section/mouse-hwheel"></a>
# Horizontal mouse wheel

A newly merged feature are [horizontal mouse
wheels](https://github.com/keyboardio/KeyboardioHID/pull/12), by [Steve
Beaulac](https://github.com/SjB) - an awesome new thing, that also happened to
[break the whole keyboard on
OSX](https://github.com/keyboardio/KeyboardioHID/issues/18). Oops.

Not wanting to back the feature out, I dove deep into the fiery pits of the USB
spec, armed with a bunch of logs from my Linux kernel, to figure out what gives.
It was a wild ride, to say the least.

You see, we quickly narrowed the issue down to the horizontal wheel. From there,
it was just one step to remove the USB HID descriptor that went with it, and
magically, things worked. It wasn't our report, or any other part of the code,
but the HID descriptors. On one hand, that is reassuring, because we just have
to restructure our descriptors. On the other hand, we have to restructure the
descriptors.

Trust me when I tell you, this is dark art. Even though I managed to hammer out
a new descriptor set that works, I do not fully understand why. The report that
goes with it is exactly the same, too. Nevertheless, [the new
descriptors](https://github.com/keyboardio/KeyboardioHID/pull/19/files#diff-482271719524cbe1a76840487519b299)
work on all major platforms.

Funnily enough, now that I'm writing this post, I realised that this whole
exercise of diving deep into HID descriptors was completely and utterly
pointless. It is **not** the restructuring that fixed the issue. It is the
change from `D_USAGE_MINIMUM`/`D_USAGE_MAXIMUM` to
`D_LOGICAL_MINIMUM`/`D_LOGICAL_MAXIMUM`. How very obvious in hindsight!

Good thing I'm writing this post, isn't it? I finally understand what I was
doing! A bit sad that I could have saved so much time and energy if I only
spotted those two lines earlier, but at least I can rest peacefully, with the
knowledge that I finally know what was happening.

<a id="section/boot-proto"></a>
# Boot report protocol

My next big task was - and in some way, still is - to figure out how to make
Kaleidoscope-powered keyboards work in old BIOSes and under operating systems
that do not support N-key roll over. Mostly the former, though I ended up doing
testing with the latter.

Thankfully, Jesse already had an [untested
patch](https://github.com/keyboardio/KeyboardioHID/commit/ad3819614590d5a6710dc19a4c9bbbf7f5676444)
that does a big part of the work, and which served as a foundation for my
attempt.

Before we go into what and why this is, a brief detour, to explain what this
boot protocol actually is: in the old days, keyboards used a simple way of
reporting what keys are pressed, and the report was set up so that six keys
could be reported at the same time, but not more. This report is what old BIOSes
understand, and which pretty much every device can speak. This is also not the
default report mode, according to the USB spec, and this will become important
later.

Additionally, this protocol must be on a separate node than the main keyboard,
to make sure that every BIOS and OS can deal with it. A bunch of them do not
like multiple reports on the same node.

This is why we are pulling in `BootKeyboard`, which implements this boot report
protocol, and is on a separate node. The big idea is that we alter `Keyboard` in
such a way that if the host told us to be in boot mode, we route everything to
`BootKeyboard`. Simple, right? I mean, the USB spec explicitly tells
implementors not to rely on the default boot mode (which is report mode), but
set their desired protocol explicitly.

As it turns out, FreeBSD does not do this. It does not set boot mode, it just
assumes that if a device has the boot subclass, it will speak the protocol by
default. So I had to make it possible to change the default too, in case the
BIOS or the OS fails to set the protocol.

But our journey does not end here: `BootKeyboard` is big, almost half a
kilobyte. That is a steep price to pay for a feature one may not even want (all
my BIOSes speak NKRO, and so does my Linux), so I also had to make it optional.

# Optional mouse keys

During all this, FreeBSD proved to be a great system to test with, because not
only did it not follow the USB spec, not only did it only support boot mode, it
also does not support keyboard and mouse on the same node. If it encounters
something like that, it prefers the mouse. That rendered the factory firmware of
Keyboardio useless, because it includes mouse keys. When I disabled that plugin,
I found it surprising that the mouse descriptors still appeared. The `Mouse` and
`SingleAbsoluteMouse` objects were still compiled in, and not optimized out.
This seemed strange, because these are only used via the `kaleidoscope::hid`
facade, and those functions should have been optimized out. But they weren't.
Why?

They weren't, because the constructor of `Mouse` did some magic to append the
mouse descriptors to what the device reports to the host. And as it is an object
we reference, even if we don't use it, the constructor couldn't be eliminated.
Once discovered, the fix was easy: move this magic from the constructor to the
`Mouse.begin` method. That one gets called early during setup, and unlike the
constructor, can be optimized out. This made the constructor empty, and as such,
droppable too.

While this did not make the factory firmware functional on FreeBSD, it at least
allows one to build a firmware that works there too.

# What is coming up next?

Next up, I plan to move from `Mouse` to `SingleMouse`, to have it on a different
node. This will make the factory firmware of the Keyboardio Model 01 work on
FreeBSD too.

I will also revisit the suspend/resume issues, and work a bit on my backlog.
There are plenty of issues and pull requests waiting on some of my plugins, and
I need to deal with those too.
