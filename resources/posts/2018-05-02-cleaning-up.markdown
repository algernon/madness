---
title: "Cleaning up"
date: 2018-05-02 07:50
tags: [News]
---

It's been a bit over two years since the last visual update of this space. In
that time, a lot changed, and I felt it was time to refresh how it looks. So
over the past month or so, I've been planning and experimenting with ideas, to
see which one sticks, which one feels right. The result is this cleaned up,
pretty minimalist site.

The minimalism is a conscious choice, and a result of one of the driving
principles that made me redo my theme.

<!-- more -->

I had a number of goals with the new design. I didn't want to just refresh the
looks, tweak a few colors here and there, rearrange the layout a bit - no, I
wanted to change a few things fundamentally. One of those goals was **better
privacy for my visitors**, so the first thing I did was to remove my
[Matomo](https://matomo.org/) (née Piwik) tracking code. The primary motivation
for the tracker was to observe visitor behaviour, so I could adjust my site
accordingly. But over the years, I grew tired of trying to write for others,
tired of trying to cater to an audience I know nearly nothing about, trying to
act as if I had anything to profit from more visitors. Thing is, I write here
mostly for my own amusement. If it is useful in some way for others, or if they
enjoy reading it - that's great, but at the end of the day, that's a nice bonus,
and not a goal. This made tracking useless. Not to mention that in this day and
age, any such tracking is something *I* would frown upon, so why should I
subject my visitors to it?

With tracking done, the vast majority of JavaScript was gone from the site,
which gave me another idea: what if I **got rid of all JavaScript**? I mean,
there wasn't much else that made use of it... I had some Bootstrap things here
and there, to position tooltips, and a small thing that allowed visitors to move
between articles with key bindings. Neither of these were very useful, the key
bindings were even annoying, so off they went.

An even worse case of privacy abuse was my use of Google and CDN services for
some of the resources used on this site (such as fonts, Bootstrap, etc). I know
I wouldn't do anything evil with the tiny amounts of information I collect with
my own tracking, but I can't say the same about Google, and frankly, I wouldn't
trust CDNs all that much either. So I made an effort to get rid of these too
(spoiler: I succeeded).

This reduced the size of every page considerably... and if I reduced it a lot, I
wondered if I could **compact the front-page into 10kb**? What if I made the
theme go on a diet, and get rid of things that aren't useful anymore? What was
the biggest contributor to size, I wondered (I didn't actually wonder, mind you,
the answer was obvious from the start)? Bootstrap. A huge amount of CSS, for a
site that made use of very little of it. I used to use a lot of Bootstrap in the
past, but not much of it since the previous visual refresh. Dropping it saved a
huge amount on size, and I only had to update a few things here and there. As a
bonus, I could simplify the markup too, because I needed less boilerplate for
bootstrap things! And since Bootstrap was pulled from a CDN, not doing so also
made my first goal more achievable to boot!

But if not Bootstrap, what then? Should I roll my own CSS? As it turns out -
yes, I should. I do not need much. I do not need anything fancy, or colorful, or
"modern". This is a simple site, a simple blog, the design should reflect that
too. This diet is where the current design was born of: a desire to minimize, to
strip down to purity, while still maintaining a kind of elegance. My goal was to
end up with a design that is like a high quality white shirt: nothing
extraordinary, nor fancy, but you'd wear it for a wedding.

I hope I succeeded. But even if I didn't on the looks, the size speaks for
itself: from 290kb initial download through 14 requests (8 external, 4 local),
we're down to 7.4kb through 3 local requests. That's almost forty times smaller,
and less than fourth of requests.

Needless to say, I'm happy with the outcome, long may it stay.
