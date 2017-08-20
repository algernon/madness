---
title: "Lessons learned while developing keyboard firmware"
date: 2017-08-20 15:20
tags: [Hacking, Keyboard, keyboardio]
---

I have worked with embedded hardware before, a long, long time ago, near the
turn of the century. But it was a brief exposure to this world, and even back
then, I was using hardware much more powerful than what the keyboards I work
with today have, the [Atmega32u4][atmega32u4]. My prior experience did not prove
all that useful in my recent work. There were lots of assumptions I made that
were flat out wrong, lots of surprising things I had to discover. Some of these
things are obvious in hindsight, some I should have known, yet, I had to learn a
lot to achieve the things I desired.

 [atmega32u4]: http://www.microchip.com/wwwproducts/en/ATMEGA32U4

To make it easier for anyone who does down a similar rabbit hole, I will try to
summarize what I learned, and how.

<!-- more -->

I think I can set up three main areas where I learned a lot this past year or so
working with [Kaleidoscope][k]: [memory](#section/memory),
[code size](#section/code-size), and [speed](#section/speed). In addition, we'll
see a few words about [tools](#section/tools), and finish up with a
[summary](#section/summary).

 [k]: https://github.com/keyboardio/Kaleidoscope

<a id="section/memory"></a>
# Memory

There is only 2560 bytes of RAM on the [Atmega32u4][atmega32u4] chip - not much
to work with, and we have to fit a whole lot of stuff in it no matter what, to
even make the keyboard work. RAM is scarce, and a precious resource. We can't
use swap space, or easily buy another gigabyte. We have 2560 bytes, and that's
it. We need to fit all global, all local, and all instance variables in here.

 [atmega32u4]: http://www.microchip.com/wwwproducts/en/ATMEGA32U4

The first thing we need to keep in mind when working with hardware with so
little resources, is that we should forget all about dynamic allocation. It may
sound efficient to only allocate as much as we need, on-demand, and free it as
soon as possible, doing so requires book keeping. That book keeping will use up
not only CPU cycles, but RAM too. So we are left with having to statically
allocate memory, never grow, never free. We will have to make compromises along
the way, keep a balance between allocating enough, and not allocating too much.

## RAM, Flash, and EEPROM

Fortunately for us, not everything we use needs to be in RAM: we also have
32kbytes (minus boot loader) of flash memory, which we can use to store
read-only data in. Such as our keymap, which is - for the most part - read only.
We can store various helper tables here too, saving a significant amount of RAM,
at the cost of a few extra instructions to fetch these. For example, by putting
the keymap in Flash, we save 2 bytes of RAM per key. On
the [Keyboardio Model 01][kbdio], this is 128 bytes per layer, a huge amount.

 [kbdio]: https://shop.keyboard.io/

We can also store things in the EEPROM, an additional 1kb of persistent space.
But access to the EEPROM is slower than RAM and Flash, and EEPROM has a limit on
how much each cell can be written, so we can't use it as a kind of RAM
extension. It is best used for things we want to change, but change is
infrequent. A good example is the keymap: we can have a default burned into
Flash, and customisation in EEPROM, allowing us to change the keymap without
reflashing a new firmware.

Or we can store the keymap in EEPROM-only, saving us a lot of program space,
too, at the cost of having a lot less space in EEPROM for other things (color
maps, settings, and so on).

But, back to the topic of RAM!

## Data structures

Because we have so little memory, we have to be careful about the data
structures we choose.

### Linked lists

While it may be tempting to use a linked list, an array sometimes works better,
because it has less overhead, even if you waste bytes by allocating an array
larger than what you will end up using. To give you an example, `Kaleidoscope`
has a 24-element array for LEDmodes, where each member is a pointer to a
`LEDMode` object. As pointers on this architecture are 16-bit, we have a 48-byte
array. Would we use singly linked lists, we'd use 4 bytes per entry (current &
next pointers), so on the same 48 bytes, we would only be able to use 12 LED
modes. The factory firmware for the [Keyboardio Model 01][kbdio] uses 14 LED
modes, so we would be using 8 bytes more RAM. If we use less than 12 LED modes,
then the linked list would save us a few bytes, but not that much more. If we
wanted to cut down on memory use, then in this case, it would be easier to
decrease the size of the pre-allocated array.

The lesson learned from this? While linked lists sound like a good data
structure, which would reduce our memory usage, in many cases, they are not
worth it: they don't save enough memory, but increase complexity, code size and
decrease speed (because of using more instructions). Adjusting array size
appears to be a simpler, better solution.

Mind you, there are situations where adjusting the array size is not all that
easy, say, because the array is hidden deep within an object, then linked lists
may end up being the better solution.

### Flags

In other cases, we may want to use a set of flags, to allow users to tune a
particular feature one way or the other. The logical choice here may be using a
`bool` for each flag, which is a reasonable choice if you have plenty of RAM.
But each of these booleans will use a full byte, to represent two bits. That's
not going to be efficient. If we have more than one flag, using an `uint8_t` and
the `bitRead`/`bitSet` functions, a bit map is much more efficient. On the other
hand, bit maps are a lot less nice to work with. Thankfully, we can have a
compromise in some cases: if we only ever access our flags by name, we can use a
structure like the following:

<div class="pygmentize" data-language="c++">struct {
  uint8_t one:1;
  uint8_t another:1;
  uint8_t third_time_is_the_charm:1;
} flags;
</div>

This will still use only a single byte of RAM, but we can store three flags, and
access them as `flags.one`, `flags.another`, `flags.third_time_is_the_charm`!
Easier to read than, say, `bitRead(flags, FLAG_ONE)`, and accomplishes pretty
much the same thing.

The downside is that we can't easily index the flags, would we want that. This
last method is not suitable when you want to track a single-bit state, but only
for flags.

Lesson learned: sometimes the smallest data type the language allows is still
too large, and we will want to pack more things into a byte. This is - as
everything else - a compromise, because working with bit maps, and data smaller
than a byte in general will require a bit more code (flash space & speed
implications) for saving RAM.

### Sub-byte data

While writing keyboard firmware, there will be a lot of cases where the data we
work with will not need 256 different values, where we could get away with using
fewer bits. For example, it is a safe assumption that we will not want to
support more than 32 layers (and 32 is already a lot of them). Likewise, we only
have 8 modifiers on modern keyboards, so we only need to support 8 one-shot
modifiers too. That is, while we can have more than 8 modifiers on a keymap, we
only support 8 *different* modifiers.

Why is this important, you may ask, and that's a very good question!

It is important because we have a lot of key variations we need to represent on
a keymap, and only a limited amount of space. We will need at least 16 bits to
describe each key (8 won't cut it, because that's pretty much used up by the
regular keycodes, leaving no space for special keys like layer keys, macro keys,
one-shots, and the like). But at 16 bits per key, we are looking at 128 bytes of
data for the Keyboardio Model 01, and over 200 bytes for a full-size keyboard!
Would we use 24 bits, we'd be looking at 192 bytes for the Model 01, not
counting the overhead this would mean for the code size (remember: this is a
8-bit architecture! working with data larger than 8 bits is going to take more
than one instruction in most cases).

So what do we do? We pack more things into a byte. But that isn't all that easy
as one may expect. We can't just split 8 bits into equal parts, like our first
instincts may tell us. Why? Because we need 8 values (or 3 bits) to store all
one-shot modifiers, but five bits to store our 32 layers. If we split our
available space the same across each key, we would need to have enough bits for
the key that needs most values, which would end up wasting a lot of bits for
keys that can use less than that.

Within `Kaleidoscope`, we use two different ways of solving this problem: the
core firmware splits a 16-bit `Key` into `flags` and `keyCode` parts, and uses
the flags to distinguish between layer keys, macros, mouse keys, normal keys,
and so on. This allows us to work with normal keys easily: we check the flags,
and if that says its a normal key, we can work with the 8-bit `keyCode` only. If
any of the special flags are set, the part of the firmware responsible for that
flag will interpret the `keyCode` the way it wants to.

The other way is using ranges: if the `RESERVED` bit is set in `Key.flags`, we
will not use the rest of it as a split `flags` / `keyCode` pair. We will not
divide each key the same way, into two 8-bit parts. Instead, we have ranges that
tell us that keys within this range belong to one plugin or the other. This way
we can split our 16-bit code not by how many bits we need, but by how many
values. This allows for a more compact representation, at the cost of a bit more
work to figure out what to do with the key (checking if it is within a range,
rather than checking a flag bit).

In other words, splitting up a 16-bit key code into varying sized ranges allows
us to put more information in there than if we would split it up into fixed
sizes. This allows us to have so describe so many different keys with merely 16
bits! This allows us to keep things small.

And the lesson learned? If we want to work with sub-byte data, splitting an
array of bytes up the same way for all elements is not always the most efficient
solution. Or in other words, our array can be not just an array of the same
type, but an array of same-sized types too. Always keep in mind that we are
working low-level, we can - and should - use the most efficient in-memory
layout, even if that can't easily be described with the type system our language
provides.

<a id="section/code-size"></a>
## Code size

While we have a lot more Flash space than RAM, it is still severely limited. We
can't just push every bit of read-only data into Flash, or we'll run out of
flash space soon, too. But it is not just read-only data that can eat up our
program space fast: code can grow huge too.

### Read-only data

To save RAM, we often find ourselves putting read-only data into flash. While we
have more space there, it is still a finite resource, so we must pay attention
to lay things out efficiently. As such, all the things learned above, with
regards to RAM, also apply to data stored in the read-only Flash space.

### Many small functions

Conventionally, we are taught that small function make more maintainable, easier
to debug code. While that is true, functions have an overhead: we need a few
bytes to set up the arguments, some to call the function, and the function needs
to pop the arguments, and clean up after itself too. That's a good few bytes per
function call. While the compiler can inline a few things, it can't do that in
every case. Especially not across compilation unit boundaries.

Sometimes it is worth taking extra steps to make sure a function is inlined, in
cases where the function is really small (smaller than the overhead would be),
and used in many places. The savings in this case can be huge (sometimes even
over a hundred bytes!).

Be careful, though, because if the code inlined is bigger than the overhead of
calling a function would be, inlining can easily result in much bigger code
size.

### Floating point

The architecture of our choice is an 8-bit one, without hardware support for
floating point operations. If we use floating point, it will be implemented in
software, and as such, will have a noticeable impact on our code size.

While using floating point arithmetic would be convenient sometimes, one must
consider the cost of that convenience. Often times, we would be better off with
integer arithmetic, or lookup tables, or something similar.

### Integer types larger than 8 bits

Again, because the MCU we use is an 8-bit one, if we are using integer types
wider than that, that will increase not just RAM use, but code size too. The
wider the type, the more it will cost in terms of code size to work with it too.

However, using wider integers is not something we can avoid, nor should we. But
to save on code-size (and speed!), we need to carefully consider how wide an
integer we do need. Sometimes we can make do with something smaller. Or we can
split the work so that we use the second half of a 16-bit integer only rarely,
and then use two 8-bit ints instead. This, of course, only works when our access
pattern allows for this kind of split, to use a `struct` of two 8-bit integers
instead of a single 16-bit one.

<a id="section/speed"></a>
## Speed

Speed is where I made the most mistakes while working on keyboard firmware. Most
of the things I learned should have been obvious. In most cases, they were too,
I was just not aware how much certain things cost.

### Loops

The biggest enemy of speed is big loops on the fast path. For example, if we
want to loop over each key on the keymap, in each cycle, that will be one of the
slowest things possible. Not only is the loop itself expensive, if we call any
non-inline functions from within the body, then each cycle, the MCU will have to
spend precious cycles setting up the arguments, calling the function, and then
cleaning up afterwards. Doing this once or twice per cycle is not a big deal.
Doing this 64 times per cycle is going to end up costing us dearly, on the
magnitude of half a millisecond as a baseline. And half a millisecond is a
tremendous amount.

### Function calls

Function calls have their cost, and as with code size, if functions are small
enough, inlining can improve our speed too, not just our quest for a smaller
size. If we have a function called every cycle, potentially many times, making
sure it is inlined can save us a lot of time.

### Caches

One of the ways we can avoid expensive operations, like looping over the keymap,
is to use caches: to figure out the value we need once, and use it as long as it
is valid. For example, to see what key code a physical location of a key has, we
can use a cache that we update only when layers change. This way our lookup
function becomes a simple lookup in RAM, instead of a loop that involves many
more lookups (in Flash or EEPROM, both of which are slower than RAM). This, of
course, comes with the cost of using more RAM, and slightly more complicated
code. We trade RAM and code size for speed, if we go down this route.

Sometimes, though, it is worth the price. If we can save a *milliseconds* at the
cost of a handful of bytes in RAM and Flash, that is a very, very sweet deal.

### Doing things

Doing anything will always cost more than not doing anything. Write your code so
that it can figure out if it needs to do anything as soon as possible, and
return early if it has no task at hand. In the context of `Kaleidoscope`,
plugins that register event handler or loop hooks should do their utmost to
return early, and not waste cycles figuring out they have nothing to do. Chances
are, most of the time, they will return early. Optimize for the most common case
first, even at the cost of making other paths slightly less efficient. An
occasional bump in our cycle times is better than a constant increase.

Nevertheless, consistent speed is also important: a keyboard that responds
within 3 milliseconds when on the base layer, but 13 when on another, is just as
useless as one always responding within 13 ms. In other words, it is okay to
have bumps, but try to make them small and predictable.

<a id="section/tools"></a>
# Tools

Debugging and profiling firmware is an art in itself, a complex one at that. We
have a few tools to our disposal to aid us, however, in the context of
`Kaleidoscope`.

To combat size growth, we can use `make size-map`, which gives us a list of
symbols within the firmware, ordered by size. Anything particularly big can then
be looked into, to figure out if it can be made smaller. Either by going over
the code, trying to spot problematic parts by the naked eye, or use `make
decompile`, and looking at the assembly source.

This latter one can be used to figure out where things slow down, but that
requires a solid grasp of AVR8 assembly.

When it comes to speed, the [Kaleidoscope-CycleTimeReport][k:ctr] plugin can be
of great assistance: it tells us the average time of a main loop cycle between
reports. We can use this information to try tweaks, disable plugins to see how
they affect the cycle times, and so on. More an aid for trial and error
debugging than deep study of the code, but it is a practical way to approach
efficiency issues.

 [k:ctr]: https://github.com/keyboardio/Kaleidoscope-CycleTimeReport

<a id="section/summary"></a>
# Summary

The gist of all this, is that firmware development is very different from how we
would write programs otherwise. The patterns and idioms we use elsewhere, do not
apply here, or even have undesired consequences. It is a very different world,
but a beautiful one. It may be full of trade-offs, but learning the cost of
things is beneficial in other areas too. It makes us better programmers.

Working in such a restricted environment can feel both a burden, because of all
the limitations. But it can also feel liberating at the same time, because we
can leave all that we learned elsewhere behind, and start afresh, find new ways
to fit into the small space we have for our programs.

Having to find alternative ways, ways to make our code either small, or fast, or
use less RAM is a whole new experience, that requires a different way of
thinking. A different mindset, different solutions, clever tricks, weird hacks.
Whole new toys to play with!

# Thanks

I want to thank all my [patrons][patreon], for their continued support. Your
support allows me to write posts like this, which I would not have the resources
to write otherwise.

 [patreon]: https://www.patreon.com/algernon
