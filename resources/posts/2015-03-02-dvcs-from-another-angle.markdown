---
title: "Distributed Version Control from another angle"
date: 2015-03-02 17:00
tags: [Technology, Rants, .planet.debian]
---

I've been using distributed version control systems for a good while,
started with [TLA][tla] back in 2001, when it was still a bunch of
shell scripts and was simply called "Arch". I jumped the [Git][git]
bandwagon quite early too, sometime around May 2005, I believe, though
it was only something to test and play with on the side: I didn't
migrate my projects over yet. There are many reasons why I preferred
these over the systems I used in the past (RCS and CVS; never
considered Subversion an option), most of those reasons have been
discussed at length over the years, and there's no reason to do so
again.

 [tla]: https://www.gnu.org/software/gnu-arch/
 [git]: http://git-scm.com/

The reason for this post, is because I read a blog post about why [Git
and the other DVCSes fall short][abandon-dvcs]. There are a number of
things in that article that I find... well, lets just say
misinformed. I'll try to explain why.

 [abandon-dvcs]: http://bitquabit.com/post/unorthodocs-abandon-your-dvcs-and-return-to-sanity/

<!-- more -->

## But here's the rub

> I have wanted to have the full history while offline a grand total of
> maybe about six times. And this is merely going down over time as
> bandwidth gets ever more readily available. If you work as a field
> tech, or on a space station, or in a submarine or something, then
> okay, sure, this is a huge feature for you. But for the rest of us, I
> am going to assert that this is not the use case you need to optimize
> for when choosing a VCS.

I'm neither a field tech, nor do I work on a space station or a
submarine. Yet, having the full history available locally has been
crucial for my work for at least the past six years. Initially, I
worked offline primarily, because my internet connection was
incredibly limited (slow, expensive and unreliable), yet, I had a
habit of committing small changes often. I needed a full clone to work
effectively, and conveniently. Throw-away branches and all that - I
needed them local, I didn't have the resources to push them online
every time I wanted a merge.

But now that I have fast and reliable internet, why do I need the full
history on my own computer? I do, because I do much more research than
I do development. The vast majority of my work between 2010 and 2012
involved reading histories, cherry picking and so on. About six hours
a day, I was reading the past. Having the full history locally is a
terrific aid when your job involves digging through it.

Between 2013 and 2014, the amount of history reading dropped
significantly, but I still worked with long-lived branches a lot,
branches where history matters, where having easy and fast access to
them is indispensable. I still had to find changes based on content
(not commit message, branch, date or anything - content) on a daily
basis. Hitting the network for each and every search would have wasted
not only tremendous bandwidth and processing power on the server, but
also a significant amount of time.

If you do more maintenance and research, or follow a good number of
branches (or forks), with the intent to merge and cherry-pick actively
from them, having all the histories available locally is a huge
improvement. I'm happy to pay the price of "wasting" disk space to
have this available. I couldn't work otherwise. Without full, local
history, the things I did for [syslog-ng][sng] would not have been
possible. No maintenance, none of the features I implemented, none of
the fixes I found. Why? Because I rely on the project's history to aid
me in learning the <em>why</em>s, not just the <em>how</em>s.

Thank you, but - perhaps unlike others - I like to learn from the
past, and prefer doing it efficiently.

 [sng]: https://www.syslog-ng.org/

## Say goodbye to blobs (or your sanity (and sometimes both))

> You know what kind of external binary asset management system works
> really well? Subversion. Turns out it works tolerably for source
> control, too. Maybe you should take a look at it.

Sure, some systems are better at managing binary assets. Git - and
usually DVCSes - are terrible at it. There's nothing stopping anyone
from using two systems: one for source, another for data. Mind you, I
am extremely happy with
[git-annex](https://git-annex.branchable.com/). I disagree with
Subversion being tolerable for source control. I'd turn this assertion
around instead: *You know what kind of system works really well for
source code? Git. Turns out it works tolerably as an external binary
assest management system, too. Maybe you should take a look at it.*

But then, I rarely need to work with blobs, so I'm likely not the best
person to disagree here.

## Say goodbye to sane version synchronization

Okay, so this part of the article is ill-titled, I believe. Large
repositories and version synchronisation are two different, not
necessarily related topic. Granted DVCSes may not be the best fit for
large repositories, but neither are centralised version control
systems, based on my past experience. I had the misfortune of working
with a roughly 3-4Gb (checked out) project, with extensive history,
managed with a centralised version control system, without local
history. It was terrible (and blame was so slow and expensive that we
were told never to use it, because it crashed the server). While the
system allowed us to have a reasonable overview over the whole
project, but it utterly failed at allowing us to work at the component
level. How do I see which version of component X we had in project
release A? Or the version of component Y we had two weeks ago? Or what
if we have component M, made up from components J and K, and belonging
to component Z? Good luck managing that in a single repository.

With TLA, we had configs, which were a bit cumbersome to use, but
captured most of the essential parts of managing component bundling
and hierarchical versioning far better than any other solution I've
seen so far. I've yet to find an acceptable solution, but I'm
convinced it will not have anything to do with centralised
systems. You can't capture the delicate relations there.

## Say what again, I dare you, I double dare you

> All of the above might be almost tolerable if DVCSes were easier to
> use than traditional SCMs, but they aren’t.

I'm sorry, but have you tried managing a decently sized project, with
a large history, built from multiple components with a traditional
SCM? Because that is a colossal pain in the backside.

Does Git's UI suck? Yes. But so did CVS's and Subversion's. If you
rely on the command-line tools, you're going to feel the pain. That is
why you use the IDE integration you surely have, which makes the most
common tasks a breeze. That may sound like a git-apology, but
remember, we did the same with every other version control system,
because smart people prefer integration, and want the computer to work
**for** them.

> And yet, when someone dares to say that Git is harder than other
> SCMs, they inevitably get yelled at, in what I can only assume is a
> combination of Stockholm syndrome and groupthink run amok by
> overdosing on five-hour energy buckets.

If you feel the need to understand the innards of Git - then yes, it
is harder than traditional SCMs. It is distributed, and different than
what you're used to. If you do not have prior knowledge though, the
difference in learning the internal working of Git and - say -
Subversion all that different. And if you do not care how it works
down below (and I dare say, a lot of developers should not, and do not
care), then there is nothing to learn, apart from your IDE
integration, which ain't different from a traditional SCM's.

Mind you, I found it both interesting, and beneficial to learn Git's
inner workings. Not only because it allowed me to make better use of
my VCS, but because it taught me a bunch of things I could make use of
elsewhere. I learned nothing from traditional SCMs.

## Do you hear the people sing, singing the song of angry men

> There is one last major argument that I hear all the time about why
> DVCSes are superior, which is that they somehow enable more
> democratic code development.

The reason Git and other tools make code contribution easier is not
GitHub pull requests (although, for drive-by contributions those are
[pretty useful too][gh:closed]). The reason they make contribution
easier is because you have your own copy of the full history. You can
create your own branches *easily*, you can work with it as if you were
the owner. For one-off contributions, that is no real advantage, I'll
give you that. It takes more effort to fork & do a PR on GitHub, than
to send a patch, I'll also agree with that.

 [gh:closed]: https://github.com/search?l=&q=type%3Apr+state%3Aclosed+updated%3A%3E2014-12-31&ref=advsearch&type=Issues&utf8=%E2%9C%93

But once you start working with a project for more than a few patches,
when you want to work with its history too, once you have long-lived
branches, the local copy quickly becomes a great asset. Once you want
to submit more than a few changes, you won't have to repeat a all
steps. And not only that: since you have the full history, merging
upstream changes (or just cherry picking from them), rebasing your
work, and many other tasks become simpler.

That is what it buys you, that is why it makes contribution
easier. GitHub pull requests are an icing over the cake at best (and
there are a good number of cases where the icing is completely
unnecessary, because it doesn't go well with the cake).

# In closing

In closing, I'd like to highlight that different projects have
different requirements. For example, I understand why Facebook, Google
and a number of others use a single, huge repository. I happen to hate
that setup, and would find working in such an environment
horrible. But luckily, I don't work for those companies, and they
don't listen to me, either, so everyone's happy. That does not mean I
would go out and tell them they're wrong to use such a centralised,
single-repository setup. For their use, in their situation, for their
requirements, that may be the better option. For mine, not so much.

In the end, what we need to realise is that there is no single tool
that will fit all needs. There are tasks where Git is a terrible
choice, but that does not make it terrible for all cases. Similarly,
there are cases where Subversion is superior (I assume - I didn't have
the misfortune of meeting with such a case).

Instead of bashing the DVCS world, and assuming the Big Company use
cases are all that matter, and that everyone uses the tools the same
way, accept that some use it to achieve things you didn't think of,
jobs you didn't consider.
