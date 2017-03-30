---
title: "FingerPainter"
date: 2017-03-30 23:30
tags: [Hacking, Keyboard, keyboardio, Patreon]
---

As promised [last time][blog:on-the-fly-remapping], I will be
sharing [Kaleidoscope][kaleidoscope]-related developments more often, and as it
happens, there are interesting news to share today!

 [blog:on-the-fly-remapping]: /blog/2017/03/27/on-the-fly-key-remapping/
 [kaleidoscope]: https://github.com/keyboardio/Kaleidoscope

In the last [Keyboardio Model 01 backer update][m01:day-648], Jesse mentioned
that I may be working on something we called "*finger panting*", born from
another off-hand remark on IRC (**hint**: be careful with off-hand remarks
around me, I may implement them!). It's an editable LED mode: we send a command
to the keyboard to enter *edit mode*, and in that mode, keys turn into pixels
under your fingers. Each time you press them, they will change color to the next
one in a palette, which makes it possible to draw little pictures on a tiny
canvas.

 [m01:day-648]: https://www.kickstarter.com/projects/keyboardio/the-model-01-an-heirloom-grade-keyboard-for-seriou/posts/1840149#h:firmware

An entirely new form of art!
 
<!-- more -->

Without much further ado, let me present you a demonstration:

<video controls>
 <source src="/assets/asylum/images/posts/FingerPainter/fingerpainter.webm" type="video/webm">
 <source src="/assets/asylum/images/posts/FingerPainter/fingerpainter.mp4" type="video/mp4">
Your browser does not appear to support the `video` tag.
</video>

Apologies for the quality, I only had an ancient smartphone available. I hope
that the video aptly demonstrates how to use the plugin.

The palette is - of course - modifiable, via the same bi-directional interface
as most other, configurable things lately. The editing can be toggled via the same interface too.

For comparison, this is how the EGA and Pastel (used in the video) palettes look
like:

 ![EGA palette](/assets/asylum/images/posts/FingerPainter/fingerpainter-ega-palette.png)
 ![Pastel palette](/assets/asylum/images/posts/FingerPainter/fingerpainter-pastel-palette.png)
 
The difference ain't that big here, because I only used seven colors, while the
EGA has more. The bad quality of the phone does not do them justice, either -
they are noticeably different (and much better looking) in reality! But the
point is, you can use any 16 colors you wish, the only limit is the sky.
