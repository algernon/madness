---
title: "ScrollBall"
date: 2018-02-26 16:30
tags: [Ommatidia, Hardware]
---

In [my first post about trackballs][blog:quest], I wrote that I wish to use the ball to scroll, while holding a button. Today, Lenz Grimmer on the `#keyboardio` IRC channel pointed me to the right direction, his short [HOWTO][lenz:scrollball] on how do accomplish this under linux, with any trackball or mouse.

 [blog:quest]: /blog/2017/11/15/quest-for-the-perfect-trackball/
 [lenz:scrollball]: https://blog.lenzg.net/2016/09/enabling-scroll-wheel-emulation-for-the-logitech-trackman-marble-on-fedora-linux-24/

It works surprisingly well! While it is a bit awkward to use my Orbit this way, it does allow me to experiment with the feature. It should be enough to validate the idea. A huge leap forward!

All I had to do is add the following lines to my
`/etc/X11/xorg.conf.d/10-trackball.conf`:

<pre>
Section "InputClass"
 Identifier "Orbit Trackball"
 MatchProduct "Primax Kensington Eagle Trackball"
 Driver "libinput"
 Option "ScrollMethod" "button"
 Option "ScrollButton" "3"
EndSection
</pre>
