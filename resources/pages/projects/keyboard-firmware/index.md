---
title: Keyboard firmware
---

I have a passion for unconventional mechanical keyboards. It all started in
[November 2015][blog:looking-for-a-keyboard], when I started looking for a new
keyboard. I soon ended up with an [ErgoDox EZ][ez] while I was waiting for the
[Keyboardio Model01][m01] to ship, and started tweaking the firmware, sometimes
heavily. Soon after, a [Shortcut][shortcut] prototype ended up on my desk too.
Then an [Atreus][atreus], followed by a [Splitography][splitography], with a
[KBD4x][kbd4x] being the latest addition.

 [blog:looking-for-a-keyboard]: /blog/2015/11/20/looking-for-a-keyboard/
 [ez]: https://ergodox-ez.com/
  [m01]: https://shop.keyboard.io/
  [shortcut]: http://shortcut.gg/
  [atreus]: https://atreus.technomancy.us/
  [splitography]: https://softhruf.love/collections/writers
   [kbd4x]: https://kbdfans.cn/collections/diy-kit/products/kbd4x-custom-mechanical-keyboard-hot-swap-diy-kit

I ported [Kaleidoscope][kaleidoscope] (the firmware originally developed for the
Model01) to all of them, because I believe that in many ways, this is the best
keyboard firmware out there. I started the [Chrysalis][chrysalis] project too,
which is a GUI configurator for keyboards powered by Kaleidoscope.

 [kaleidoscope]: https://github.com/keyboardio/Kaleidoscope
  [chrysalis]: https://github.com/keyboardio/Chrysalis

Suffice to say, I enjoy working with mechanical keyboards, especially
unconventional ones. I'd love to port Kaleidoscope (along with Chrysalis) to
more hardware, but my resources are limited. I already bought three keyboards
for the sake of porting, I can't justify buying more at the moment.
Nevertheless, there are boards out there that are whispering to me, promising me
fame and fortune (or at least a big bucket of fun), if I were to port
Kaleidoscope to them. And I would, if I had access to the hardware!

<a name="the-deal"></a>
## The deal

So, here's the deal, or an offer: if anyone sends me a keyboard they want
Kaleidoscope to be ported to, and I get to keep the board after, I'll port it.
Within reasonable limits, of course, so talk to me first.

I also have a wishlist of boards I want to port Kaleidoscope to at some point,
so if you, my dear reader, are a vendor making any of these, or you're
interested in seeing the firmware ported to them for any reason, [get in
touch][mail:porting].

 [mail:porting]: mailto:firmware-porting@gergo.csillger.hu

<a name="why"></a>
## How is porting Kaleidoscope beneifcial?

At some point, I should explain this, I suppose. For now, lets say that the
biggest benefit is that porting also includes porting [Chrysalis][chrysalis] to
the keyboard in question, which means that users will be able to change their
layouts, configure LEDs, and whatever else Chrysalis and Kaleidoscope end up
supporting down the road - without having to compile or flash anything. All they
need to do, is download Chrysalis, run it, and everything else can be done from
the comfort of the application.

Mind you, Chrysalis is still in heavy development, but we're making rapid
progress.

 <a name="wishlist"></a>
## Wishlist

### [Azreon](https://azeron.eu/)

This is one strange device. I love it. It has a good number of keys, an analog
thumb stick, and looks like the ideal gaming peripheral to use alongside a
pointing device.

The tricky part of the porting work would be the analog thumbstick support. Its
default firmware is not open source, so that will complicate porting a little,
but last time I talked to the creators, it seemed they're not opposed to custom
firmware, so I'm hoping they're also willing to share how the keys are wired.
Worst case, I'll open it up and check, or go with trial & error, both work.

Having support for analog inputs in Kaleidoscope would be lovely, so this is at
the top of my list.

### [Georgi](https://www.gboards.ca/product/georgi)

I bought the [Splitography][splitography] both for porting purposes, and for
practicing Steno. I have a soft spot for Steno, because my late Grandmother used
to be a typist, and she was also fluent in Hungarian shorthand. Whenever I
practice steno, I remember her. She would have loved these steno keyboards.

Anyhow, even though porting Kaleidoscope to this board would be very easy,
there's one thing that makes it interesting: low profile switches. I don't have
any boards with low-profile switches, and this seemed like a small and
affordable one.

### [Planck](https://olkb.com/planck)

A basic port is already present in Kaleidoscope, for the older AVR variants. The
current port doesn't support audio, nor does it support LEDs. I'd love to fix both.

A [Planck Light](https://www.massdrop.com/buy/massdrop-x-olkb-planck-light-mechanical-keyboard?mode=guest_open) would also be interesting to try my hands on.

Since its firmware is open source, I can always look at QMK for hints if I get
stuck, which makes porting reasonably easy. The hard parts would be making sure
that the port is built on reusable components, so the next board with a speaker
would be much easier to port.

Oh, and recent versions of the Planck also support rotary encoders, so another
input source to add to Kaleidoscope!

The Planck keeps on giving.

### [Comet46](https://github.com/satt99/comet46-hardware)

Due to being wireless. Any other keyboard that's wireless is interesting, this
is the only one I could find that QMK supports, and has reasonable availability.
I'm interested in porting to any kind of wireless board.

### [XMIT Hall-Effect keyboard](https://www.xmitkeyboards.com/)

Due to the hall-effect switches. This would be challenging, as the firmware is -
as far as I know - not open source, and I have no idea about neither the
controller used, nor about its wiring.

### [Wooting](https://wooting.io/)

Flaretech optical switches! But like in the XMIT case, the firmware isn't open
source, and there's no public information about wiring and MCU used.
