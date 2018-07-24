---
title: "GitHub vs mailing lists, from another perspective"
date: 2018-07-24 14:45
tags: [Rants, Technology]
---

The other day, I made the mistake of getting involved in a kind of flamewar,
that revolved around GitHub versus an email-driven workflow. As can be safely
inferred, I do not subscribe to the email-driven workflow idea. There seem to be
fundamental disagreements, and I think it's worth a shot to show my side of
things.

In this first part, I will reflect on an article titled "[Mailing lists vs
Github][begriffs:mailing-list-vs-github]", which should have been titled
"Mailing lists vs the GitHub web UI", but I digress. Lets cut to the chase!

 [begriffs:mailing-list-vs-github]: https://begriffs.com/posts/2018-06-05-mailing-list-vs-github.html

<!-- more -->

## Critique

> Code changes are proposed by making another Github-hosted project (a “fork”),
> modifying a remote branch, and using the GUI to open a pull request from your
> branch to the original.

That is a bit of a simplification, and completely ignores the fact that GitHub
has an API. So does GitLab and most other similar offerings. You can work with
GitHub, use all of its features, without ever having to open a browser. Ok,
maybe once, to create an OAuth token.

> Email sub-threads allow specialized discussion about different aspects or
> sections of the code. A linear Github-style discussion would mix those
> conversations.

I'm not a fan of deep threads. If something has many sub-threads, or the thread
goes too deep, for me that indicates that there's a deeper problem. That the
patch may need to be split up, or discussed, or even re-designed first. A
certain level of discussion is useful, but once it splits up into longer
sub-threads, it becomes way too easy to loose sight of the whole picture.

GitHub's flat discussion discourages this, and I find that to be beneficial most
of the time. There are times when I don't, but that happens rarely enough that I
can live with it.

> On Github, comments continually change. They become “outdated” and disappear
> when attached to a line that has been changed. Same for the commits, which
> vanish after a force-push to the pull request branch. In an email thread, by
> contrast, the original messages and proposed changes remain for comparison
> with later messages and patches.

On mailing lists, when you "force push", and start a new thread (or subthread at
best) with the new version of a patchset, the history is similarly hard to use.
You can link the new patchset to the older discussion, but comparsion has to be
done manually. Same applies to GitHub. I don't see much of a difference here.

If you send updated patches, that's the same as pushing new commits on GitHub,
which appear clearly, and still keep older comments available. Some may get
marked outdated, but I consider that a good thing. I don't want to see typically
irrelevant, already-addressed comments by default. I can still look at them if I
want to, mind you.

> Furthermore, patches from multiple authors can’t mix in a Github pull request.

Except, you can, and there are a number of ways to accomplish that. You can
[allow maintainers of the upstream repo][gh:maintainer-edit] to edit your pull
request. You can also give permission to other to collaborate with you on a
repo, and push to your branches. The latter does give a bit wider access than
one might wish, but it is an option. Furthermore, others can open pull request
against the branch you used to open yours from. When you merge those, their
commits will be added to the pull-request you opened.

 [gh:maintainer-edit]: https://help.github.com/articles/allowing-changes-to-a-pull-request-branch-created-from-a-fork/

This last one fits the GitHub pull-request model best, and when you are
comfortable with working with GitHub, it is not any more complex to work this
way than with e-mail. Instead of applying patchsets, you merge PRs. Both are
supported by integrations, the difference in complexity is none.

> The pull requester must turn those comments into commits on the branch if he
> or she wants to incorporate the suggested changes.

Or the commenter can send a pull-request against the branch the PR in question
was opened from. You can have a discussion there, to discuss the changes (much
like a subthread on the mailing list). You can even link the two together, and
navigate between the two.

> Another nice effect is that other people can carry the patch to the finish
> line if the original author stops caring or being involved.

On GitHub, if the original proposer goes MIA, anyone can take the pull request,
update it, and push it forward. Just like on a mailing list. The difference is
that this'll start a new pull request, which is not unreasonable: a lot of time
can pass between the original request, and someone else taking up the mantle. In
that case, it can be a good idea to start a new thread, instead of resurrecting
an ancient one.

> Patch Format

This is indeed a case where an e-mail based workflow is more flexible. Yet, does
it matter? In either case, you can just apply the patch, and create diffs in
whatever format, and was much context as you wish. You'll see not just the
parts, but the whole thing in context. You can do this with GitHub, you can do
this with email. I've been doing it ever since I started working with patches,
and it is mighty convenient.

Use the tools you have, if you need and want to. You are not limited to the
GitHub UI. Nor are you limited to your MUA, either. There are tools outside of
those, tools you can integrate with. Use them.

Don't treat patches as discrete items. Apply them, and have a look at the whole.
That gives you all the context you need, in whatever format you desire. I found
this to be a very powerful workflow, one that is also easier to work with than
patches, because the tree is easier to navigate this way. You can use tools that
understand the code, to jump to definitions (that's a lot harder when you view a
diff). You can apply code formatting, use refactoring tools to better understand
the code - and then undo it all if so desired. You can edit and change code as
you review, and work with the code the same way you normally do, using all the
help your IDE can give you.

> Patch/Discussion mix

You can link issues and pull requests on GitHub. IDEs with good integration
allow easy navigation too, akin to navigating an e-mail thread. There are
projects out there that separate bug report and development lists, which suffer
from the same issue as GitHub's issue/pull request split.

This is not an inherent advantage of mailing lists. It's an advantage of not
separating the two.

> While web apps deliver a centrally-controlled user interface, native
> applications allow each person to customize their own experience.

GitHub has an API. There are plenty of IDE integrations. You can customize your
user-experience just as much as with an email-driven workflow. You are not
limited to the GitHub UI.

> Open protocols like SMTP encourage a proliferation of clients.

There are all kinds of GitHub clients, each with their own added
functionalities, each with their own set of features. Just like email clients,
you have a wide selection to choose from.

> Mail clients provide ways to mark a message important, or set it back as unread.

You can build something like that on top of the GitHub API. I believe that is
what [Octobox][octobox] have been doing.

 [octobox]: https://octobox.io/

Again, you are not limited to the GitHub UI.

> Some people script their mail client so that they can apply patches with a
> keyboard shortcut, others go minimalist, and still others even use webmail.
> Each person is different, and so is their software, but the nature of the
> mailing list allows them all to work together.

Nothing is stopping anyone from doing the same with GitHub. For example, I use
Emacs and Magit to work with GitHub, never leaving my IDE. Others I collaborate
with use the GitHub UI. Others use vim and other tools I have no clue about.
Some others use Visual Code Studio or Atom. Or GitHub Desktop. Or in some cases,
e-mail. We all work on the same projects.

> Another area of control is the ability to search and interact with a mailing
> list while offline.

You can do that with GitHub too. With limitations, and a bit of preparation, but
similar limits apply to working with a mailing list while offline, too.

> Github requires connectivity to review issues and pull requests.

No. You can [fetch the pull requests][gh:fetch-prs] you want to review before
going offline.

 [gh:fetch-prs]: https://help.github.com/articles/checking-out-pull-requests-locally/

You can also use the API to cache issues, and schedule updates.

A lot of people don't sync their mail locally either, so will have to do some
preparations when going offline too. Those who do, can also set up automatic PR
syncing too.

Again, you are not limited to what the GitHub UI offers. You can use additional
tools, the same way you do when using an email-driven workflow. You aren't
limited to what you MUA has to offer.

> With a native email client you can review all emails and attachments offline.
> You can even send replies to messages offline and the client will queue them
> until internet access becomes available.

This assumes that one synced e-mail locally. Many people use IMAP or similar
protocols, and typically don't sync for offline use. Doing so is most often a
conscious decision. When you do decide that, you can also pull down PRs and
issues first. There are existing tools to aid you in that.

> Tools can work together, rather than having a GUI locked in the browser.

GitHub has an API. Granted, it is not an RFC, and you are at the mercy of GitHub
to continue providing it. But then, you are often at the mercy of your email
provider too.

> What a twist of history, then, that users of git chose Github… a centralized
> host granting free licenses for open source projects, and requiring projects
> to store their metadata on company servers.

Except they do not do that. They provide you an API to look at the additional
meta-data, to build custom integrations on top of it.

You can host your issues elsewhere. You can even use GitHub as a mirror only.
You only need to host your metadata there, if you want to use the features
GitHub provides. You are not in any way required to do that.

You can even opt to do both! You can accept GitHub issues, pull request, **and**
have a mailing list! There is nothing stopping you from doing that.

> Github can legally delete projects or users with or without cause.

Whoever is hosting your mailing list archives, or your mail, can do the same.
It's not unheard of.

> An author can download the source release tarball, make changes in the copy,
> capture the diff, and email it.

You can do the same with a project using GitHub. Send the e-mail to one of the
maintainers. You are at the mercy of the recipients to deal with the patch
appropriately, but that's the same situation when they use CVS/Mercurial/etc,
and you send a bare patch. They still have a little work to do to fit it in
their workflow.

But GitHub itself does not prevent accepting e-mail.

> Sending and applying patches cuts out busywork like cleaning up remote
> branches after merge, or creating a local branch in the forked repo in
> preparation for a pull request.

All of this can be easily automated away. Besides, if you are a long-term
contributor, creating a local branch is a good idea *anyway*, GitHub or not.

> For comparison, I remember teaching a group of new programmers how to use
> Github, and was conscious at the time of all the weird steps I asked them to
> do.

For comparsion, when my Wife (a garden engineer, not a tech-savvy person) wanted
to contribute a little during Hacktoberfest, she found the "Edit file" button on
the UI, and went further from there on her own. No weird steps, just edit a
file, submit changes, done.

She'd never be able to send an patch by e-mail.

> There’s also less busywork for finished communications. There aren’t things to
> “clean up” like abandoned pull requests, merged branches, or issues to mark
> closed. The replies just stop on those threads.

I'd rather see an explicit marker that an issue is resolved or a PR is merged,
but hey, that's me, who doesn't like to dig this information out of random
e-mail threads. Discoverability is important.

> PGP provides a further guarantee of identity, verified through a decentralized
> web of trust.

You can sign commits and tags while using GitHub. You can't sign comments, last
I checked, but you can respond to them by email, which is signed. It might look
a bit weird on the UI, but hey.

> Perhaps this article can start these developers on the path to rediscovering
> the care and engineering that went into classic email clients (“MUAs” as they
> are called).

Or perhaps there are people who'd rather not deal with email, because they have
purpose-built tools that aid them better than a MUA and tools built around that
workflow would.

## Exceptions

There are certainly projects where the GitHub model just doesn't work, the Linux
kernel being one particularly great example of that. What works for one project,
may not be the best choice for another. I assert that most projects are not the
Linux kernel, and aren't anywhere near that level. As such, modeling your
workflow on something with vastly different needs may not be the best course of
action.

## Next up

In the next installment of these series, I will explain my workflow. No
comparisons, just examples of how I, an allegedly seasoned engineer of sorts,
uses the tools he has at hand. It's a workflow of a power user. One who prefers
working with APIs instead of email. It may include a little bit of history.

After that, I'll have a look at a few other posts that campaign for an
email-driven workflow, and see where that takes us.
