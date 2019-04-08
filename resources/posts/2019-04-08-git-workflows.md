---
title: "On Git workflows"
date: 2019-04-08 08:45
tags: [Hacking, Rants, Technology]
---

To make things clear, I'll start this post with a strongly held opinion: E-mail based git workflows are almost always stupid, because in the vast majority of cases, there exists a more reliable, more convenient, easier to work with workflow, which usually requires less setup and even less sacrificial lambs. I've tried to explain this a few times on various media, but none of those provide the tools for me to properly do so, hence this blog post was born, so I can point people to it instead of trying to explain it - briefly - over and over again. I wrote about [mailing lists vs GitHub][blog:ml-vs-gh] before, but that was more of an anti-GitHub rebuttal than a case against an e-mail workflow.

 [blog:ml-vs-gh]: /blog/2018/07/24/on-git-github-and-email/

I originally wanted to write a long explanation comparing various workflows: A Forge's web UI vs Forge with loose IDE integration vs Forge with tight IDE integration vs E-mail-based variations. However, during this process I realised I don't need to go that far, I can just highlight the shortcomings of e-mail with a few examples, and then show a glimpse into the power a Forge can give us.

<!-- more -->

One of the reasons I most often hear in support of an e-mail-based workflow is that git ships with built-in tools for collaborating over e-mail. It does not. It ships with tools to send email, and tools to process e-mail sent by itself. There's no built-in tool to bridge the two, it is entirely up to you to do so. Collaboration is *not* about sending patches into the void. Collaboration includes receiving feedback, incorporating it, change, iteration. Git core does not provide tools for those, only some low-level bits upon which you can build your own workflow.

Git is also incredibly opinionated about *how* you should work with e-mail: one mail per commit, nicely threaded, patches inline. But that's not the only way to have an e-mail based workflow: [PostgreSQL][pg:patch] for example uses attachments and multiple commits attached to the same e-mail. This isn't supported by core git tools, even though it solves a whole lot of problems with the one inline patch per commit method - more about that a few paragraphs below.

 [pg:patch]: https://wiki.postgresql.org/wiki/Submitting_a_Patch

So what's the problem with e-mail? First of all, in this day and age, delivery is not reliable. This might come as a surprise to proponents of the method, but despite the SMTP protocol being *resilient*, it is not *reliable*. It will keep retrying if it gets a temporary failure, yes. But that's about the only thing it guarantees, that it keeps trying. Once we add spam filters, greylisting, and a whole lot of other checks one needs in 2019 to not drown in junk, there's a reasonable chance that something will, at some point, go horribly wrong. Let me describe a few examples from personal experience!

At one time, I sent a 10-commit patch series to a mailing list. I wasn't subscribed at the time, and the mailing list software silently dropped every mail: on the SMTP level, it appeared accepted, but it never made to the list. I had no insight into why, and had to contact a list admin to figure it out. Was it a badly configured server? Perhaps, or perhaps not. Silently dropping junk makes sense if you don't want to let the sender know that you know they're sending junk. Sometimes there are false positives, which sucks, but the administrators made this trade-off, who am I to disagree? Subscribing and resending worked flawlessly, but this introduced days of delay and plenty of extra work for both me and the list admins. Not a great experience. I could have read more about the contribution process and subscribe in advance, but as this was a one-off contribution, subscribing to the list (a relatively high-volume one) felt like inviting a whole lot of noise for no good reason. Having to subscribe to a list to meaningfully contribute is also a big barrier: not everyone's versed in efficiently handling higher volumes of e-mail (nor do people need to be).

Another time, back when greylisting was new, I had some of my patches delayed for hours. This isn't a particularly big deal, as I'm in no rush. It becomes a big deal when patches start arriving out of order, sometimes with hours between them because I didn't involve enough sacrificial lambs to please the SMTP gods. When the first feedback you get is "where's the first patch?", even though you sent it, that's not a great experience. I've even had a case where a part of the commit thread was rejected by the list, another part went through. What do you do in this case? You can't just resend the rejected parts unchanged. If you change them to please the list software, that pretty much invalidates the parts that did get through - and nothing guarantees that they'll all get through this time, either.

In all of these cases, I had no control. I didn't set the mailing lists up, I didn't configure their SMTP servers. I did everything by the book, and yet...

From another point of view, as a reviewer, receiving dozens of mail in a thread for review isn't as easy to work with as one would like. For example, if I want to send feedback on two different - but related - commits, then I have to either send two mail, as replies to the relevant commits, or merge the two patches in one email for the purpose of replying. In the second case, it's far too easy to loose track of what's where and why.

With these in mind, I'm sorry to say, but e-mail is not reliable. E-mail delivery is not reliable. It is resilient, but not reliable (see above). The contents of an e-mail are fragile, change the subject, and `git am` becomes unhappy. You want to avoid bad MUAs screwing up patches? Attach them! Except the default git tooling can't deal with that. There are so many things that can go wrong, it's not even funny. Many of those things, you won't know until hours, or days later. That's not a reliable way to work.

Sending patches as attachments, in a single mail, solves most of these problems: if it gets rejected, all gets rejected. If it gets delayed, the whole thing gets delayed. Patches never arrive out of order and with delays. Reviewing multiple commits becomes easier too, because all of them are available at hand, without having to build tooling to make them available. But patches as attachments aren't supported by core git tools. Even in this case, there's plenty more you can't easily do, becaise there's something that patches lack: plenty of meta-information.

You can't easily see more context than what the patch file provides. You can, if you apply the patchset and look at the tree, but that's not something the default tools provide out of the box. It's not hard to do that, not hard to automate it, but it doesn't come out of the box. To navigate the source code at any given time of its history, you have to apply the patches too. There are plenty of other things where one wants more information than what is available in a patch.

But I said in the opening paragraph that:

> there exists a more reliable, more convenient, easier to work with workflow, which usually requires less setup and even less sacrificial lambs

So what is this magical, more reliable, more convenient, easier to work with workflow? Forges. Forges like GitHub, GitLab, Gitea, and so on. You may have been led to believe that you need a browser for these, that a workflow involving a forge cannot be done without switching to a browser at some point. This is true: you will usually need a browser to register. However, from that point onwards, you do not, because all of these forges provide powerful APIs. Powerful APIs that are much easier to build good tooling upon than email. Why? Because these APIs are purpose-built, their reason for existence is to allow tooling to be built upon them. That's their job. When you have purpose-built tools, those will be easier to work with that something as generic and lax as e-mail. With this comes that Forges do most of the integration required for our workflow. We only have to build one bridge: one between the API, and our IDE of choice.

As an example, lets look at [magit/forge][magit/forge], an Emacs package that integrates forge support into Magit (the best git UI out there, ever, by far)!

 [magit/forge]: https://github.com/magit/forge

 [![Forge overview][forge/overview.thumb]][forge/overview]

We see pull requests, issues, recent commits, and the current branch in one place. Want to look at an issue? Navigate there, press enter, and voila:

 [![Viewing an issue with Forge][forge/issue.thumb]][forge/issue]

Easy access to the whole history of the issue. You can easily quote, reply, tag, whatever you wish. From the comfort of your IDE.

Pull-requests? Same thing, navigate there, press enter:

 [![Viewing a pull request with Forge][forge/pr.thumb]][forge/pr]

You have easy access to all discussions, all the commits, all the trees, from the comfort of your IDE. You do not need to switch to another application, with different key bindings, slightly different UX. You do not need to switch to a browser, either. You can do everything from the comfort of your *Integrated Development Environment*. And this, dear reader, is awesome.

The real power of the forges is not that they provide a superior user experience out of the box - they kinda do anyway, since you only have to register and you're good to go. No need to care about SMTP, formatting patches, switching between applications and all that nonsense. The web UI is quite usable for a newcomer. For a power-user - no; using a browser for development would be silly (alas, poor people stuck using Atom, VS Code or Chromebooks). Thankfully, we do not have to, because all of the forges provide APIs, and many IDEs also provide various levels of integration.

But what these Forges provide are not just easy access to issues, pull-requests and commits at one's fingertips. They provide so much more! You see, with a tightly integrated solution, if you want to expand the context of a patch, you can: it's already right there, a single shortcut away. You can easily link to parts of the patchset, or the code, and since they'll be links, everyone reading it will have an easy, straightforward way to navigate there. You can reference issues, pull-requests from commit messages, other issues or pull-requests - and they'll be easy to navigate to, out of the box. A forge binds the building blocks together, to give us an integrated solution out of the box.

Forges make the boring, tedious things invisible. They're not exclusive owners of the code either: you can always drop down to the CLI and use low-level git commands if need be. This is what computers are meant to do: help us be more efficient, make our jobs more convenient, our lives easier. Thankfully, we have Forges like GitLab, Gitea and others, that are open source. We aren't even forced to trust our code, meta-data and workflows to proprietary systems.

However, forges aren't always a good fit. There are communities that wouldn't work well with a Forge. That's ok too. But in the vast majority of cases, a forge will make the life of contributors, maintainers and users easier. So unless you're the Linux kernel, don't try to emulate them.

 [forge/overview]: /assets/asylum/images/posts/git-workflows/forge-overview.png
 [forge/issue]: /assets/asylum/images/posts/git-workflows/forge-issue.png
 [forge/pr]: /assets/asylum/images/posts/git-workflows/forge-pr.png
 [forge/overview.thumb]: /assets/asylum/images/posts/git-workflows/forge-overview.thumb.png
 [forge/issue.thumb]: /assets/asylum/images/posts/git-workflows/forge-issue.thumb.png
 [forge/pr.thumb]: /assets/asylum/images/posts/git-workflows/forge-pr.thumb.png
