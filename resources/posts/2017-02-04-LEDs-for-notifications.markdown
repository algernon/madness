---
title: "Want to use per-key LEDs for notification? Think again."
date: 2017-02-04 22:20
tags: [Keyboard, rants]
---

A recent [conversation on twitter][twt:leds], and the mention of the Das 5Q
reminded me how different people are. A lot of people seem to be on the opinion
that per-keyboard LEDs are so very useful for notifications, which is something
I very strongly disagree with. Back when the 5Q was announced (and I'm not going
to link to it, because it is easily one of the silliest ideas I've seen in
recent years), I was baffled by their focus on using the LEDs for notifications
from the host.

Granted, the idea sounds neat, at first, until you start to think about the
details. In this short post, I'll go into details why I think such use of the
per-key LEDs are nothing more than useless gimmick; but will also offer a few
reasons why - and under what circumstances - the LEDs themselves are useful
nevertheless.

 [twt:leds]: https://twitter.com/ArleyM/status/826478074975903745

<!-- more -->

One of the most important things about useful notifications is that they should
be both easy to notice, and convey useful information. Using per-key LEDs fails
both of these goals. Why? For a number of reasons!

## General problems with notifications

I do not like notifications, in general. Most of the time, they are a
distraction. Unless it is my job, I do not want to get notified about stock
prices, or people tweeting, or anything of that sort. I have applications for
Twitter, and pretty much everything else, that can collect notifications, and
**I** can choose when to look at them. Pushing them in my face is something I
turn off as soon as possible.

There are some things I want to get notified about, like being mentioned in a
work-related chat room, or my laptop battery draining, or my wife messaging me.
These are important things, but they happen infrequently. Yet, when they do
happen, I do not want to miss them. Since my eyes are on the screen while at a
computer, notifications are best displayed there. Using the screen also allows
the transfer of much more information: it can tell me the channel and the
message, the remaining time in the battery, or what my wife wrote me. The
keyboard can't do that.

Having too many notifications only leads to information overload, introduces way
too many distractions, which are counter-productive. Whether we work, game, or
relax, interruptions are not something we are looking forward to. Notifications
are inherently interruptions, so the less, the better, and when they happen,
they better be informative.

Flashing colors is not informative.

## Problems for touch typists

### While using the keyboard

When using the keyboard, people who touch-type, rarely look at the device. As
such, LEDs aren't always obvious, and in most cases, one would have to pay
special attention to notice and interpret them, because they only see the colors
from the corner of their eyes. It is a considerable effort to locate the source,
and then to interpret it. On the other hand, a desktop notification could pop up
in a corner: a bit more obtrusive than on the keyboard, but still within limits.
It is easier to notice, easier to interpret, and can convey information in a
much more useful way, too. It can also tell you more: while a LED can tell you
that your favourite celebrity tweeted, or that a certain stock rose, a desktop
notification can show you (part of) the tweet, or the current price of the
stock, too. Per-key LEDs can't do either, not in a straightforward way. You can
color-code the price of the stock, but that's extra effort to interpret. And you
can't color-code the tweet contents. You could play morse, but... lets not go
there, shall we?

Another factor that makes per-key LEDs less useful for this kind of thing, is
that while touch typing, your hands will obscure the keys. They will be in the
way, and may even fully cover a key or two, which makes it all the harder to
notice LEDs changing color.

### While away from the keyboard

There are cases when the keyboard is not obstructed by hands lingering over the
keys, and when one has a reasonable chance to look at the keyboard: when idle,
and possibly not at the desk. From a distance. Coming back from a coffee break,
or things like that: whenever you have your lock screen up.

In these cases, desktop - or lock screen - notifications are still a much better
option. While both can be easily seen in this case, the screen has a much higher
resolution, and can show you a lot more information, in a superior presentation.
It can not only show you the stock, but can show you its recent history on a
graph, so you see the trend. It can show you not only a tweet, but a whole
conversation.

Not to mention, that the screen is usually higher, closer to eye level, so it is
still easier to see things there.

## Problems for non-touch typists

People who can't touch type, use only a few fingers, or look at the keyboard
often will have a much easier way to see notifications there. But, the problem
with information bandwidth remains: they still have to remember what the color
under that certain key means, while they could just glance at an icon, or a
short text in a desktop notification, and know a lot more, a lot easier.

## Problems with split keyboards

When you are using a split keyboard, and you tilt or tent it, it becomes even
more effort to locate where a certain color is on the keyboard. With a high
tenting angle, the outer keys aren't in our viewport as much as the inner ones.
The keyboard halves may also be much further to the side than with a traditional
keyboard, making it even harder to pay attention to per-key LEDs.

## When keyboard notifications are still useful

However, not all kinds of notifications are bad: some are even useful on the
keyboard!

### Feedback

A colorful feedback to a keyboard-triggered action, for example, can
be useful. Say, you told your IDE to compile a project and run the test suite.
The keyboard can light up in green for a few seconds if the tests succeed, or in
red if they fail. This is useful, because you get feedback, and a solid color
across the keyboard is something you'd notice easier than a single key being
colored.

Similarly, if you use a modal editor, and hide the mode-line because you want as
few distractions as possible, then color-coding the mode you are in
(orange/yellow for command-mode; green for insert mode; magenta for paste mode;
white for visual mode; etc), and having the keyboard backlight set to that color
is of use: even if we don't look at the keyboard, we can still notice the color
from the corner of our eye. We don't need to know which keys are lit - all of
them are. We only have to remember a very small set of colors, too.

Another case for per-key LEDs, one where we don't light up the whole keyboard
with the same color is when we use one-shot keys. With one-shot keys (usually
modifiers and layer keys), when you press and release them, their effect remains
active until you press and release another key, or until a timeout. As a way to
see which modifiers are active, lighting up the active ones can be useful. It
helps us if we tap a modifier accidentally. It helps to see when they time out.
It's a way to see where we are in a sequence.

The common thing between all of these uses is that these are all reaction, they
are all feedback to what we were doing with the keyboard. They are not
notifications, they are feedback, and that's an important distinction.

### Other uses for per-key LEDs

When using layers extensively, coloring the changed keys can also be useful: if
one comes back at the keyboard from a break (or an interruption), coloring the
layout gives an easy way to continue from where we were. It is also a great aid
in learning to use layers.

Another use is for pure showing off: things like a blazing trail effect, or a
rainbow swiping through the keys - they are fun little effects one can use to
show off what the keyboard can do. They are useful for testing too, perhaps. But
not really for everyday use. They have their place, but they are not all that
useful for notification purposes, you see.
