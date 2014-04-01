---
title: "Motif on Wayland"
date: 2014-04-01 13:45
tags: [Technology, Hacking, .planet.debian]
---

Earlier this year, on the fourth of February, [Matthew Garrett][mjg59]
posted an interesting [tweet][tweet:motifway]. The idea of porting
[Motif][motif] to [Wayland][wayland] sounded quite insane, which is
right up my alley, so I've been pondering and preparing since. The
result of that preparation is a fundraiser [campaign][igg:motifway],
which, if successful, I'll dive deeper into the porting effort, to
deliver a library that brings the Motif we used to love back in the
days to a modern platform.

 [mjg59]: http://mjg59.dreamwidth.org/
 [tweet:motifway]: https://twitter.com/mjg59/status/430723377653755904
 [igg:motifway]: http://igg.me/at/motif-on-wayland
 [motif]: http://motif.ics.com/
 [wayland]: http://wayland.freedesktop.org/

<!-- more -->

The aim of the project is to create a library, available under the
[GNU Lesser General Public License][lgpl] (version 2.1 or later, the
same license original Motif is under), ported to Wayland, with full
API compatibility if at all possible. In the end, we want the result
to feel like Motif, to look like Motif, so that any program that can
be compiled against Motif, will also work with the ported library. I
will start fresh, using modern tools and modern methodologies
(including, but not limited to autotools and test-driven development,
on [GitHub][github]) to develop the new library, instead of changing
the existing code base. Whether the goal is fully achievable remains
to be seen, but the API - and the look of the widgets, of course -
will feel like Motif, even if in a Wayland context, and we will do our
best to either make the API 100% compatible with Motif, or provide a
compatibility library.

 [lgpl]: https://www.gnu.org/copyleft/lesser.html
 [github]: https://github.com/madhouse/motifway

Since I have a day job, in order to be able to spend enough time on
the library, I will need funding that more or less matches my salary.
The more raised, the more time will be allocated to the porting
project. Would we exceed the funding goal, there are a few stretch
goal ideas that can be added later (such as porting the Motif Window
Manager, and turning it into a Wayland compositor, with a few modern
bells and whistles).

For further information, such as perks, updates and all, please see
the [campaign][igg:motifway], or the [project website][website]!

 [igg:motifway]: http://igg.me/at/motif-on-wayland
 [website]: http://madhouse.github.io/motifway
