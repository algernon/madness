---
title: "Chrysalis Progress Report #2"
date: 2017-05-17 13:50
tags: [Hacking, Keyboard, keyboardio, Shortcut, Chrysalis, Patreon]
---

The past few weeks were quite a challenge, but we are slowly settling down at
our new place (where I now have a proper desk, with enough space for all the
gadgets, whee!), and [Chrysalis][chrysalis] has been moving forward nicely too,
with some major changes all around the place. While you can see some of those
changes in the video below (just compare it to
the [previous one][blog:chrysalis-1]!), a lot of the changes were made under the
hood, and can't be seen. I'll be going into more details, but first, lets see
how things look as of today!

<video controls width="621" height="336">
 <source src="/assets/asylum/images/posts/chrysalis-progress-report-2/Chrysalis-demo.webm" type="video/webm">
 <source src="/assets/asylum/images/posts/chrysalis-progress-report-2/Chrysalis-demo.mp4" type="video/mp4">
Your browser does not appear to support the `video` tag.
</video>

 [chrysalis]: https://github.com/algernon/Chrysalis
 [blog:chrysalis-1]: /blog/2017/04/30/chrysalis-progress-report-1/

<!-- more -->

# User-visible changes

For our end users, the most important changes are those they can see, or benefit
from it some other way. While a lot of changes in Chrysalis recently has been
under the hood, there are plenty of user-visible changes too.

## LED Theme Editor

Likely the most interesting new feature is the `LED Theme Editor`, which still
needs a lot of polish, but the basic building blocks are there. With the theme
editor, one will be able not only to change the current theme on the keyboard,
but save presets too, and apply them easily.

To make this work, I had to design a few things under the hood:

* I needed a way to load, and the augment SVG files, so that events can be bound
  to parts of it, colors bound, and so on. Some of this code will be reused for
  the layout editor, too.
* To make presets possible, Chrysalis needed a way to save parts of its state.
  We don't want to save all of the state, because there are a lot of transient
  stuff there that do not need to be saved. So I sketched out a little framework
  that allows parts of Chrysalis to save and load their state, whenever they see
  fit to do so.
  
## Wire Traffic Spy

While the `REPL` can display commands we write their, interpret the results and
display it in a number of accessible ways, sometimes - especially when
debugging - we want to have a look at the raw data that goes through the wire.
The `Wire Traffic Spy` page does just that: everything that goes through
Chrysalis, be that explicitly or through using Chrysalis itself, will be shown
on this page, without any of the fancy display the `REPL` applies.

## Miscellaneous improvements

Small adjustments and polish were applied all over the place, ranging from a
redesigned main menu (with more descriptive names), to saving some state here
and there, such as the window position and size.

# Changes under the hood

There has been a lot of changes under the hood, refactoring, code cleanup, and
new interfaces existing and future plugins can hook into. One of these are the
settings framework that makes it possible to lift out bits and pieces of the
current state of Chrysalis, and save them, or load them. This way any plugin can
register `load`/`save` handlers, and thus save their settings to the same place
the rest of the settings are saved.

A few other things were also rearranged, and the architecture is shaping up
nicely: a lot of small, optional interfaces one can use, with very little
mandatory boilerplate.

# What's next?

You can follow development on [GitHub][chrysalis], we even have
a [project set up][gh:cs:project] to track the tasks and issues remaining until
the first stable release. I'm also posting updates
on [Mastodon][mastodon:chrysalis] and on [Patreon][patreon] too.

 [chrysalis]: https://github.com/algernon/Chrysalis
 [gh:cs:project]: https://github.com/algernon/Chrysalis/projects/1?fullscreen=true
 [mastodon:chrysalis]: https://trunk.mad-scientist.club/tags/chrysalis
 [patreon]: https://www.patreon.com/algernon
 
The next few weeks will be slower than usual, due to still packing out, and
because the next step is laying down the foundation of the layout editor. That
is a big task, which requires a lot more thinking than coding. I like to let
ideas settle first before going out to experiment with them, and this case is no
different.

Nevertheless, at the same time, there are many opportunities to polish the
existing features, fix some known issues, and so on.
 
# Thanks

I want to thank all my [patrons][patreon], and all of the people on Twitter and
elsewhere, who are following the Chrysalis development along. Their support is a
continuing inspiration for my Chrysalis work (I'm still not an UI person, this
stuff is way out of my area of expertise).
