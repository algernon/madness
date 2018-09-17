---
title: "Finding a Continous Integration solution"
date: 2018-09-17 21:15
tags: [Hacking, Technology, CI]
---

I have been happily using [Travis][ci:travis] for years, it worked quite well
for the projects I had on GitHub. But ever since I started moving to my own,
[self-hosted Gitea][a:gitea], I had this nagging feeling that I should find a
different continous integration solution. One that I can host myself, because
I'd like to be less and less dependent on other services, and self-host what I
can. It feels safer that way, and also gives me a lot more control.

 [ci:travis]: https://travis-ci.com/
 [a:gitea]: https://git.madhouse-project.org/

Therefore, the other day I went looking for a CI service, that has just the
right features:

 - Can be self-hosted.
 - Open source (or better yet, free software).
 - Lightweight.
 - Can get it up and running within an hour.
 - Language & build system agnostic.
 - Configuration as code.
 - Can work together with my existing [Gitea][gitea] install.
 - Built on containers, preferably.
 - Reasonable APIs I can use to build things on top of it.

 [gitea]: https://gitea.io/

I've looked at many, but only one emerged as winner: [Drone][ci:drone]. Let me
explain why the above features are necessary, and how Drone came out winning.

 [ci:drone]: https://drone.io/

<!-- more -->

# Ruling products out

### Self hosted

I want to self-host as much of the things I rely on as possible, because it
gives me better control of my things. I will be in control of the whole
pipeline, and my data too.

This rules out hosted solutions like [Travis][ci:travis],
[Shippable][ci:shippable], etc.

 [ci:travis]: https://travis-ci.com/
 [ci:shippable]: https://www.shippable.com/

### Open source

If I'm going to host it myself, it is going to be open source, because I'm not
going to maintain non-free stuff on my servers. I want to be able to look inside
it, perhaps even tweak it to my needs (I have tweaked almost every service I
run, a little, and not just in configuration, but at the source-code level).

This rules out products like [TeamCity][ci:teamcity].

 [ci:teamcity]: https://www.jetbrains.com/teamcity/

### Lightweight

I have a reasonably small VPS, which already runs plenty of things. The last
thing I want is my CI eating up a lot of resources. Consequently, the service
I'm looking for is a CI/CD product, not one integrated with other things. I have
solutions for that already.

This rules out [GitLab CI/CD][ci:gitlab], because it's integrated with GitLab,
and that's a heavy beast. I already use Gitea for my git hosting needs,
installing GitLab beside it, just to get a CI/CD product would be stupid. This
also rules out [Phabricator][ci:phabricator], doubly so since their CI/CD
application is still very much experimental. It also rules out [GoCD][ci:gocd],
because their recommended RAM is what I have in total.

 [ci:gitlab]: https://about.gitlab.com/features/gitlab-ci-cd/
 [ci:phabricator]: https://secure.phabricator.com/book/phabricator/article/harbormaster/
 [ci:gocd]: https://www.gocd.org/

### Easy to get started with

I'm a lazy person. I don't like maintaining services, to be honest, I only do it
out of necessity. So if I do end up running something, it must be easy to do so.
If I have to install a gazillion of dependencies, or fight with the solution to
get it up and running, we're not going to be friends.

Similarly, if a product can be used to build a CI/CD solution with, but isn't
one in and of itself, we're not a good match, either.

These things rule out [Factor][ci:factor] and [Jenkins][ci:jenkins].

 [ci:factor]: http://www.factor.io/
 [ci:jenkins]: https://jenkins.io

### Language & build system agnostic

I use a whole host of languages, from C, through JavaScript to Clojure. Some
projects of mine aren't really code either, just a bunch of Dockerfiles. The
CI/CD system must let me build all of these, out of the box (I'm not going to
build language support for a CI system myself).

This rules out a number of language-specific solutions like
[GolangCI][ci:golangci] (Go-only), [php-censor][ci:php-censor] and
[PHPCI][ci:phpci] (PHP-only), [Strider CD][ci:strider-cd] (not language agnostic)

 [ci:golangci]: https://golangci.com/
 [ci:php-censor]: https://github.com/php-censor/php-censor
 [ci:phpci]: https://www.phptesting.org/
 [ci:strider-cd]: http://strider-cd.github.io/

### Configuration as code

While using Travis, I got used to having my project's CI pipeline together with
the rest of the sources. I feel they belong together (and this way, I can use
source code management for the CI pipeline too). For this reason, I'm only
willing to consider products that support this. Not to mention, I often want
different pipelines depending on the branch, so that I can experiment. I want to
be able to just modify a control file within the source, and be done with it. I
do this all the time, and I'm not willing to part with this workflow.

This rules out [Buildbot][ci:buildbot] and [laminar][ci:laminar].

 [ci:buildbot]: https://buildbot.net/
 [ci:laminar]: https://laminar.ohwg.net/

### Working together with Gitea

I'm using Gitea for my git hosting, and I want my CI/CD solution to integrate
well with it. By integration, I mean that ideally, I'd want my CI system to
authenticate against Gitea (but allow me to restrict who can use CI), to run
pipelines on pull requests, tags, and so on. Running only on commits is not
enough.

This rules out [Concourse][ci:concourse].

 [ci:concourse]: https://concourse-ci.org/

### Built on containers

I'd like my builds to be isolated, to run in containers, so I won't have to set
up different agents with a suitable environment for each project. I want the
projects themselves to set up their own environment.

### Reasonable APIs

In the future, I might want to build things on top of the CI/CD system, so I
want API access to builds, projects, whatever. It's not a terribly important
thing right now, but an API is essential in the long run. If I have to use a
specific tool, that's not the same.

# The Winner

After everything else has been ruled out, there was only one product standing:
[Drone][ci:drone].

 [ci:drone]: https://drone.io/

It is self-hosted, open source, lightweight, easy to get started with, language
& build system agnostic, treats configuration as code, integrates well with
Gitea, pipelines are built on top of Docker, and the server has a useful API.

It ticks all my boxes. While it may seem that the requirements were specifically
set up to match Drone and Drone alone, that's not the case. The list was set up
before I started looking, based on experiences I had with CI systems in the
past: I have used [Buildbot][ci:buildbot] and [Jenkins][ci:jenkins] in the past,
some of the requirements are a result of this.

Lets have a look at the products I reviewed again, but this time, we'll look at
the whole list of requirements, per tool.

# Comparing CI/CD solutions

I will only consider self-hosted, open source solutions now, because these two
are hard requirements, and there's no point in including hosted / non-free
solutions in the comparison, even if they'd tick all the other boxes (they
don't). I will not compare CI solutions that aren't language-agnostic by default
either, because no matter how many other boxes they tick, if I have to build the
language support for them, that's work I am not willing to do, so they're out by
default.

### [GitLab CI/CD][ci:gitlab], [Phabricator][ci:phabricator]

These are integrated solutions, and even if they'd otherwise support the
features I require, simply being too heavy disqualifies them. For this reason, I
haven't looked at them closer. I do have some experience with GitLab, as we used
it at a previous job. It was okay, but my impression is that Drone's much more
flexible. But that's just an impression - I haven't looked closer at GitLab
CI/CD.

### [Buildbot][ci:buildbot]

I have two main problems with buildbot: configuration is separate from the
tested project, and workers need to be set up so that they're able to build &
test the projects that use them. In practice, this means that setting up the
environment for a project is a separate step.

I want to run builds against pull requests too, which is something not easily
doable with Buildbot. I'd have to build this part myself.

I want my projects to be in control of how they are tested, so that if I add a
new dependency, I can adjust the CI pipeline in the same commit. I want to be
able to use different pipelines in different branches. This requires CI
configuration to be part of the source code. Sure, I could set up my Buildbot
workers so that each of them are just thin wrappers around docker that runs the
build steps in a container. But then I'd have to build this thing. I could,
then, build it so that it takes the configuration from the tested project - but
I still have to build it. I don't want to do that. I want this out of the box.

### [GoCD][ci:gocd]

Apart from the very high memory requirements, GoCD suffers from the same issues
Buildbot does: configuration not being part of the project, and lack of Gitea
integration.

With GoCD, I need to set up pipelines on a web interface. Yeah... no, thanks,
for all the reasons listed above for Buildbot.

### [Jenkins][ci:jenkins]

Jenkins does support configuration as code nowadays, and is able to use docker
agents, allowing my projects to be in full control over how and in what
environment they are tested in. The `Jenkinsfile` syntax is a tad too verbose
for my taste, but it gets the job done.

The problem with Jenkins is that it's far from lightweight, and non-trivial to
set up to work with Gitea. There is a Gitea plugin, but it has zero
documentation. I'm sorry, but I can't work with that.

### [Factor][ci:factor]

Factor is more like a thing that pulls a whole workflow together, rather than a
CI/CD system in and of itself. Just looking at the docs, I failed to figure out
how I could build a simple CI out of it.

If it doesn't make it obvious how to do that, it's not the right project for me.

### [Laminar][ci:laminar]

Laminar is in many ways similar to Buildbot, except its even more primitive. It
requires me to create a script that runs the build, including fetching the
source. It lacks any kind of integration. It's probably a good building block
for people who want to build their own pipelines, but for my needs, I need
something a bit more advanced.

It has no triggers - I'd have to build those myself. I'd have to build a way to
run builds in containers, and pull the configuration from the source. And figure
out a way to run builds on pull requests, too. So many things to build!

### [Concourse][ci:concourse]

This ticks pretty much all of my boxes, except one: Gitea integration. While I
can easily trigger a build on PRs, Concourse pipelines define the source as a
dependency, and that fails in the face of pull requests. Or branches.

### [Drone][ci:drone]

Drone pipelines are built on Docker. Every step is ran inside a container, and I
can specify which one. Drone takes care of laying out the source, so my
pipelines do not need to care about that step, only about what comes after. It
comes with Gitea integration built-in, so much so that I can set it up to
authenticate against it, and even lock it down to only allow select users to
access Drone (while still keeping the results public). It's also incredibly
lightweight.

None of the other solutions offered all this out of the box.

It is not perfect, there are a bunch of things I want to change about it, like
the logo in the top left corner taking me to the project list, or having an URL
for the last build of a project. Oh, and removing the login bar for
unauthenticated users: noone but me will be able to log in, and I know the login
URL anyway. There's no need to show that bar to anyone.

# Conclusion

In short, [Drone][ci:drone] fulfills all my requirements, and more: it is also
fairly easy to extend. I mean, if I want a step that verifies that each commit
conforms to the [Developer Certificate of Origin][dco], or that each tag is
signed by a know key, I can do both: [drone-plugin-dco][drone:dco],
[drone-plugin-signature-check][drone:sc]. I'll just include a step in the
projects that need these, and I'm all done. Doing these kinds of plugins is
easy, they're just docker containers that have access to the source code, and
have a bunch of environment variables available to help them figure out what's
what. Can't be much simpler than that, and you can write them in any language.
Or even multiple languages. It can even talk to external services if it needs
to.

 [dco]: https://developercertificate.org/
 [drone:dco]: https://git.madhouse-project.org/algernon/drone-plugin-dco
 [drone:sc]: https://git.madhouse-project.org/algernon/drone-plugin-signature-check

I'm very happy with my choice so far.
