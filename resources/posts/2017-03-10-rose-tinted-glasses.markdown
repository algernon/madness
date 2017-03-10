---
title: "Rose Tinted Glasses"
date: 2017-03-10 07:30
tags: [Hacking, Keyboard, keyboardio]
---

You know that feeling when everything seems to fall into place? When you finally
reach the top of the hill, and look around? When everything you worked towards
bears fruit? When you lay back, and prepare to rest, to enjoy the hard work put
into your creation?

I thought I knew that feeling too. But then, on the top of the hill, I saw
another, higher, with grass greener than what I've ever seen before, and flowers
in wild colors, butterflies flapping their little wings among them. I wasn't
done yet.

Today, we are going to start climbing that higher mountain, and discover what
we've built so far, and what lies still ahead. Oh, and this is all about
keyboards.
 
<!-- more -->

A lot has happened since the [last progress report][blog:shortcutting], and this
time, I will not start the story where we left off last time. Rather, we'll
start at the beginning, in a way. We'll start with a table of contents, because
this is going to be a long post.

 [blog:shortcutting]: /blog/2017/02/15/Shortcutting-Kaleidoscope/

<a id="toc"></a>
# Table of contents

* [Kaleidoscope](#kaleidoscope)
    - [Plugins](#plugins)
        + [Colormap](#plugins/colormap)
        + [Cycle](#plugins/cycle)
        + [DualUse](#plugins/dualuse)
        + [GhostInTheFirmware](#plugins/ghostinthefirmware)
        + [Heatmap](#plugins/heatmap)
        + [HostOS](#plugins/hostos)
        + [IgnoraneIsBliss](#plugins/ignoranceisbliss)
        + [KeyLogger](#plugins/keylogger)
        + [LED-ActiveModColor](#plugins/led-activemodcolor)
        + [LED-AlphaSquare](#plugins/led-alphasquare)
        + [LED-Stalker](#plugins/led-stalker)
        + [LEDControl](#plugins/ledcontrol)
        + [LEDEffect-Breathe](#plugins/ledeffect-breathe)
        + [LEDEffect-Chase](#plugins/ledeffect-chase)
        + [LEDEffect-Rainbow](#plugins/ledeffect-rainbow)
        + [LEDEffect-SolidColor](#plugins/ledeffect-solidcolor)
        + [LEDEffecs](#plugins/ledeffects)
        + [Leader](#plugins/leader)
        + [Macros](#plugins/macros)
        + [MagicCombo](#plugins/magiccombo)
        + [MouseKeys](#plugins/mousekeys)
        + [MouseGears](#plugins/mousegears)
        + [Numlock](#plugins/numlock)
        + [OneShot](#plugins/oneshot)
        + [Escape-OneShot](#plugins/escape-oneshot)
        + [ShapeShifter](#plugins/shapeshifter)
        + [SpaceCadet](#plugins/spacecadet)
        + [Syster](#plugins/syster)
        + [TapDance](#plugins/tapdance)
        + [TopsyTurvy](#plugins/topsyturvy)
        + [Unicode](#plugins/unicode)
    - [Very experimental plugins](#very-experimental-plugins)
        + [Focus](#plugins/focus)
        + [EEPROM-Settings](#plugins/eeprom-settings)
        + [EEPROM-Keymap](#plugins/eeprom-keymap)
    - [Future plugin ideas](#future-plugin-ideas)
* [Chrysalis](#chrysalis)
* [Miscellaneous news](#misc-news)
* [Contributing](#contributing)
     - [Non-code contributions](#non-code-contributions)
     - [Keyboards for the Twins](#keyboards-for-the-twins)
* [What next?](#what-next-questionmark)

<a id="kaleidoscope"></a>
# Kaleidoscope

We've done some further tuning of the core Kaleidoscope firmware, and plugins
that are part of the default Sketch, and there are some more changes on the way.
From the outside, little changed, but under the hood, there are some very
noticeable improvements: the way LEDs are refreshed has changed, we only refresh
the LEDs if any of them changed. This is by far the single biggest win when it
comes to the speed of the scan cycle: it nearly halves the length of a cycle!

I'm also working on removing pre-allocated arrays, and transitioning to linked
lists instead. With the default Sketch, as things stand now, this is another
huge win: almost a quarter kilobyte of SRAM saved, and more than a dozen bytes
of code! However, this is not merged yet, due to a few bugs here and there that
I'm yet to investigate.

But these are under the hood, and while important, they are just a few small
rocks on our hill.

<a id="plugins"></a>
## Plugins

Plugins are where most of the work is being done - as it should be. A lean and
clean core makes it so much easier to extend it. Unlike the last few times, I
will now go through every single plugin available, and briefly describe what
they are for. I won't tell you how to use them, most of these have
documentation, aimed at showing just that.

So find below a comprehensive list of all the plugins Kaleidoscope has, at the
time of this writing! Brace yourselves, this will be a long ride.

<a id="plugins/colormap">
### [Colormap](https://github.com/keyboardio/Kaleidoscope-Colormap)

A simple plugin that lets you set the color of each LED, based on the active
layer. I use it to highlight keys that are active on the particular layer, that
have changed meaning. I then group them with colors, so mouse keys have the same
color, cursor keys another, and so on.

Nothing terribly interesting here.

---------------------------------------------------------------------------------

<a id="plugins/cycle">
### [Cycle](https://github.com/keyboardio/Kaleidoscope-Cycle)

Inspired by mobile phones of old, where you had to press a key repeatedly to get
to the next symbol, this plugin implements something similar. When you press the
`Cycle` key, it will look at the previously pressed key (okay, it doesn't, it
keeps track of the last key that is not itself), and then consult a dictionary,
to see which symbol to replace the previous with.

This allows one to cycle through a number of options by mashing a single key.

---------------------------------------------------------------------------------

<a id="plugins/dualuse">
### [DualUse](https://github.com/keyboardio/Kaleidoscope-DualUse)

Popularized by small keyboards, where every key is precious, dual-use keys allow
one to have two different functions on the key, depending on whether it is
pressed in isolation, or held together with others.

In practice, this means you can have a `Control/Z` key, which acts as a normal
`Z` key would, if you tap it in isolation; but it will act as `Control`, if you
hold it together with any other key.

This can be tremendously useful when you have problems placing your modifiers or
layer keys - because `DualUse` also supports momentary layer switching! -, and
want to reuse a key you would normally not press together with the modifier.

The [default keymap of the ErgoDox EZ](http://qmk.fm/keyboards/ergodox/keymaps/default/) is
a good example how these can be used.

---------------------------------------------------------------------------------

<a id="plugins/ghostinthefirmware">
### [GhostInTheFirmware](https://github.com/keyboardio/Kaleidoscope-GhostInTheFirmware)

Serves mostly demonstration purposes. It can press keys for you, as if a human
pressed them. Unlike [macros](#plugins/macros), this does not work by giving it
a list of key codes to report, but you give it a list of coordinates, and it
will press whatever keys are on those coordinates.

With this, you can show off LED effects that react to keys being pressed,
without your hand obstructing the view.

---------------------------------------------------------------------------------

<a id="plugins/heatmap">
### [Heatmap](https://github.com/keyboardio/Kaleidoscope-Heatmap)

A very, very experimental, and somewhat buggy heat map for your per-key LEDs. It
keeps track of all the keys you press, and draws a heatmap on your LEDs.

It eats a significant amount of memory, and the method of computing the colors
is far from being perfect - or even good. It's an idea I still want to play with
at some point, but for now, it's pretty much on hold.

May be a good candidate for someone else to pick up and polish, once the Model
01s start shipping!

---------------------------------------------------------------------------------

<a id="plugins/hostos">
### [HostOS](https://github.com/keyboardio/Kaleidoscope-HostOS)

A utility plugin, in desperate need of redesign. All it does is keep track - and
try to detect - the operating system on the host, so other plugins can use this
information to adjust their behaviour.

See the [Unicode](#plugins/unicode) plugin for an example.

---------------------------------------------------------------------------------

<a id="plugins/ignoranceisbliss">
### [IgnoranceIsBliss](https://github.com/keyboardio/Kaleidoscope-IgnoranceIsBliss)

There was a short time when my prototype had a few loose switches, and they kept
triggering. `IgnoranceIsBliss` was the temporary solution, as it ignores a set
of keys, so the rest of the stack will never see anything happening with those
keys.

It really is not a useful plugin. If you want to disable a key, just set it to
`Key_NoKey`. This plugin is only of use when you don't want to change your
keymap, yet, still want to disable a key or two. That should not happen, unless
you are heavily modding the keyboard.

---------------------------------------------------------------------------------

<a id="plugins/keylogger">
### [KeyLogger](https://github.com/keyboardio/Kaleidoscope-KeyLogger)

You are probably raising your eyebrows now. A key logger? In the keyboard? Are
you **mad**?

Why yes, I am. But the key logger still serves a higher purpose, and a good one
too: it allows me to post-process the logs and draw a heatmap from that, a
better, more informative one than what the [Heatmap](#plugins/heatmap) plugin is
able to provide. I collect the logs from the keyboard, because those logs are
far more reliable than collecting on the host side - I can catch dead keys, too!
And the logger logs coordinates, not key codes, so the log is pretty much layer,
and layout agnostic.

Do keep in mind that anyone who can read the keyboard's virtual serial port,
will be able to see every single key you press, or release. Do use it with care.

---------------------------------------------------------------------------------

<a id="plugins/led-activemodcolor">
### [LED-ActiveModColor](https://github.com/keyboardio/Kaleidoscope-LED-ActiveModColor)

Highlights active modifiers, most useful together with
the [OneShot](#plugins/oneshot) or [DualUse](#plugins/dualuse) plugins, because
otherwise, you'd just be holding modifiers, and wouldn't need a highlight.

---------------------------------------------------------------------------------

<a id="plugins/led-alphasquare">
### [LED-AlphaSquare](https://github.com/keyboardio/Kaleidoscope-LED-AlphaSquare)

The purpose of this plugin is two fold: first, it is 4x4 pixel alphabet, with
some utility functions to display the symbols on the keyboard LEDs. It can also
draw arbitrary 4x4 symbols, and has a few macros to make it easier to draw
these.

Second, it is a LED effect, that shows the symbol you just pressed for a short
while, on the side of the keyboard it was pressed at. So if you type "hello", it
will display those chars on the LEDs, using the 4x4 pixel font.

You may need to squint a little to recognise some of the letters, but hey. This
one's a fun little thing.

---------------------------------------------------------------------------------

<a id="plugins/led-stalker">
### [LED-Stalker](https://github.com/keyboardio/Kaleidoscope-LED-Stalker)

Imagine that as you type, a faint light haunts your fingers, follows them
everywhere they go, like a ghost! Or imagine a trail of blazing fire! Type fast,
or the fire will catch up and **burn** your fingers!

The `LED-Stalker` plugin will give you these experiences. Except for the burn.
It won't damage neither you, nor your keyboard, it was just poetic hyperbole.

---------------------------------------------------------------------------------

<a id="plugins/ledcontrol">
### [LEDControl](https://github.com/keyboardio/Kaleidoscope-LEDControl)

Another building-block for other plugins, the LEDControl plugin abstracts away a
few of the nuances that come with having to deal with LEDs.

One of the biggest wins in speeding up the firmware came from here: once we were
reliably measuring the speed of a cycle, it didn't take long to figure out that
updating LEDs take a long time. It takes a long time, because there's plenty of
data that has to travel through from the main MCU, to the ATTiny that controls
the LEDs.

So we did two things: first, we restricted how often the LED data is sent over,
so instead of sending every cycle, we send every 16ms (so about 60 refreshes /
second); and second, we only send the data when there is a change. If the data
did not change, we do not refresh. These two combined made the cycle length of
my own Sketch go from 14ms to about 7ms on average. That's a pretty sweet
speedup.

---------------------------------------------------------------------------------

<a id="plugins/ledeffect-breathe">
### [LEDEffect-Breathe](https://github.com/keyboardio/Kaleidoscope-LEDEffect-Breathe)

Your standard LED breathe effect, fade in, fade out, and repeat. Relaxing and
simple.

---------------------------------------------------------------------------------

<a id="plugins/ledeffect-chase">
### [LEDEffect-Chase](https://github.com/keyboardio/Kaleidoscope-LEDEffect-Chase)

Watch two dots chase each other across the keyboard. It is relaxing, one of my
wife's favourite LED effects.

---------------------------------------------------------------------------------

<a id="plugins/ledeffect-rainbow">
### [LEDEffect-Rainbow](https://github.com/keyboardio/Kaleidoscope-LEDEffect-Rainbow)

As the name may suggest, provides rainbow-like effects for the keyboard. They
look nice, and are a great way to show off the RGB capabilities of the keyboard.

---------------------------------------------------------------------------------

<a id="plugins/ledeffect-SolidColor">
### [LEDEffect-SolidColor](https://github.com/keyboardio/Kaleidoscope-LEDEffect-SolidColor)

Sets all the LEDs to a single color. That's about it. Small, dumb, sweet.

---------------------------------------------------------------------------------

<a id="plugins/ledeffects">
### [LEDEffects](https://github.com/keyboardio/Kaleidoscope-LEDEffects)

A collection of LED themes, currently a Miami and a Jukebox-inspired theme is
available. See the documentation!

---------------------------------------------------------------------------------

<a id="plugins/leader">
### [Leader](https://github.com/keyboardio/Kaleidoscope-Leader)

Those who come from the world of Vim, may be familiar with the idea of a Leader
key: you press a leader, then a sequence of keys, and instead of those keys
registering as their usual symbols, an alternative action happens once the
sequence is complete.

In practice, this means you can tap `LEAD t a` to toggle the `alternative`
layer, for example. Mostly useful for macro-like functionality where you have a
set of random functionality that you do not want to put on dedicated keys.

There may be other uses for it, but this is what I use them for.

---------------------------------------------------------------------------------

<a id="plugins/macros">
### [Macros](https://github.com/keyboardio/Kaleidoscope-Macros)

Macros are a way to assign a sequence of events to a single key: press a macro
key, and your computer will see "hello world, this was a macro" entered very
quickly. Or assign some complex modifier sequence to a single key.

Pretty much essential for certain software, and for a lot of games, too.

---------------------------------------------------------------------------------

<a id="plugins/magiccombo">
### [MagicCombo](https://github.com/keyboardio/Kaleidoscope-MagicCombo)

Another way to put special behaviour on keys, but this time, we will perform an
action when a certain combination of keys are pressed. We can tell the plugin
that whenever the two palm keys, and the `R2C3` and `R2C12` keys are pressed, we
want some special action to perform.

This can be used to put the keyboard into factory-test mode, for example. Bind
that functionality to a combination of keys that would be hard for a human to
press, but easy to do with a machine, and voila, you can switch to testing mode!

---------------------------------------------------------------------------------

<a id="plugins/mousekeys">
### [MouseKeys](https://github.com/keyboardio/Kaleidoscope-MouseKeys)

As the name says, this implements mouse keys, keys that move your mouse, click,
and all that buzz. It's pretty common by now to have this functionality, but
this plugin goes even further!

You see, it provides a feature called "mouse warping": four directional keys -
north west, north east, south west, and south east -, and a reset key. When you
press any of the directional keys, the mouse cursor will jump to the middle of
that particular quadrant of your screen, no matter where it was before. When you
press any of them again, it will do a similar jump, but this time, limited to
the quadrant you first jumped into. This way, you can quickly move your mouse
around the screen, and then switch to the normal mouse keys for finer-grained
positioning.

This is an incredibly useful feature. Works right out of the box on both Windows
and Mac. On Linux, the XOrg driver was not prepare to handle devices as complex
as the Keyboardio Model 01, so upstream - Peter Hutterer, who is an awesome
fellow by the way - had to update the driver to make it properly support devices
that are a keyboard, a mouse, and a touchscreen, all in one. As of today, there
is a patch that when applied, makes all the cool features available. It may take
a while until the patch gets merged (though most of it was done by upstream, I
added two or three lines maybe), and even more until it trickles down to Linux
distributions, but support is coming!

The [story of Linux support][bug:xf86-libinput] is worth reading, it is not too
long, yet very educational!

 [bug:xf86-libinput]: https://bugs.freedesktop.org/show_bug.cgi?id=99914

---------------------------------------------------------------------------------

<a id="plugins/mousegears">
### [MouseGears](https://github.com/keyboardio/Kaleidoscope-MouseGears)

An extension on top of [MouseKeys](#plugins/mousekeys), that makes it -
hopefully - easier to deal with acceleration. The default mouse key acceleration
is a bit too fast for my taste, and often times, I don't want automatic
acceleration, but would rather have more control over it.

The `MouseGears` plugin provides an easier way to achieve that.

It still needs some heavy tuning, and may even be rewritten at some point, but
the goal will remain: provide some easier control over mouse keys.

---------------------------------------------------------------------------------

<a id="plugins/numlock">
### [Numlock](https://github.com/keyboardio/Kaleidoscope-Numlock)

Primarily used by the default Model 01 firmware, this implements the coloring of
the Numlock layer. If you use the same numlock layer layout as the default
firmware, this plugin may be of use to you as well.

---------------------------------------------------------------------------------

<a id="plugins/oneshot">
### [OneShot](https://github.com/keyboardio/Kaleidoscope-OneShot)

Easily one of my favourite plugins: one-shot layer & modifier keys. What's a
one-shot? It is a key that remains active until after you tap another. This
makes it possible to tap and release `Shift`, then press `a`, and have a capital
`A` come out.

There are many reasons one may prefer one-shots over traditional press & hold
modifiers:

 - You don't have to apply force to hold a key. This is important if you want to
   lessen the load you put on your fingers.
 - Allows you to put modifiers to positions where you normally wouldn't, because
   you can't comfortably hold them, and press other keys at the same time.
   
With one-shots, I only need one shift, because I put it on the thumb cluster,
and release it right after tapping it. No need to have them on both sides,
that's mostly useful when you need to hold the key, because then, it is often
more comfortable to hold the `Shift` with the opposite hand. But with one-shots,
you don't hold them, so there is no comfort penalty, thus, you don't need two.

Then, there's the case when you want to switch to a layer, press one key, and
then switch back. With one-shots, the switching back is automatic. For example,
I use this to input Hungarian symbols like `ű`: it is very, very rare that I'd
need to enter two accented symbols after each other, so the switching-back
mechanism is just perfect.

---------------------------------------------------------------------------------

<a id="plugins/escape-oneshot">
### [Escape-OneShot](https://github.com/keyboardio/Kaleidoscope-Escape-OneShot)

A very small plugin that lets you easily cancel any `OneShot` effects by
pressing the `Esc` key. If any one-shot effect is active, your `Esc` key will
cancel that, and do nothing else. If there are none such, it will work and act
like the traditional `Esc` key.

---------------------------------------------------------------------------------

<a id="plugins/shapeshifter">
### [ShapeShifter](https://github.com/keyboardio/Kaleidoscope-ShapeShifter)

Simply put, changes the symbol sent to the computer when a key is augmented with
`Shift`. If you wanted to send `B` when you press `Shift + a`, you can do it
with this plugin. Originally developed so I can have different symbols when I
shift my number row, so that `Shift + 1` would become `^` instead of the usual
`!`, and so on.

If you want to have different shifted symbols, but don't want to rearrange them
on the host side, `ShapeShifter` is going to be your friend.

---------------------------------------------------------------------------------

<a id="plugins/spacecadet">
### [SpaceCadet](https://github.com/keyboardio/Kaleidoscope-SpaceCadet)

A bit like [DualUse](#plugins/dualuse), but more specialized: it turns your
`Shift` keys into dual use keys: if pressed in isolation, they will act as
opening- and closing parentheses. When held with other keys, they'll act as your
normal shift keys.

Requires that you have two shifts on the keyboard, and doesn't play nicely
with [OneShot](#plugins/oneshot) keys.

---------------------------------------------------------------------------------

<a id="plugins/syster">
### [Syster](https://github.com/keyboardio/Kaleidoscope-Syster)

The `Syster` plugin is a bit similar to [Leader](#plugins/leader), except it
does not swallow the keys input, but deletes them back once the sequence is
finished. This limits the sequence to characters that can be deleted back, but
offers a visual aid in return.

I used this to implement symbolic unicode input, where I trigger the `Syster`
key, and type in a Unicode symbol name, like `heart`, press `Space` to end the
sequence - another difference from Leader, the sequence needs to be explicitly
ended! -, and lo and behold, `❤` is input, with magic.

---------------------------------------------------------------------------------

<a id="plugins/tapdance">
### [TapDance](https://github.com/keyboardio/Kaleidoscope-TapDance)

Tap-dance makes your fingers dance around a single key! No, seriously, this
plugin makes it possible to have different behaviour on a single key, depending
on how many times we have tapped it.

We can have a key output `:` when tapped once, or `;` when tapped twice. Or we
can have it input `[` when tapped once, `(` when tapped twice, and so on. There
are many possibilities.

The reason for the existence of this plugin is that sometimes it is more
convenient to tap the same key twice, than to apply a modifier to it. And at
other times, you want to cram more functionality into a single key, without
having to press more than one modifier, or two different ones for different
behaviours.

---------------------------------------------------------------------------------

<a id="plugins/topsyturvy">
### [TopsyTurvy](https://github.com/keyboardio/Kaleidoscope-TopsyTurvy)

This plugin helps turn the world upside down. As in, given a list of keys, it
will invert the behaviour of `Shift` for those keys, and those keys only. This
way, you can have a [programmer Dvorak][dvk:prg]-esque number row, where the
symbols are unshifted, and the numbers are.

 [dvk:prg]: http://www.kaufmann.no/roland/dvorak/

---------------------------------------------------------------------------------

<a id="plugins/unicode">
### [Unicode](https://github.com/keyboardio/Kaleidoscope-Unicode)

If you ever felt the need to input Unicode, but were frightened away by how
complicated it is on your operating system, worry no more, because the keyboard
is here to help you!

The `Unicode` plugin hides the intricate magic required to input unicode on
various operating systems, so you won't have to care about all that nonsense,
and it provides utility functions you can call from macros to have unicode
symbols input.

<!-- ------------------------------------ -->

<a id="very-experimental-plugins"></a>
## Very experimental plugins

So far, we have looked at the smaller hill we have climbed thus far. It's a big
hill in itself, with lots of great places to visit, lots of stuff to play with.
That was the hill we set out to climb, but not the hill we ended climbing. We
went higher.

The plugins below are very new, very experimental, and may end up being redone
in the near future, but the features they implement are one of the biggest
milestones we have reached so far, as far the firmware goes. In my opinion, at
least.

Before we dive into what these are, let me describe the mountain we ended up on,
with a single sentence of buzz-words: extensible, discoverable, text-based
bi-directional communication between keyboard and host, that is also reasonably
small, and light on resources.

It is text based, because that's easy for humans to comprehend. It is
extensible, because you can extend the set of existing functionality, without
touching the core plugins involved in the process. It is discoverable, because
it includes (optional) documentation, and that allows one to see what the actual
firmware was configured to support. It is bi-directional, because it implements
a way for the host to talk to the keyboard, and the keyboard to respond in
appropriate fashion.

This has some serious consequences, and opens up possibilities we did not have
before:

- We can tell the keyboard the host operating system, so it doesn't have to
  guess. This makes it easier for the keyboard to adapt to the OS.
- We can tell the keyboard what the active application is, so it can adjust its
  layout, or behaviour. If you start Photoshop, the keyboard can switch to the
  Photoshop layer automatically. If you `Alt+Tab` over to Chrome, it can
  automatically switch back to your default layer.
- We can reprogram the layers, without having to compile and flash a new
  firmware.
- We can tweak settings (such as timeouts), without compilation and flashing.
- We can have LED effects react to events on the host: did the test suite you
  ran from you IDE fail? Your keyboard can light up red.

<a id="plugins/focus">
### [Focus](https://github.com/keyboardio/Kaleidoscope-Focus)

`Focus` is the plugin that implements the above mentioned bi-directional
communication, over a virtual serial port. Deep down, it is a pretty light and
lean solution, and the available program and data space is the only limit of
what we can do with it.

---------------------------------------------------------------------------------

<a id="plugins/eeprom-settings">
### [EEPROM-Settings](https://github.com/keyboardio/Kaleidoscope-EEPROM-Settings)

Once I figure out how, this plugin will be the basis of storing stuff in EEPROM
in an extensible way. Keymaps, LED themes, plugin settings, you name it. It will
do the grunt job of validating things, and making sure that the contents of
EEPROM are what the active firmware expects.

It is nowhere near done yet, mind you, and thinking about this topic is what
made me go on a short break from keyboards, to avoid burn out.

---------------------------------------------------------------------------------

<a id="plugins/eeprom-keymap">
### [EEPROM-Keymap](https://github.com/keyboardio/Kaleidoscope-EEPROM-Keymap)

A proof of concept plugin that changes where the keymap is stored. By default,
the keymap is compiled into the firmware, and can only be changed by flashing
new firmware. With this plugin, the keymap can be moved to EEPROM, and will be
used directly from there.

As a consequence, this allows us to save a lot of program space by not having
the keymap there, and moving it to EEPROM instead.

The plugin is a proof of concept for now, mind you.

<a id="future-plugin-ideas"></a>
## Future plugin ideas

Apart from finishing up the EEPROM & Focus plugins, the rest will likely receive
an update to allow better integration with these. This will be quite a
challenge, to make it efficient and optional. This is what is giving me a
headache, and why I'm taking a break from keyboard work - to let ideas sink in,
and to relax.

Nevertheless, there are still a couple of ideas, that need to be explored still,
like a snake game on the LEDs. And - inspired by a recent Women's Day gift to my
wife - a platformer.

<a id="chrysalis"></a>
# [Chrysalis](https://github.com/algernon/Chrysalis)

`Chrysalis` will be a cross-platform GUI tool to configure, and communicate with
the keyboard. I'd like to follow the same principles as `Kaleidoscope`: a light
and lean core, and a host of plugins that can be mixed and matched together. I
do not yet know *how* to achieve this, or if it is even feasible, but that's the
goal.

Among other things, I plan for this tool to be able to (with the help of
plugins, of course):

- Provide a REPL, so one can talk directly to the keyboard, and do things by
  hand. The textual protocol of [Focus](#plugins/focus) help immensely with
  this. Of course, this is for advanced users.
- Allow one to download the keymap from the board, change it, and upload a new
  one - all without compiling or flashing anything.
- Allow one to load a pre-compiled HEX file, and flash it from a GUI.
- Provide a way to set up new LED themes, without reflashing the keyboard.

Currently, almost nothing of that is implemented, the only thing that exists is
a proof of concept skeleton:

 ![Chrysalis - Proof of Concept](/assets/asylum/images/posts/rose-tinted-glasses/Chrysalis.png)

Luckily, a few people already expressed interest in helping out, so you won't
have to endure my total lack of any design sense! Nevertheless, this is a fairly
big project, in an area I'm nowhere near comfortable with, so any help is most
appreciated, if anyone feels like contributing in any way.

<a id="misc-news"></a>
# Miscellaneous news

In other news, I've been quiet lately, ever since reaching the next milestone of
building a bi-directional channel between host and keyboard. The reason for this
is that I'm feeling a bit overwhelmed, and to avoid burning out, I have to cut
back, and distract myself with something else. So a week or two of much reduced
keyboard work to cool off, and let ideas sink in, and to recharge is the current
plan. This will continue for a little while longer. I'll still do
keyboard-related stuff, because I can't resist it, but I won't be pushing myself
anymore. Once recharged, you can expect some swift developments in various
places.

<a id="contributing"></a>
# Contributing

We have made lots of progress in the past few months, and Kaleidoscope and its
plugins are increasingly looking like a very versatile and powerful set of
libraries. It could not have been done without the help of Jesse, who steered
the core library, vetted my ideas, and dug out the reasonable parts of my walls
of text on IRC, and provided helpful hints, and directions. Without the help and
ideas the Keyboardio forum members, some of the plugins may not even exist!

It's a terrific feeling seeing your work be part of something so incredible. I
can highly recommend joining in!

There are a lot of ways one can contribute to the development of Kaleidoscope,
and the plugins, though, most of them will be available only when people get
their hands on hardware. But there are things you can do even now, without
hardware! A bit of creativity, and programming knowledge may be required,
though.

We could use help with improving the documentation of plugins - there are some
that don't have any yet. A little understanding the code should be enough to get
started, and asking us to clarify things will get you even further. This would
be a much needed improvement, and one that does not require neither hardware,
nor a lot of programming experience. Just a little.

For the more adventurous, there are plugins that still need work, and tuning -
see the list above, or the issues open for them.

<a id="non-code-contributions"></a>
## Non-code contributions

A lot of people who use keyboards, and who will be using the Keyboardio Model
01, or the Shortcut, are not programmers, yet, may still want to contribute.
They can still report issues, or submit ideas, all of which are, and will be
most appreciated.

This made me think about ways to make it possible to maintain plugins properly,
without burning out, or taking precious time from the Twins and family. You see,
I'd like to be a good maintainer, but I am fully aware that my time is limited.
Yet, when keyboards start to ship, and arrive at end-users, I'm pretty sure
there will be a lot to do. I would like to be there, because seeing my work bear
fruit makes me happy.

While there is no perfect solution, there is something that may help, at
least to focus: a long term project. A long term project of building a pair of
keyboards for the twins, with them, once they are of appropriate age.

<a id="keyboards-for-the-twins"></a>
## Keyboards for the twins

So what is this "Keyboards for the Twins" project?

I'm a nerd, and as such, I'd love if my kids would be technically adept, and
learn to use computers at an early age. In our modern times, they will need to
be proficient with them machines, no matter what they'll grow up to be. I'd like
to introduce them - and myself too - to building stuff by building a pair of
keyboards with them, for them. I already bought a number of Arduino & Maker
books via Humble Bundle, they may prove useful in the not too distant future!

But, there's still one question that remains unanswered: how will a long term
project help with maintenance, if I have very little time as it is?

That's where another idea comes in: in the past few weeks, a handful of people
contacted me, to ask if I have a Patreon account, or anything similar. I didn't,
because I do all this work for one reason, and one alone: because I want to.
Most of the things I developed so far, are things I will either use, or things I
find interesting. It did not feel right to ask - or even accept - money for work
I would do anyway. But with the "lets build a pair of keyboards" project, I will
need equipment, parts, and whatnot. I can accept money towards that goal!

So I went out and created a [Patreon page][patreon] with this in mind.

 [patreon]: https://www.patreon.com/algernon

If you are a current or potential user of my keyboard-related work, and you want
to thank me, and think that an e-mail, or a message on Twitter is not enough (it
is, really!), or just want to contribute to the Fund, you now have the option
to.

In case you have a specific issue, or request, that you'd like me to handle with
priority, other than trying to convince me of its importance (it is possible,
ask Jesse), or bribing me with creating something in return, you now have the
option to use Patreon to support my dream project, and in return, I'll give
higher priority to the task of your choosing.

Keep in mind that all the work I did, do, and will do in the future, will remain
free software, even if it was done for people who supported the project via
Patreon. There is no exclusive reward. It's all there as an option, as a last
resort, so to say. And because people asked if I have an account.

This will not be the only source for the keyboard building project either, so if
I end up with an empty bucket, that's perfectly fine too. If all goes according
to how I see things, people will not choose the last resort. There are so many
better options. Anyway, lets talk about something different, shall we?

<a id="what-next-questionmark"></a>
# What next?

Lately, I always tried to predict what I would do next, or what I should be
doing (the two rarely had any overlap, and I feel a tad guilty about that), but
I try to learn from my mistakes, so this time, I'll make no promises, nor raise
any hopes.

I will simply spend the next few weeks relaxing, and typing on the gadgets I
have, getting used to their layout, and trying to learn the [ADORE][adore]
layout I designed a while back. I'm curious how it feels on the Model 01.

 [adore]: https://github.com/algernon/Model01-sketch#adore-layer

So next time, I hope I can report on how well typing went, and how little code I
did. I will admit to any guilty keyboard commits and pull requests I've done,
though, so it won't be entirely boring!
