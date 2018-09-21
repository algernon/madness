---
title: "On CI systems"
date: 2018-09-21 12:45
tags: [Hacking, Technology, CI]
---

A few days ago I [wrote a post][blog:finding-ci] about finding a suitable CI/CD system, which got posted on [lobste.rs][lobster:finding-ci], where a number of questions were raised. Turns out I did not adequately explain why some of my requirements are there, and what usecases they serve. I'll try to correct that, and do so in this post.

In short, the two most important features I require are pipeline configuration being part of the repository, and being built on containers. A whole lot of CI/CD systems fail either or both of these, and that immediately disqualified them during my search.

 [blog:finding-ci]: /blog/2018/09/17/finding-a-ci-solution/
 [lobster:finding-ci]: https://lobste.rs/s/m1si0g/finding_continous_integration_solution

Lets see why!

<!-- more -->

To better understand why I think these features are a must have, I will first explain my projects, and the workflow I'm used to. Understanding these is essential for understanding my requirements. Do keep in mind that there are different workflows and different needs, the conclusions I arrive at are only valid in context: there are use-cases where my choices make no sense.

I have a lot of projects, in various languages: C, Python, Emacs Lisp, Clojure, Ansible, Shell, Dockerfiles, Hy, C++, Arduino, JavaScript, and so on. There is very little in common in them, they are all built and tested differently. I use branches a *lot*: whenever working on a new feature, or even a bugfix, or anything non-trivial for that matter, I open a branch. Branches often have different testing requirements: sometimes I change dependencies; other times I temporarily disable parts of the CI pipeline to be able to iterate faster (and then revert that, and rebase the branch to make it nicer before merging).

For the above reasons, I want my pipelines to be able to set up their own environment, so that I can have one set of dependencies installed for the master branch, another set for my current area of focus, and so on. This is easiest to do when the CI pipeline is part of my sources, because I can just change it on a branch, along with the rest of the changes. I can change source & CI pipeline in lockstep, there will never be a point where one is ahead of the other. I don't have to special case the pipeline, because it's already tied to the branch - I don't need to care about other branches when working on a temporary one. This gives me incredible flexibility, which I use a lot.

When setting up [Drone][ci:drone], I made heavy use of this, to experiment with how to test my projects. Lots of fake commits, lots of build failures - all while the master branch built just fine, the same way it always did.

 [ci:drone]: https://drone.io/

Because I want my pipelines to be able to set up their own environment, I need them to run in containers, so they can install packages, run other services, and so on. I do not ever want to give my CI pipelines access to the host, not under normal circumstances. The whole pipeline-along-the-source feature depends on containers, as far as I'm concerned. I do not see a reasonable way to do it without (chroots lack network isolation, VMs are too heavy, dedicated workers are a pain to orchestrate).

To make it even more clear, lets see the pipeline of my [riemann-c-client][rcc] project:

 [rcc]: https://git.madhouse-project.org/algernon/riemann-c-client

<div class="pygmentize" data-language="yaml">
pipeline:
  dco:
    group: meta
    image: algernon/drone-plugin-dco

  signature-check:
    group: meta
    image: algernon/drone-plugin-signature-check
    keys: [ 10E65DC045EABEFCC5193A26AC1E90BAC433F68F ]
    when:
      event: tag

  bootstrap:
    group: bootstrap
    image: algernon/drone-language:C
    commands:
      - autoreconf -i

  tls-certificates:
    group: bootstrap
    image: algernon/drone-language:C
    commands:
      - apt install -y openssl
      - cd tests/etc && ./gen-certs.sh

  build:
    group: build
    image: algernon/drone-language:C
    environment:
      - RCC_NETWORK_TESTS=1
      - RIEMANN_HOST=riemann
    commands:
      - apt install -y ...
      - install -d _build/unstable
      - cd _build/unstable
      - ../../configure --enable-silent-rules CFLAGS="-Wall -Wextra -O3 -g --coverage"
      - make
      - make coverage.info CK_VERBOSITY=normal
      - genhtml coverage.info | tail -n 3

services:
  riemann:
    image: algernon/drone-service:riemann
    environment:
      - RIEMANN_CONFIG=.../tests/etc/riemann.config
      - RIEMANN_HOST=riemann
      - RIEMANN_TLS_KEY=.../tests/etc/server.pkcs8
      - RIEMANN_TLS_CERT=.../tests/etc/server.crt
      - RIEMANN_TLS_CACERT=.../tests/etc/cacert.pem
</div>

That's quite a handful, isn't it? Lets take it apart.

First of all, and this is important to keep in mind, all of these stages run in different containers, with the only shared state being the checked out sources mounted as volumes. Whatever gets put in that directory, will be immediately visible to other stages, and will survive 'till the next stage.

Second, stages in the same `group` execute in parallel, in different containers. In this case, we have three groups: `meta` - which does some sanity checking on the git metadata, such as enforcing `Signed-off-by` lines and GPG-signed tags; `bootstrap` - which generates some files needed for the build, such as autotools-generated files, and TLS certificates for end-to-end testing; `build` - which does the real building & testing.

Being able to run stages in parallel is a great time saver, although not as much as it was a few days ago, when I still built the project on two different distribution releases. But nevertheless, being able to run `autoreconf -i` and TLS certificate generation side-by-side does buy me a handful of seconds.

Another crucial part of this pipeline is the Riemann service: I do end-to-end testing, and pull up a Riemann service, with configuration appropriate for the tests.

Since all of these run in containers, I can install whatever I want in them. I can start a service that is accessible to all stages, and is not visible outside of the pipeline. If I run another pipeline for another project while this one is running, it will not see my Riemann, even if it is run by the same agent, on the same host. If I quickly push to both `master` and onto another branch, the two pipelines can be run in parallel, and they will not trip over each other. I did not have to do anything special to achieve this, Drone did all that for me.

Even without considering parallel execution, running everything in a container by design has plenty of benefits, as we've seen. But there's much more too! Everything being a container, it is exceptionally easy to reuse existing components, there is very little I need to build myself. I can just pull up a Debian container, slap `build-essential` and a few other things onto it, and call it `algernon/drone-language:C`, for example. For other languages, I do the same: find a convenient base image, add a thing or two, and we're done. This is immense power at my fingertips.

If I want to iterate fast on a branch, I will temporarily disable end-to-end tests: this allows me to drop `tls-certificate`, and the `riemann` service. To do this, I did not have to add a temporary special case to a global pipeline, I did not need to think about what happens if the pipeline runs against any other branch, simply because it doesn't do that. It only runs for the current branch, and when I rebase it in preparation for the merge, I will simply discard my changes to `.drone.yml`, and pretend none of this ever happened.

Thus, the combination of containers and in-source pipelines give me enough flexibility to use the workflow I find most convenient. It's not a workflow that suits everybody, or every project, or group of projects. It is simply one that suits *me*. And I want my CI/CD system to support it as well.
