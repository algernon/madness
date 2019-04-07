---
title: "Git workflows"
date: 2019-04-07 23:45
tags: [Hacking, Rants, Technology]
---

To make things clear, I'll start this post with a strongly held opinion: E-mail based git workflows are almost always stupid, because in the vast majority of cases, there exists a more reliable, more convenient, easier to work with workflow, which usually requires less setup and even less sacrificial lambs. I've tried to explain this a few times on various media, but none of those provide the tools for me to properly do so, hence this blog post was born, so I can point people to it instead of trying to explain it - briefly - over and over again.

I originally wanted to write a long explanation comparing various workflows: GitHub web UI vs GitHub with loose IDE integration vs GitHub with tight IDE integration vs E-mail-based variations. However, during this process I realised I don't need to go that far, I can just highlight the shortcomings of e-mail with a few examples.

<!-- more -->

One of the reasons I most often hear in support of an e-mail-based workflow is that git ships with built-in tools for collaborating over e-mail. It does not. It ships with tools to send email, and tools to process e-mail. There's no built-in tool to bridge the two, it is entirely up to you to do so. Collaboration is *not* about sending patches into the void. Collaboration includes receiving feedback, incorporating it, change, iteration. Git core does not provide tools for those, only some low-level bits upon which you can build your own workflow.

Git is also incredibly opinionated about *how* you should work with e-mail: one mail per commit, nicely threaded, patches inline. But that's not the only way to have an e-mail based workflow: [PostgreSQL][pg:patch] for example uses attachments and multiple commits attached to the same e-mail. This isn't supported by core git tools, even though it solves a whole lot of problems with the one inline patch per commit method - more about that a few paragraphs below.

 [pg:patch]: https://wiki.postgresql.org/wiki/Submitting_a_Patch

So what's the problem with e-mail? First of all, in this day and age, delivery is not reliable. This might come as a surprise to proponents of the method, but despite the SMTP protocol being *resilient*, it is not *reliable*. It will keep retrying if it gets a temporary failure, yes. But that's about the only thing it guarantees, that it keeps trying. Once we add spam filters, greylisting, and a whole lot of other checks one needs in 2019 to not drown in junk, there's a reasonable chance that something will, at some point, go horribly wrong. Let me describe a few examples from personal experience!

At one time, I sent a 10-commit patch series to a mailing list. I wasn't subscribed at the time, and the mailing list software silently dropped every mail: on the SMTP level, it appeared delivered, but it never made to the list. I had no insight into why, and had to contact a list admin to figure it out. Was it a badly configured server? Perhaps, or perhaps not. Silently dropping junk makes sense if you don't want to let the sender know that you know they're sending junk. Sometimes there are false positives, which sucks, but the administrators made this trade-off, who am I to disagree? Subscribing and resending worked flawlessly, but this introduced days of delay and plenty of extra work for both me and the list admins. Not a great experience.

Another time, back when greylisting was new, I had some of my patches delayed for hours. This isn't a particularly big deal, as I'm in no rush. It becomes a big deal when patches start arriving out of order, sometimes with hours between them because I didn't involve enough sacrificial lambs to please the SMTP gods. When the first feedback you get is "where's the first patch?", even though you sent it, that's not a great experience. I've even had a case where a part of the commit thread was rejected by the list, another part went through. What do you do in this case? You can't just resend the rejected parts unchanged. If you change them to please the list software, that pretty much invalidates the parts that did get through - and nothing guarantees that they'll all get through this time, either.

From another point of view, as a reviewer, receiving dozens of mail in a thread for review isn't as easy to work with as one would like. For example, if I want to send feedback on two different - but related - commits, then I have to either send two mail, as replies to the relevant commits, or merge the two patches in one email for the purpose of replying. In the second case, it's far too easy to loose track of what's where and why.

With these in mind, I'm sorry to say, that e-mail is not reliable. E-mail delivery is not reliable. It is resilient, but not reliable (see above). The contents of an e-mail are fragile, change the subject, and `git am` becomes unhappy. You want to avoid bad MUAs screwing up patches? Attach them! Except the default git tooling can't deal with that. There are so many things that can go wrong, it's not even funny. Many of those things, you won't know until hours, or days later. That's not a reliable way to work.

Sending patches as attachments, in a single mail, solves most of these problems: if it gets rejected, all gets rejected. If it gets delayed, the whole thing gets delayed. Patches never arrive out of order and with delays. Reviewing multiple commits becomes easier too, because all of them are available at hand, without having to build tooling to make them available. But patches as attachments aren't supported by core git tools. Even in this case, there's plenty more you can't easily do, that patches lack: plenty of meta-information.

You can't easily see more context than what the patch file provides. You can, if you apply the patchset and look at the tree, but that's not something the default tools provide out of the box. It's not hard to do that, not hard to automate it, but it doesn't come out of the box. To navigate the source code at a given time, you have to apply the patches too. There are plenty of other things where one wants more information than what is available in a patch. Let me illustrate!

Lets look at an average project at an average forge (GitHub, GitLab, Gitea, etc)! They allow expanding the context easily. You can easily pull the patchset to your local tree. You can browse issues, existing pull requests with ease, without having to wade through a mailing list and separate signal from the noise. You can do a whole lot more, because forges provide tight integration, while e-mail delegates any integration tasks to you. Integration and good tooling is good. We want computers to do the boring stuff for us, they should make our lives easy, or work convenient and efficient. None of the e-mail-based workflows I saw over the past two decades provided the convenience the forges do. Not even close. If you enjoy switching between your MUA, your editor, and the command-line, be my guest (and you can still work with a forge that way). I prefer to stay in my IDE, with everything at my fingertips: I don't need to switch to a different application with different key bindings, I can do **everything** from the IDE of my choice. I can do this because forges do most of the integration, and they provide APIs my IDE can use.

Want to see what's possible with a forge and tight integration? Look no further that [magit/forge][magit/forge]:

 [magit/forge]: https://github.com/magit/forge

Overview of a project:

 [![Forge overview][forge/overview.thumb]][forge/overview]

Viewing an issue:

 [![Viewing an issue with Forge][forge/issue.thumb]][forge/issue]

Viewing a pull request:

 [![Viewing a pull request with Forge][forge/pr.thumb]][forge/pr]

 [forge/overview]: /assets/asylum/images/posts/git-workflows/forge-overview.png
 [forge/issue]: /assets/asylum/images/posts/git-workflows/forge-issue.png
 [forge/pr]: /assets/asylum/images/posts/git-workflows/forge-pr.png
 [forge/overview.thumb]: /assets/asylum/images/posts/git-workflows/forge-overview.thumb.png
 [forge/issue.thumb]: /assets/asylum/images/posts/git-workflows/forge-issue.thumb.png
 [forge/pr.thumb]: /assets/asylum/images/posts/git-workflows/forge-pr.thumb.png
