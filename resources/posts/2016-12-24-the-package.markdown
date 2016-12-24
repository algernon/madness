---
title: "The Package"
date: 2016-12-24 10:00
tags: [Hacking, Keyboard, keyboardio]
---

This past week I have been waiting for a package. Not just any random package,
mind you: this one came from the US, and was special in a number of ways. The
eagerness to have it in my hands was huge, so huge that I started to plot its
route on a map, trying to estimate where it will land next, when, and where it
will go after. This was a reasonable success, but once the package arrived to
Hungary, the excitement increased by a tenfold.

It was originally to be shipped to my door on the 22th, but it missed the last
truck leaving the facility by about five minutes. On the morning of the 23th, I
got an e-mail saying that the package will be delivered. This was around 9am.

The rest of this post is the story of the package, with a few words about what
I've done with it so far. Keep reading, but be aware that there are no pictures
in this post. You will understand why, as the story unfolds.

<!-- more -->

# The wait

According to the online tracking progress, The Package™ was put on a truck at
9:15. Takes at least half an hour from get from the FedEx storage facility to my
door, and some more if there is traffic (there is always traffic). So until ten,
it won't be here, don't have to worry. But come 10:00, I found myself listening
for the sounds of a truck, or van trying to park below the window. My wife found
that funny.

An hour later, and I was going to the window every 10-15 minutes, stare out a
bit, hoping to catch a glimpse of the carrier. Wife still found this funny,
bless her.

By 12, things went south, and I was staring out the window non-stop, while my
wife was prodding me to go and help her make Christmas cookies. <em>"Trust me,
it will show up as soon as you start kneading!"</em> - she was saying.
Reluctantly, I went to the kitchen, where everything was prepared, all she
needed were my hands. In the meantime, she took over the job of staring out the
window.

The moment I start working, elbow-deep in messy goo, the doorbell rings.

Fuck. She was right.

# The Mad Rush

The main issue is, our doorbell does not work. It rings, but we can't open the
door, and can't speak either. So I quickly washed my hands, put on a coat, and
rushed down as I were: slippers, a messy t-shirt, breeches, hair all over the
place like if I were a mad scientist (not kidding!). Of course, the elevator was
on the top floor, so I stumble down the stairs, to find myself face to face with
a neighbour, but no carrier in sight.

<em>"Was the carrier coming to you?"</em> 
<em>"Yes..."</em> 
<em>"He just left, saying no-one's home. Went right."</em>

# The Chase

Off I go like a rocket, as far as rocket wear slippers, of course. Out in the
street, I see a guy with a package under his arms, so I sprint that way, and
reach the crossing street a few seconds after he does. The light are about to
turn red, and he's on the other side, opening the door of his van. I rush over,
and say hello.

<em>"Oh, you are the one I was supposed to deliver this to?"</em> 
<em>"Yes."</em>
<em>"Noone answered the doorbell, I thought nobody is home."</em>
<em>"Yeah, it doesn't work, that's why I left a note to call me instead."</em>

For the record, my name is not on the doorbell, and my address did not include
the floor either. The only way the carrier could have known which doorbell to
ring, was to read the note that clearly said it does not work, and had my phone
number two words later. But these are minor details!

Anyway, this doesn't matter anymore, I caught him in time!

So we get to the point of paying customs and stuff, which is always a pain,
because they don't take cards, only cold, hard cash, and customs never tell me
the exact amount I have to pay. Best they got so far is an estimate. This time,
the estimate was about $50 off, but that's ok, because I was prepared for that,
and had a lot more cash with me. Except not. I was about $3.5 short.

Fuck.

# Money, money, money

Thankfully, there are a number of ATMs nearby, so I offered to run to one, grab
some cash, get back, if the carrier waits for me. He looked me over, in my
breeches and slippers: <em>"Hell no, you're not going to go anywhere like this.
I'll take you."</em>

And so I found myself in a FedEx van, on our way to a Bank. It must have been
quite a sight, as I waltzed in all proud with my mad-scientist hair and messy
t-shirt, in a big winter coat. I got a few looks as I crossed the street from
the van to the ATM, but ATMs don't judge, nor care.

I paid the carrier, he took me back home, and off I went, with The Package™ in
hand!

# Opening

At home, I was greeted by a worried wife, who had no idea why I was away for so
long - I had no time to take my phone. We snapped a few pictures of the package,
of course.

 ![The Package](/assets/asylum/images/posts/the-package/the-package.jpg)

I lied, there is a picture.

Anyhow, while my wife opened the package, I hopped on to IRC to see the sender
go <em>"NOOOOOOOOOOOOOOOOOOOOOO! FedEx says they missed you!"</em>, and to
happily tell him that the Chase was a success.

# The contents

In the package was a hand-assembled prototype of the [Keyboardio Model01][m01],
made with pre-production parts. I was surprised how good it looks, despite being
a prototype from pre-production parts. I expected much worse when Jesse first
described it to me some ten days ago.

 [m01]: https://shop.keyboard.io/

# Checking if things work

Sadly, the top rightmost key fell out (did I mention this is a janky
prototype?), so I only have 63 keys at most. Time to test if they work! I plug
the keyboard in, start typing... weeell... left side is kind of bust, it seems.

Fuck. The left side has the `Prog` key.

Turns out, the keyboard froze. Literally. Gave it half an hour to warm up in the
room, and all keys - except for the Fallen One - work now. Sweet!

# Flashing through the snow...

The reason I was sent a prototype was to work on the firmware (this is starting
to become a pattern, maybe I should start a firmware hacking consultancy?), so
once the initial heart-attack induced by the left half not working was done and
over with, I flashed a slightly modified firmware, that included
the [Borealis plugin][plugin:borealis] that
I [wrote about earlier][blog:borealis], and to my surprise, it worked! Except
the colors were off, because I saw a type called `cRGB`, and assumed that the
members in it were in RGB order too. They aren't, they are in BGR, so the red
and blue components were swapped. Quick fix, and a lesson learned: read the
code, dummy!

 [plugin:borealis]: https://github.com/algernon/Keyboardio-Borealis
 [blog:borealis]: /blog/2016/12/09/Akela/#borealis

Armed with this new found courage, I started to hack around randomly to try and
see what happens. And in a couple of minutes I ended up with a firmware that
crashed in a buffer overflow as soon as it booted.

Fuck.

No problem, I'll just flash a fixed one, only need to hold the `Prog` button
while plugging the keyboard in! Except, that did not work, either, the keyboard
still crashed.

Fuck.

No problem, Jesse told me that if I unscrew the left half, and remove the
enclosure, it will reveal a reset button. I did so, and lost my
<em>unscrew-a-keyboard</em> virginity. This was the first time **ever** that I
opened up a piece of hardware, not counting the few times I opened the house of
my desktop PC to clean it. Decades of being in IT, I never ever opened anything
except the PC house.

That reset button worked, thankfully, and I was able to restore a working
firmware. Another lesson learned: buffer overflows are bad, and even worse when
installing the fix them is cumbersome to say the least.

# The rest of the day

I spent most of the day trying to understand why my recent changes do not work.
The short story is that it is all fixed now, and I have a prototype where the
firmware works the way it is supposed to, including layers, mouse keys, etc.
There is still lots to do, mind you, some of the workarounds have to be cleaned
up, and I need to test most of [Akela][akela], which do not work at all, either.

 [akela]: https://algernon.github.io/Akela/

Okay, some things do, like the [LEDEffects][akela:ledeffects] plugin, but none
of the ones that change key behaviour do.

 [akela:ledeffects]: https://algernon.github.io/Akela/plugins/LEDEffects/

# The Plan

I will spend some time today with the prototype, and iron out some of the other
stuff. I will be doing another round of updates once I'm closer to having the
Akela plugins working. Expect it sometime before the new year.

Merry Christmas to all my readers who happen to celebrate this holiday!
