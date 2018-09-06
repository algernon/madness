---
title: "The Brutalist Path"
date: 2018-09-06 14:30
tags: [Emacs, Ergonomics]
---

I have used plenty of Emacs themes in the past, but in the end - like many
others - I ended up creating my own. What I have now is *very* different than
what I used even a year ago, let alone even before. It differs not just in
colors, tone, or being light or dark - it differs fundamentally, in its goals.
This theme is not for everybody, and I'm not going to try and convince You, my
dear reader, that it is good for anyone but me. What I want to do today is
explore the path I took, and how I ended up writing my own
[brutalist-theme][theme:brutalist].

[![Emacs + brutalist-theme][theme:brutalist:thumb]][theme:brutalist:full]

 [theme:brutalist]: https://git.madhouse-project.org/algernon/brutalist-theme.el#repo-readme
 [theme:brutalist:thumb]: /assets/asylum/images/posts/the-brutalist-path/emacs-brutalist.thumb.png
 [theme:brutalist:full]: /assets/asylum/images/posts/the-brutalist-path/emacs-brutalist.png

<!-- more -->

I will not be exploring the themes I used before switching to
[Spacemacs][spacemacs], because not only would I have to dig up my old Emacs
configuration for that, it would also take a very, very long time. Eigtheen
years of Emacs use has seen some themes, restricting myself to the last three
years is plenty enough.

 [spacemacs]: http://spacemacs.org/

When I switched to Spacemacs, I initially used the [`Material`][theme:material]
theme, a dark theme with plenty of colors, mostly borrowed from Google's
Material Design guidelines.

[![Emacs + material-theme][theme:material:thumb]][theme:material:full]

 [theme:material]: https://github.com/cpaulik/emacs-material-theme/tree/master
 [theme:material:thumb]: /assets/asylum/images/posts/the-brutalist-path/emacs-material.thumb.png
 [theme:material:full]: /assets/asylum/images/posts/the-brutalist-path/emacs-material.png

I used this theme for a bit over half a year, when I switched to
[`darkokai`][theme:darkokai], still a dark theme, with somewhat stronger colors.

[![Emacs + darkokai-theme][theme:darkokai:thumb]][theme:darkokai:full]

 [theme:darkokai]: https://github.com/sjrmanning/darkokai/tree/master
 [theme:darkokai:thumb]: /assets/asylum/images/posts/the-brutalist-path/emacs-darkokai.thumb.png
 [theme:darkokai:full]: /assets/asylum/images/posts/the-brutalist-path/emacs-darkokai.png

It did not last long, I switched to the [`misterioso`][theme:misterioso] theme
in just a month. While still a dark theme, it felt a bit lighter, less hard on
the eyes.

[![Emacs + misterioso-theme][theme:misterioso:thumb]][theme:misterioso:full]

 [theme:misterioso]: https://github.com/emacs-mirror/emacs/blob/master/etc/themes/misterioso-theme.el
 [theme:misterioso:thumb]: /assets/asylum/images/posts/the-brutalist-path/emacs-misterioso.thumb.png
 [theme:misterioso:full]: /assets/asylum/images/posts/the-brutalist-path/emacs-misterioso.png

...this did not last long, I switched to
[`solarized-dark`][theme:solarized-dark] within a week. At that point, I was a
big fan of the Solarized palette, and used it for my terminal too.

[![Emacs + solarized-dark-theme][theme:solarized-dark:thumb]][theme:solarized-dark:full]

 [theme:solarized-dark]: https://github.com/bbatsov/solarized-emacs/tree/master
 [theme:solarized-dark:thumb]: /assets/asylum/images/posts/the-brutalist-path/emacs-solarized-dark.thumb.png
 [theme:solarized-dark:full]: /assets/asylum/images/posts/the-brutalist-path/emacs-solarized-dark.png

It has been in use for about a year, when I switched to
[`github-modern`][theme:github-modern], my first foray into light themes. I have
looked at light themes before, but none of them really stuck, some of the colors
were always unreadable, or looked just plain wrong. But `github-modern` was
something I liked, possibly because when I wasn't spending time in Emacs, I was
mostly looking at GitHub, so the colors were familiar. Around this time, I
started to use two monitors, with Emacs on the primary one, a browser (usually
with GitHub shown) on the other. The contrast of the dark editor and the light
browser were annoying - which is why I went looking for a light theme in the
first place.

[![Emacs + github-modern-theme][theme:github-modern:thumb]][theme:github-modern:full]

 [theme:github-modern]: https://github.com/philiparvidsson/GitHub-Modern-Theme-For-Emacs/tree/master
 [theme:github-modern:thumb]: /assets/asylum/images/posts/the-brutalist-path/emacs-github-modern.thumb.png
 [theme:github-modern:full]: /assets/asylum/images/posts/the-brutalist-path/emacs-github-modern.png

This is where things start to get interesting. This was in 2017, I already had
an ergonomic keyboard, and was in the process of transforming my environment to
feel better, to really serve me. I started to make conscious decisions about
things I largely ignored before. One of my goals at this time was to reduce
distractions. For many years, I've been using full-screen windows, to be able to
focus on one thing only at a time. I still had a number of applications that
popped up notifications for me, but by 2017, I disabled those notifications, and
banished these applications to a second screen. This way I didn't get
interrupted, I didn't need to switch my focused application, but could still
look at these things whenever I wanted to. I found that looking at the second
screen was much less effort, and a lot less distracting than either
notifications or switching applications on my main screen.

There was, however, one notable exception where I did not make an effort to
reduce distraction: my editor. I've read a number of articles about syntax
highlighting (or rather, the lack of thereof), such as "[A case against syntax
highlighting][blog:case-against-syntax-highlighting]". I've had colleagues who
did not use syntax highlighting at all, too. It was an intriguing concept, I
tried it myself too a few times, but always came back to my colors.

 [blog:case-against-syntax-highlighting]: http://www.linusakesson.net/programming/syntaxhighlighting/

This all changed this may, when I started experimenting with the [`eink
theme`][theme:eink]. It was an eye opener: I could use syntax highlighting, but
without the distraction!

[![Emacs + eink-theme][theme:eink:thumb]][theme:eink:full]

 [theme:eink]: https://github.com/maio/eink-emacs/tree/master
 [theme:eink:thumb]: /assets/asylum/images/posts/the-brutalist-path/emacs-eink.thumb.png
 [theme:eink:full]: /assets/asylum/images/posts/the-brutalist-path/emacs-eink.png

You see, unlike Linus Åkesson (the author of the article above) or [Rob
Pike][pike:go-syntax], I like some *gentle* syntax highlighting, much like
[Robert Melton][blog:syntax-off] among other people. I believe that highlighting
matching parentheses, the current line, or the selected region, or search hits
all add useful information. Additionally, there are parts of source code that I
think deserve to be presented differently than normal text. Fortunately, there's
a solution that often does not involve colors at all! There's weight,
underlining, slant, inverse colors - they can all be used to apply some minimal,
not too distracting visual aids.

 [pike:go-syntax]: https://groups.google.com/d/msg/golang-nuts/hJHCAaiL0so/WLKD3zcVvfoJ
 [blog:syntax-off]: https://www.robertmelton.com/posts/syntax-highlighting-off/

I do not agree with turning syntax highlighting completely off being a useful
thing one should do. Quite the contrary. Belittling visual aids is a mistake,
akin to throwing away utensils because you can just eat with your hand. You can
do that, but most people will not, because utensils are useful. Use simple tools
that get the job done, and do no more, but do use tools that make your life
better. You don't need fancy spoons with elaborate decoration, or even jewels
embedded. But eating soup with a spoon is a lot easier than doing so without
(unless you can drink it, of course, but not all soups are like that).

On the other hand, over the years I learned that distracting colors are a
bother, something I do not really need. Therefore, a minimal theme that
highlights things I want highlighted, in an unobtrusive manner, possibly with
markup other than colors is something I strive for.

This is the aim of my [`brutalist theme`][theme:brutalist]: not the banishment
of color, because it still uses colors for some select things (such as search
results, links, strings, selection, and a number of others); not a banishment of
syntax highlighting, because I believe it is useful; not a strive for minimalism
either. It is none of those things. It may have a thing or two common with those
goals, but that is purely coincidence.

[![Emacs + brutalist-theme][theme:brutalist:thumb]][theme:brutalist:full]

 [theme:brutalist]: https://git.madhouse-project.org/algernon/brutalist-theme.el#repo-readme
 [theme:brutalist:thumb]: /assets/asylum/images/posts/the-brutalist-path/emacs-brutalist.thumb.png
 [theme:brutalist:full]: /assets/asylum/images/posts/the-brutalist-path/emacs-brutalist.png

The goal of my theme is to be as free of distraction as possible, while still
providing hints where I feel they are necessary. It is supposed to expose the
underlying context, to guide me to the content, but stay out of my way
otherwise. It is brutally pragmatic, and in many ways, focuses on raw content
true to its construction. I try to remove color as much as possible, and prefer
other forms of distinction instead. Sometimes I do have to fall back to using
colors, and that is okay. It is done with care, and plenty of experimenting
before committing to a particular face.

I do not want to get rid of syntax highlighting, nor do I preach minimalism. The
theme is merely a pragmatic one that happens to borrow ideas from a lot of
places - but that's only that. It does not strive for either, nor imitate. It
borrows, as long as that serves the purpose of not being needlessly distracting.

Combined with [`writeroom-mode`][emacs:writeroom-mode], we get a solid,
distraction-free environment. Mind you, to be truly productive with this setup,
one needs to be familiar with their editor, because it provides only the
essential hints. But that's the beauty of it: once you know each other well,
there is little reason for the fancy facade.

 [emacs:writeroom-mode]: https://github.com/joostkremers/writeroom-mode/tree/master

In closing, the evolution of my Emacs themes looks somewhat like this:

[![Emacs theme montage][theme:montage:thumb]][theme:montage:full]

 [theme:montage:thumb]: /assets/asylum/images/posts/the-brutalist-path/emacs-theme-montage.thumb.png
 [theme:montage:full]: /assets/asylum/images/posts/the-brutalist-path/emacs-theme-montage.png

All of these pictures were taken with my current Emacs setup. At the time of
using each theme, my setup was different: I had a modeline, or line numbers, and
so on. In this respect, the images lack a bit of context. But the evolution is
interesting nevertheless.
