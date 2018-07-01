---
title: "Cleaning up, part #2"
date: 2018-07-01 18:15
tags: [News]
---

[Last time](/blog/2018/05/02/cleaning-up/) I wrote about the reasons why I
redesigned the looks of this site, the ways I cut down on size. I described how
I went from 290kb initial download through 14 requests to 8.4kb through three
requests. Since then, I made a few minor changes that made the site even
smaller.

While I should be writing about keyboard firmware progress, and a number of
other things, time only allows me to write this post, about website diet. Like
the size itself, this will not be a very long post.

<!-- more -->

To cut to the chase, the end result is that I went from *8.4kb* through three
requests (of which *7kb* was CSS with long expiration) down to *2.1kb* through
two requests, of which only *0.8kb* is CSS.

The biggest, and most visible change is that I dropped syntax highlighting, that
saved me *4.2kb* on a fresh visit, but I still had almost *3kb* of CSS for no
good reason. That no good reason was that I was writing my CSS by hand, and
didn't have a minify step. Instead of implementing a minification step, I
converted my CSS rules to [Garden](https://github.com/noprompt/garden), which
can minify it for me. And I get to write CSS with Clojure, so even better! This
made my CSS considerably smaller: from *2.8kb* to around *1kb*, slightly less.

And if I minify CSS, I might as well minify the HTML too, using
[clj-html-compressor](https://github.com/Atsman/clj-html-compressor). That had a
noticeable effect too, although not as much as the CSS minification, I only
saved about *200* bytes on the index page. More on longer posts, but at most a
kilobyte. Well worth the trouble in the end.

But there are visible changes too, apart from the removal of syntax
highlighting: I made the font size bigger on the desktop, but left it unchanged
everywhere else. I did this because desktop resolutions are typically bigger
than tablets and phones, and the default font size was too small on my screens.

I also changed the main header, which used some special styling, drop shadows,
and colors not used elsewhere on the site. It stood out, looked different, and I
found it too distracting. So it is now styled as normal text, only a tiny bit
bigger, and using a monospace font. It stands out less, and as such, is less
distracting. But it is still there to serve as a way back to the root of the
site.

I also moved the article meta-data (tags and date) to be closer to the title,
and removed the dimming. It should be more accessible now.

There is still no JavaScript, and the CSS I have is even smaller, the markup
trying to get out of your way, letting you enjoy the content itself. At least, I
hope it does that, because ultimately, that's my goal: to have something easy
and pleasant to consume, something... not too remarkable as far as looks go, so
the focus can be on what's important: the content. The better the UI stays out
of the reader's way, the happier I am.

I suppose I should advertise it better that the blog here has an [RSS
feed](/blog/atom.xml) with full contents... Perhaps I'll put that into the
footer somewhere!
