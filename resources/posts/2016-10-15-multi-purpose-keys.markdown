---
title: "Multi-purpose keys, and a case for small keyboards"
date: 2016-10-15 12:15
tags: [Hacking, Keyboard, ErgoDox, keyboardio, Ergonomics]
---

The layout [I use][ez:layout], and the one [I'm aiming for][kbdio:layout] are
rather unconventional in a few respects. One of these is that I make heavy use
of various multi-purpose keys: one-shot modifiers, tap-dance-, and leader keys.
I have read, and continue reading a lot of keyboard and layout-related material.
Blog posts, researches, theorycrafting about the most efficient layout, and a
lot more. And reading most, I often feel frustrated, because almost all of them
are written with the same old keyboard design and behaviour in mind. Even those
that do mention ergonomic keyboards, rarely manage to think outside the box, and
assume (or just play with the idea of!) unconventional behaviour.

 [ez:layout]: https://github.com/algernon/ergodox-layout#base-layer
 [kbdio:layout]: http://www.keyboard-layout-editor.com/#/gists/da05641b419790a7a4c1297c4e58ec9f

While I wrote about my keyboard journey numerous times, there was no single
place where I summarize what I ended up with, and why. This changes now.

I will not be talking about the alphanumeric layout, because that is a well
researched topic, and I don't wish to add even more noise. My focus - as the
title suggests - will be on multi-purpose keys, modifiers included.

 ![M01 layout](/assets/asylum/images/posts/multi-purpose-keys/m01-layout.png)

Above you can see the layout I'm aiming for. Look at it, try to ignore the
alphanumeric layout, and concentrate on the other keys! Take your time, and try
to imagine how the layout could be used! I'll wait.

Still here? What did you find?

<!-- more -->

------------------------------------------------------------------------

Unlike on most, traditional keyboards, I only have one of each modifier, and
none of them are on the pinky. Even worse, they are on the inner side of the
keyboard! "*How do you press `Alt+X`?*", "*Don't you have to do weird gymnastics
with most modifiers on one side?*", "*How do you type capital letters, with
`Shift` on the thumb cluster?*", "*What's with `[{(` / `)}]`? Three symbols on
the same key?*".

These are questions rightfully asked.

## The most obvious: modifiers

Before I explain how my modifiers work, I need to explain why.

Traditionally, keyboards have `Shift` and `Control` on the pinky on each side,
and `Alt` around the `Space` bar. This is so one can chord them with other keys:
press and hold a modifier on one side, tap a key on the other, and release the
modifier. Having these keys on both sides makes it possible to press pretty much
any combination without having to do move hands, or do strange gymnastics. But
this layout of modifiers also presents a problem: putting a lot of work -
because holding a key is significant work - on the weakest digit. This can
easily cause issues such as the
[Emacs pinky](https://en.wikipedia.org/wiki/Emacs#Emacs_pinky).

There are numerous ways to help the situation, including drastically reducing
the amount of shortcuts that need modifiers. I do this by using
[Spacemacs](http://spacemacs.org/), where most of the shortcuts start with
`Space`, and are key sequences, rather than chords.

But this alone - I found - is not enough. There are situations when one does
need to press `Control`, and there is even more reason to need `Shift`! Some
people use `Caps Lock` instead of `Shift`, because that stays active until
tapped again, thereby reducing the work the pinky has to do. Why? Because
tapping twice takes less work than holding.

On keyboards with a thumb cluster, some of the modifiers are often moved there,
as the thumb is a much stronger digit. Unfortunately, this just delays the
inevitable: the root issue is not that the pinky is weak, but that chording puts
too much work on any finger.

We should get rid of chording then!

Sadly, there are only a handful of programs that handle key sequences in a
reasonable way, and even then, doing this on the software side is often
unreliable, and induces delays.

Why not do it on the keyboard then? What if we had modifiers that activated only
for a single keypress? That way, we would not need to change anything on the OS
side, and would still be able to use sequences instead of chords!

These are called **one-shot** modifiers: tap them, and they become active until
either another key is pressed (in which case the modifier unregisters after the
modified key), or until a timeout. For those situations when one wants them
active for more than one key, they can tapped twice to make them sticky. Being
sticky, they remain active until tapped a third time.

All of my modifiers work this way, and it is incredibly nice: I only need one of
each, thus saving three keys; I can put them to positions that are easy to
reach, without having to worry about being able to chord them with other keys; I
can still type fast and fluently, because I use less muscles to achieve the same
things.

Most layouts I have seen, most research I have read did not even consider this
possibility. Even on a traditional keyboard, with the stock QWERTY layout,
one-shot modifiers would still be an improvement. On ergonomic keyboards, even
more so.

### Use cases for one-shot keys

* **One-shot modifiers**: Tap a modifier, release it, tap another key, and have
  the modifier applied, and the modifier deactivated after. This allows one to
  move these keys to a more convenient place, which would otherwise be obscure
  to use with chording. On boards with thumb keys, this allows one to have
  modifiers on the thumb cluster.

* **One-shot layers**: For symbols that one typically uses once, and then leaves
  the layer, one-shot layers are tremendously useful, because one does not have
  to hold the layer key. Less chording, more sequencing!
  
* **One-shot combos**: A combination of the above two, for cases where one wants
  to temporarily switch to a layer, and have a modifier applied. For example,
  having function keys on a different layer, and wishing to tap, say, `Alt+F2`,
  one would need to hold both `Alt` and the layer key. With one-shot combos, one
  can have a key that presses `Alt`, and switches layers - until after the next
  keypress!
  
### One-shot FAQ

* **How do you know which one-shot modifier or layer is active?** Easily: I use
  the LEDs on the keyboard, to signal which modifier is active. For layers, I
  use two LEDs, and set them to a brighter color, so I can see whether it is
  indicating a layer or a modifier.

## The less obvious: tap-dance keys

Likely the next thing one would notice is that I have keys with more than two
symbols on them, but the layout lacks `AltGr` or similar. The most visible
example of this are the `[{(` / `)}]` keys. I wanted to have all the bracket
keys on the main layer, as I use them frequently, and temporary layers for this
purpose proved to be much less convenient than desired. Thus, I needed a way to
input symbols without pressing another key: why not do different actions based
on the amount of times a key has been tapped?

If we were able to do this, it would become possible to replace a modifier and
key sequence with a double tap. While it is true that in the latter case one
must first wait for the key to bounce back up, it also means no finger has to
move!

Instead of `Shift+9` or `Shift-0`, I tap `[` / `]` twice. Only one finger used,
less movement, less stretching, and all the brackets are on one key. Sounds like
a win to me! No layer to switch, no key to hold or have on one-shot, just one
finger is all it takes.

### Use cases for tap-dance keys

* **Fake layering**: The situation described above: having one key to three
  things, to reduce movement, and to have similar symbols on the same key.

* **Avoiding modifiers**: Somewhat similar to the previous example, but
  different in subtle ways. The case of my `:` key is the perfect demonstration:
  it is on the thumb cluster, because it is used often while programming, but so
  is `Shift`, and on the same side, too! Moving the thumb is slow, tapping the
  same key twice with it, less so.
  
* **Modifier/symbol dual-use**: A single key should act as a modifier when held,
  but as a regular key when tapped. When tapped twice and then held, it should
  keep the regular key registered until release. This makes it possible to have
  a - say - `Ctrl/Z` key, with a way to allow `Z` to repeat!
  
## Behind the scenes: leader key

Leader keys are less obvious on the keymap above, and their use even less so,
please allow me to briefly describe what they do!

These keys allow one to bind actions not to a key, or a chord, or a simple
sequence of modifiers and a key, but to any key sequence! One can even reuse the
same key multiple times in the same sequence. For single-letter sequences one
could use one-shot layers and achieve the same results, but for anything more
complicated, the layer-based solution is no longer an option.

The way Leader keys work, is that the moment you hit one, all keys on the board
are remembered instead of acting on them, and with each key pressed, we try to
look up the sequence entered so far in a dictionary. If we find a non-ambiguous
match, instead of the keys input so far, we execute the action bound to the
sequence.

### Use cases for a leader key

* **Mnemonic shortcuts**: Ever wanted to switch layers by typing its name? Now
  you can. `LEAD l dvorak` and voila! Want to reprogram the keyboard? `LEAD
  reflash`. Want to input symbols that otherwise require a complex sequence,
  like `¯\_(ツ)_/¯`? No problem, `LEAD s shrug`, or even `LEAD s s` for short.

# The case for small keyboards

A lot of people seem to believe that if you want to have a lot of functionality
on your keyboard, you need to have many keys to put macros and the like on. As
you may have guessed by now, I don't subscribe to that point of view. When using
a keyboard, I prefer if I don't have to move my hands all that much, but the
more keys, the bigger challenge to place them well enough for easy access. If I
have to move my hands from their resting position, I try to find a way around
that instead.

Because of this preference, instead of quantity, I look for quality: instead of
having a dozen programmable macro keys, on a huge board, I'll take a smaller
board with all keys fully programmable. I'll use the device smarter, rather than
just having more keys.

With a smaller keyboard one is forced to think about the layout, and about the
behaviour of it, in general. It makes one more conscious about the choices made,
about the amount of intelligence one can put into a device. It makes one look
for ways to improve their equipment, and perhaps even to consider such things as
multi-use keys.
