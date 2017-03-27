---
title: "On-the-fly key remapping: A step-by-step guide"
date: 2017-03-27 15:00
tags: [Hacking, Keyboard, Patreon]
---

In the span of two weeks, we made a few big leaps forward with
the [Kaleidoscope][kaleidoscope] firmware, and it is now possible to remap keys
on the keyboard without using any software on the host. We can just do it
on-the-fly using nothing else than the keyboard. While this is pretty awesome,
it's not the most convenient thing, and has its share of limitations. Yet, it
has its uses. Among others, it is one of the best examples to show how all the
small pieces fit together, and can be used to build something neat, with very
little effort.

So today, we will walk through the implementation of this feature, and the
building blocks that were required to make it possible.

 [kaleidoscope]: https://github.com/keyboardio/Kaleidoscope
 
<!-- more -->

Before we dive any deeper, we need to understand how the keymap is stored first,
and what it actually is. [Kaleidoscope][kaleidoscope] uses a map to describe
what each key on the keyboard does: each position is assigned a code, and the
firmware does some action based on that code. We use 16 bits per key for this.
That may seem a lot, who needs over 64 thousand different keycodes anyway? But
it really isn't all that much. A good number of codes are flags, that augment
other keys: for example, a keycode that means `Ctrl+Alt+Del` is the `Del` key
with two flag bits, one for `Ctrl`, and one for `Alt`.

 [kaleidoscope]: https://github.com/keyboardio/Kaleidoscope

Traditionally, we stored the keymap in the read-only program memory of the
`MCU`, because at 16 bits per key, it is a sizable structure, and we did not
want to waste so much SRAM for something that will rarely change. But storing it
in program memory has its share of problems too, the biggest one being that it
can't be modified without re-flashing the firmware. That's an exercise that even
a novice user can do, but in practice, it would be preferable if they didn't
need to.

For this reason, we developed the [EEPROM-Keymap][plugin:eeprom-keymap] plugin,
which makes it possible to store the keymap in `EEPROM`. The `EEPROM` has the
advantage that it can be updated without re-flashing. Unfortunately, such an
update is still not trivial, so much so, that we ended up
using [Focus][plugin:focus] to present an interface to do so. This made it
possible to update the keymap without flashing, but one still needed a tool to
do so, and while there are plans to make a friendly GUI to assist us with this
(see [Chrysalis][chrysalis]), that is just not there yet.

 [plugin:eeprom-keymap]: https://github.com/keyboardio/Kaleidoscope-EEPROM-Keymap
 [plugin:focus]: https://github.com/keyboardio/Kaleidoscope-Focus
 [chrysalis]: https://github.com/algernon/Chrysalis
 
While I was happy with the status quo at this point (I have my hacky tools to
fiddle with a keymap stored in `EEPROM`), Jesse wasn't, and made an off-hand
remark on IRC that with these things already in place, on-the-fly remapping is
very close, too: just tap a `Remap` key, the key we want to remap, and another
we want to remap it to.

This sounded easy to do, and it turns out, it
was [as easy as described][plugin:eeprom-keymap-programmer], only a hundred
lines of code - even less, if we don't count the copyright blurbs.

 [plugin:eeprom-keymap-programmer]: https://github.com/keyboardio/Kaleidoscope-EEPROM-Keymap-Programmer
 
## So how does it all work?

Pretty darn simple, actually. The core idea is that we'll have the keymap in the
read-only program memory, and the `EEPROM` parts will act as an override only.
You'll see in a moment why this is important.

When the reprogram state is triggered (with a macro, a magic combo, or any other
way we can think of), the plugin will start capturing all keys pressed,
preventing normal handling of them. At first, it will wait for a target key to
be pressed, and when that one is released, it will wait for the source. It
remembers the position of both, so that when the source key is released, it will
look at the program memory-stored keymap, at the source position, and copy that
key over to the `EEPROM` override area. This way you can remap any key, on any
layer, to any other on the same layer, by copying from the source keymap to the
override. Tapping the same key twice, so that it becomes both source and target,
will also reset it to its original keycode, without any special handling.

And that's it! This is all our code needs to do, because everything else was
neatly in place already:

- We had a way to store a "built-in" keymap in program memory.
- We had a way to store an override in EEPROM.
- We could instruct the key lookup mechanism to look up in EEPROM first, and
  program memory after (if the key is transparent in EEPROM).
- We had a way to build a plugin that captures key events, stealing them before
  any other plugins get to jump on them.

## How do these fit together, again?

Lets start at the beginning: we make a macro, that puts the keymap into
remapping mode. This is as simple as calling
`EEPROMKeymapProgrammer.nextState()` from within the macro. When this gets
called, it activates the event handler hook, which will capture and steal the
next key (the target key), and remember its position. This is where event
handler hooks become important. Then, it waits for another key, and when that is
released too, it looks at the built-in keymap, and copies the key in source
position, to the target position in `EEPROM`, and then deactivates the event
handler hook.

When any key is pressed, the key lookup mechanism will call our custom
`EEPROMKeymap.getKeyOverride` function, which looks at `EEPROM` first, program
memory after. An extensible key lookup mechanism is of crucial importance here.
It would be very hard to implement this feature without this being possible.

Said lookup method builds upon both the built-in keymap, and the `EEPROM`-stored
one too. It is vital to have both available, and the two not being exclusive!
  
## Thanks

I want to thank all my [patrons][patreon], without whom this little post may
have never been written. Stay tuned for (slightly) more frequent updates about
new or interesting developments in [Kaleidoscope][kaleidoscope].

 [patreon]: https://www.patreon.com/algernon
