---
title: "Emacs setup redone"
date: 2015-01-10 16:00
tags: [Technology, Emacs, Hacking]
---

Recently, I have been taking a new look at my Emacs setup, as I do
every once in a while. Between [`~/.emacs.d`][gh:emacs.d] hacking
sessions, I research new or interesting packages, and once enough has
been bookmarked, I sit down and see how they can improve my
environment and workflow. One of the major themes with the most recent
update is getting rid of distractions. You see, my entire work
environment is tuned to [stay out of my way][b:emacs-well-trained],
and my editor is no different, either. But distraction is not just
something that annoys my eyes! Unorganised, hackish code is also a
huge distraction, because it will always make me want to redo it, to
correct its mistakes.

My Emacs configuration has become a little messy lately, so when I
read about [`use-package`][e:use-package], that finally gave me enough
motivation to go out and rearrange it.

 [gh:emacs.d]: https://github.com/algernon/emacs.d
 [b:emacs-well-trained]: /blog/2013/01/20/an-emacs-well-trained/
 [e:use-package]: https://github.com/jwiegley/use-package

<!-- more -->

After all the rearrangement, customisation and whatnot, the result
looks like the following:

<div style="text-align: center">
<a href="/assets/asylum/images/posts/emacs-setup-redone/emacs-1.png"
   class="thumbnail" style="display: inline-block">
 <img
   src="/assets/asylum/images/posts/emacs-setup-redone/emacs-1.thumb.png"
   alt="[My Emacs setup]">
</a>
</div>

A few things are immediately obvious:

**There are no window borders, menus or toolbars.** Those are all
distractions, and while they can be useful from time to time, I'd
rather toggle them on when needed, rather than have them take up
precious screen estate all the time. This is not new, however, I've
had this setup for years.

**The main editing area is centered, with two big borders on the
side.** I used to dislike this setup very much on web pages, because
there was too much space left blank. But later I realised that for
reading and editing text, this is much easier on my eyes.

**The mode-line is clean and concise.** There are only a handful of
things on the mode-line: row & column position, modification
indicator, file / buffer name, major mode, minor modes (aggressively
diminished, with nice icons), and a clock. All this wrapped in a nice
curved powerline bar: the various parts have different background
colors, so it is fast to find each component quickly with my eyes. But
the colors work great with the theme, and are not otherwise
distracting.

**The current sentence is highlighted.** I get easily distracted. When
I look back at the screen, some very light indication about which
sentence I'm editing is a great help. Also comes handy when I'm
jumping around in the text. A little visual aid that goes a long way.

What can't be seen, is that the configuration became orders of
magnitudes simpler, and easier to navigate. Thanks to
[`use-package`][e:use-package], configuration that belongs to a
package, is close to the package. Things are not scattered around in
different places anymore, but are contained in a simple
<code>(use-package *foo*)</code> block.

 [e:use-package]: https://github.com/jwiegley/use-package

During this exercise, I also learned about a few interesting packages,
such as `fancy-narrow`, `drag-stuff`, `git-messenger`, `flycheck`,
`centered-window-mode`, `sentence-highlight-mode`, `neotree`, `helm`
and others. I also rediscovered `writeroom-mode`, which offers a
completely distraction-free environment, by hiding the mode-line too.

I also learned a lot about navigation (sentence navigation rocks),
paredit and ace-jump.

The next task is to find (or tweak, or create) a theme I'm happy
with. Currently, I'm using the `molokai` theme, but I'm not entirely
satisfied with it. However, theming is a task I have not been able to
convince myself of doing yet. We'll see in a few months time whether I
find one I like, or if I hit a trigger that pushes me over the *lets
develop a theme!* threshold.

Until then, happy hacking!
