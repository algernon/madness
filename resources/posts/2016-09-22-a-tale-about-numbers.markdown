---
title: "A tale about numbers"
date: 2016-09-22 19:00
tags: [Hacking, Keyboard, ErgoDox, keyboardio, Ergonomics]
---

Today I will write more about layouts and the [keyboard.io][kbdio] than the
ErgoDox, though most of the testing has been made on it. This time, I took a big
step: having procrastinated on it for a very long time, I rearranged the number
row, along with the symbols. To make the transition easier - for some values of
easy - I started to use the same number/symbol layout on my base layer too. What
can I say? It's painful. I have used the same layout ever since I first laid my
hands on a keyboard in the early 1980's (except for one minor change, `"`
changing to `@` somewhen in the nineties):

 [kbdio]: https://shop.keyboard.io/

<pre>
! @ # $ % ^ & * ( )
1 2 3 4 5 6 7 8 9 0
</pre>

What I have now has only one thing in common: the `@` symbol. The bulk of this
post will attempt to explain what layout I came up with, and how.

<!-- more -->

<a id="adore"></a>
## ADORE

Before we get to the number row, lets refresh our memory, and have a look at how
the ADORE layout looked last time:

 ![ADORE on day 141](/assets/asylum/images/posts/ergodox-day-141/adore.png)

I'm not showing the heatmap, because there was an embarrassing oversight in the
keylogger: it also logged the arrow layer too, suggesting way more use of the
right side than it really was. With that in mind, and based on the feel of
typing, I made one minor modification: swapped `G` and `H`. This made some of
the digraphs easier, and so far, I find it easier to remember this variant, too.

The new layout is below:

 ![ADORE now](/assets/asylum/images/posts/a-tale-about-numbers/adore.png)

And as you can see, the top row is nothing like the traditional one.

<a id="number-row"></a>
## The number & symbol row

The motivation for the change came from the realization that there is a huge
dissonance between which numbers, and which symbols I use. To verify this
assumption, I wrote a small program that can turn any text into a keylog, which
I can then make a heatmap from. With the help of this tool, I started to map the
most used symbols, based on some C and Python code, and all the posts on this
blog. The results were surprising: `*` won by a large margin, followed by `#`
and `!`, then `%`, `&` and the rest. Since I do not use the brackets normally on
`9` and `0`, my symbol row has the pinkies free, for sure. All that remained was
to arrange the rest. I started with looking at [Programmer Dvorak][pdvr], but
quickly found out it won't work: too many symbols on the top row, which I have
elsewhere.

 [pdvr]: http://www.kaufmann.no/roland/dvorak/

I spent a long afternoon thinking and trying all kinds of symbol layouts. In the
end, the two most used ones went to the middle, because that's the longest
finger, and thus `*` and `#` were placed on opposite sides. Since `*` was
usually followed by a space, it went to the left side for hand alteration
purposes. The two keys beside the middle are also reasonably easy to reach, so
it did not take long to place `@` and `&` to the outer neighbours, and `^` and
`!` on the inner ones. These were put there mostly based on feeling, just like
`$` and `%` on the innermost keys.

With the symbols out of the way, it was time to rearrange the numbers. I
couldn't use the QWERTY numbers, and rearrange the numbers into the same order I
rearranged the symbols: `92864 51370` makes no sense. So I took another look at
[Programmer Dvorak][pdvr], and that gave me an idea: odds on the left, even
numbers on the right! But to aid in remembering which number is where - and
because I use it the least - I moved `9` to the pinky. This way the order is
descending on the left, and ascending on the right - easy to remember!

For the sake of symmetry, `F11` and `F12` were moved to the innermost keys on
each side.

### Getting used to the rearranged layout

I have been using the same top row arrangement all my life, erasing and
retraining three decades of muscle memory is **hard**. Even harder than
switching to Dvorak was, because I had prior attempts at that, the ADORE top row
is completely new. There are some keys I'm used to already: `1`, `3`, `5`, `0`,
`2`; and `@`, `!`, `#`, `$`. But others, like the rest of the numbers, or `^`,
`%` and `&` - those are a problem.

But so far, it's not bad, I like the feel of the layout, even if it takes some
time to get used to it.

<a id="keyboard.io"></a>
## keyboard.io

Originally, my plan was to start with the easy things: an ADORE layout on the
[keyboard.io firmware][kbdio:fw]. But at the time of this writing, it is not
easy to add new layouts, without touching other parts of the code. So instead, I
went out and wrote a [tap-dance library][atd], a fairly generic one, which
should be usable by any arduino-based keyboard - at least in theory, because I
have no way to test it on real hardware yet. There is a test suite however, that
attempts to simulate the various cases tap-dance needs to function under.

 [kbdio:fw]: https://github.com/keyboardio/KeyboardioFirmware
 [atd]: https://github.com/algernon/arduino-kbd-tap-dance
 
For now, I do not plan to work more on neither the tap-dance library, nor on
integrating ADORE into the firmware: I read through all of it, and it needs to
sink in first. Once it did, there will be lots to do! For example, one-shot
layers and mods could be implemented on top of tap-dance.

# Parting thoughts

This concludes the update, and like last time, we finish with a heatmap of the
layout, collected while writing this post:

 ![ADORE heatmap](/assets/asylum/images/posts/a-tale-about-numbers/adore-heatmap.png)
 
I ran my text-to-keylog tool on this post, and if I entered it as-is, without
corrections or editor assistance, the heatmap would have looked like this:

 ![ADORE heatmap](/assets/asylum/images/posts/a-tale-about-numbers/adore-heatmap-2.png)

In the next update, I will talk about the progress of learning the new
number/symbol layout, and as usual, about the changes made to ADORE, based on
the heatmap above, and my feelings while writing this post.
