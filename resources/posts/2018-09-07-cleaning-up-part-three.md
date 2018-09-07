---
title: "Cleaning up, part #3"
date: 2018-09-07 08:30
tags: [News]
---

In the last installment of [cleaning
up](/blog/2018/07/01/cleaning-up-part-two/), I described how removing some
infrequently used features from the site resulted in an even smaller size,
especially when combined with minification. I have not stopped working on making
this site nicer since. There have been a few things that annoyed me long enough
that I sat down a few days ago, and fixed them. Turns out doing so was much
easier than I thought.

<!-- more -->

The first thing I did was I redesigned the [landing page](/): it used to show an
excerpt from the latest blog post, a left over from ages past where I had more
than one post shown there. This has been annoying me for a while, because such
truncated posts are just plain horrible. On the other hand, I didn't want to put
the whole most recent post on the landing page, either. What then should go
there?

Because there are other things on this site than a blog (or, truth be told, used
to be - the blog is the only useful thing here nowadays), I turned the landing
page into a logo and a few buttons that lead to interesting places. This made
the initial experience much better in my opinion: no more truncated post, handy
links to various places - what's not to like?!

This, however, presented a problem: I needed to make the [blog archives](/blog/)
more useful. Previously, the archive was nothing more than a flat list of posts,
without dates or anything. One could infer the date from the URL, but that's not
a great experience, is it? So I added dates to the archives. But it was still a
flat list, which is okay, but it could be better. For this reason, I grouped it
by year, and indented the entries a little.

While there, I also removed a few places that injected bootstrap artifacts into
the HTML, even though I don't use Bootstrap anymore. That saves me a few bytes
each page, too.

With the improved archives and the landing page, I think the site design is at a
place now that I'll be happy with for a while.
