---
title: "Chrysalis Progress Report #3"
date: 2018-11-13 11:15
tags: [Hacking, Keyboard, keyboardio, Chrysalis]
---

(This is a *draft*!)

It's [been a while][blog:chrysalis-2] I wrote a progress report, yet, there's so
much to share! Many, many things happened in the 18 months since the last
update, some good, some bad. This report will not be a completely accurate and
through account of those months, but rather a summary. Lets start with the most
glaring fact: Chrysalis is still not ready for a beta. But it is closer than
ever was before.

Before you continue, be aware that I'm still not a front-end guy, a grumpy one
at that. There is some mild dissatisfaction and complaining involved in the
paragraphs below. Thankfully there's a way to cheer me up, read the end to see
how!

 [blog:chrysalis-2]: /blog/2017/05/17/chrysalis-progress-report-2/

<!-- more -->

One of the major pain points of Chrysalis development for me was that I was
doing it. I'm not a front-end guy. I know next to nothing about UI/UX, nor about
frameworks, libraries, I'm not even familiar with the tooling. That's why I went
with ClojureScript, so I'd have at least something familiar in the set. It was a
good choice at the time, but it had one huge problem: the number of people
willing and able to contribute to a ClojureScript project were few. That's not a
good outlook when your long term plan is to let others take over the project.
You see, I started Chrysalis because I felt there's a need for it, and noone
else was working on it. I never intended to develop and maintain it forever, the
goal has always been that the community will eventually take it over.
ClojureScript turned out to be a roadblock.

So a couple of months ago, after having let Chrysalis linger in limbo for many
more, the decision was made to rewrite it in JavaScript, because there are many
more people familiar with that language, so we can both attract new
contributors, and it will hopefully make it easier to pass the torch down the
road. This wasn't an easy decision. I'm still not a front-end person, and don't
intend to become one either. Previously I had ClojureScript to cling on to, but
now that's been pulled out from under me (by my own self, even!). I had to fight
with tooling (did I mention I'm not familiar with it?), and a language that's
quite far from my preferred ones. It was a rough start. However, there were a
number of lessons learned from the previous implementation of Chrysalis, all of
which are proving to be useful in this new iteration.

First of all, Chrysalis was too ambitious in its goals. It tried to be
everything, for everyone, and ended up being nothing useful to anyone. It tried
to support many different devices, flashing, LED theme editing, keymap editing,
debugging capabilities, a REPL. All in one package. This simply did not scale.
The new implementation sports a very different architecture, and is developed
rather differently too. While the ClojureScript implementation was all in one
repository (with the goal of eventually splitting it up), the JavaScript rewrite
started off as a few tiny libraries I can build an UI on top of. The older
version was supposed to support many keyboards with the same application, with
the new architecture, we expect vendors to do their own bundles, to ship their
own UI on top of the same building blocks. This way they all have complete
control over how it looks and behaves, and Chrysalis itself does not need to
support themeing or branding in any way. That's a huge relief. Of course, we
still want to share as much code as possible, so the common, reusable parts will
be in their own little place, separate from the bundle itself. I'm still working
out this part, mind you. Right now there are a few closely tied libraries, split
across a few repositories. It might make sense to pull them together into one
repository instead, but still keep them as separate packages. But I'd need to
lean yet another tool I'll never use ever again to do that, so it's not the top
of my priorities at the moment.

Second, the ClojureScript version tried to be well designed. But due to my lack
of experience with building such applications, this didn't quite happen. But the
code was more complex in places than it really needed to be. With the JavaScript
rewrite, the goal is to have something usable out the door as soon as possible.
It may look horrible under the hood (it does), it may not be the most efficient
(it isn't), nor would it follow best practices (it doesn't), but it will do what
most people want to do with it: allow one to remap keys. This was another
problem of the old implementation: key editing came after the LED editor. I
deemed it easier to implement LED theme editing (it is easier), but that came at
the cost of keymap editing lagging behind. I had people help me out there, and I
want to thank both James Cash and Simon-Claudius for their amazing work on the
old code base. They took it where I wasn't able to.

With the rewrite, the primary focus has been keymap editing. I'm happy to report
that it works. It's not perfect. It's not exactly pretty, or very friendly, but
it works, and is - at least in my opinion - usable. A quick demonstration is
shown in the video below:

<video controls width="960" height="516">
 <source src="/assets/asylum/images/posts/chrysalis-progress-report-3/chrysalis-demo.webm" type="video/webm">
 <source src="/assets/asylum/images/posts/chrysalis-progress-report-3/chrysalis-demo.mp4" type="video/mp4">
Your browser does not appear to support the `video` tag.
</video>

It was a long road that led to this, and it doesn't look have many of the
features the old Chrysalis did, but on the other hand, it sports a usable keymap
editor, which works with the upstream firmware (from git master, the factory
firmware doesn't have the necessary bits yet) out of the box. It is at a stage
where I feel comfortable editing my own keymap with it: the last few changes I
did (adding a `+` and a `=` key to my numpad area), I did with Chrysalis first
to try them out, and only added them to my sketch after. I used to implement
changes in my sketch, copy the keymap over to EEPROM with a tool, and try then.
Now I can try a bunch of things without having to do that dance, and the feeling
is liberating.

## Closing words

In the intro I warned you, my dear reader, that this is a slightly grumpy post.
If you read this far, you now have a better idea why I'm grumpy: I'm working in
a field I have no experience with, and no desire to stay there long term. I also
hinted at a way to cheer me up, if you'd like: contribute! Make my life easier
by letting me work on things I do better and enjoy much more (the firmware), by
helping push Chrysalis forward. You don't need to be a wizard, or a React and
frontend guru (but that certainly helps, and if you are, here, have the reins!),
no. If you can give Chrysalis a try, submit issues, ideas, that helps a lot too.
Critique my code! Tell me how to change the looks so it becomes friendlier! Tell
me what would it take to make the application useful to **you**! If you are up
for it, I'm more than happy to accept pull requests too. There is plenty of
things in there that could be improved, big and small, enough to do for all
levels of experience.

Head over to the [chrysalis bundle][chrysalis:bundle], and have a go at it.

 [chrysalis:bundle]: https://github.com/keyboardio/chrysalis-bundle-keyboardio
