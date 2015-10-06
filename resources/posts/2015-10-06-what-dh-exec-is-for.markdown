---
title: "What dh-exec is, and what it isn't for"
date: 2015-10-06 15:00
tags: [Debian, Rants, .planet.debian]
---

Strange as it may be, it turns out I never wrote about
[dh-exec][dh-exec] yet, even though it is close to being four years
old. Gosh, time flies so fast when you're having fun! Since its first
introduction, there's been a reasonable uptake in `dh-exec` use: as of
this writing, 129 packages build-depend on dh-exec. One might think
this would be a cause for celebration, that the package is put to
great use. But it's not.

You see, a significant number of those 129 packages are doing it
wrong, and need not build-depend on dh-exec at all.

  [dh-exec]: https://github.com/algernon/dh-exec

<!-- more -->

The purpose of `dh-exec` is to allow one to do things stock
`debhelper` can't do, such as renaming files during the `dh_install`
phase, or applying architecture or build profile based filtering, or
doing environment variable substitution. There are many legit uses for
all of these features, but there are some which can be easily solved
without using dh-exec. So first, I'll talk a bit about the **don'ts**.

What not to use dh-exec for
===========================

One of the most abused part of `dh-exec` is its variable substitution
feature, and it is often used without need, to install
multiarch-related files. While that is one intended use-case, there
are few situations currently in the archive that stock debhelper can't
handle. Let me explain the situation!

Lets assume we have an upstream package, where the build system is
something as common as autotools or CMake, for which debhelper can
automatically set the appropriate flags and paths. Furthermore, lets
assume that the upstream build system would install the following
files:

    /usr/lib/x86_64-linux-gnu/libalala.so.1.2.3
    /usr/lib/x86_64-linux-gnu/libalala.so.1
    /usr/lib/x86_64-linux-gnu/libalala.so
    /usr/lib/x86_64-linux-gnu/libalala.a
    /usr/lib/x86_64-linux-gnu/pkgconfig/libalala.pc
    /usr/include/lalala/lalala.h

We want to include the first two in the `libalala1` package, the rest
in `libalala-dev`, so what do we do? We use stock debhelper, of
course!

* `libalala1.install`:

        usr/lib/*/lib*.so.*

* `libalala-dev.install`:

        usr/lib/*/lib*.a
        usr/lib/*/lib*.so
        usr/lib/*/pkgconfig/*.pc
        usr/include/*

That is all you need. In this case, there is absolutely no need for
dh-exec. While using dh-exec without need is not much of an issue,
because it only increases the space required for the build and build
times by a tiny bit, I would still strongly recommend not introducing
dh-exec needlessly. Why? Because of simplicity and aesthetics.

So, if you find yourself doing any of these, or similar, that's a sign
you are doing things wrong:

    usr/lib/${DEB_HOST_MULTIARCH}
    usr/lib/${DEB_HOST_MULTIARCH}/*.so.* /usr/lib/${DEB_HOST_MULTIARCH}/
    usr/lib/${DEB_HOST_MULTIARCH}/pkgconfig/*.pc /usr/lib/${DEB_HOST_MULTIARCH}/pkgconfig
    usr/lib/${DEB_HOST_MULTIARCH}/package/*.so /usr/lib/${DEB_HOST_MULTIARCH}/package

Unless there are other directories under `usr/lib` that are not the
multiarch triplet, using stock debhelper and wildcards is not only
more succinct, simpler and more elegant, but also lighter on resources
required.

When dh-exec becomes useful
===========================

Changing installation paths
---------------------------

Once you want to **change** where things get installed, **then**
dh-exec becomes useful:

    usr/lib/*.so.* /usr/lib/${DEB_HOST_MULTIARCH}
    usr/lib/${DEB_HOST_MULTIARCH}/package/* /usr/lib/${DEB_HOST_MULTIARCH}/package-plugins/
    some/dir/*.so.* /usr/lib/${DEB_HOST_MULTIARCH}

This usually happens when upstream's build system can't easily be
taught about multiarch paths. For most autotools and CMake-based
packages, this is not the case.

Variable substitution
---------------------

Consider this case:

    #!/usr/bin/dh-exec
    /usr/share/octave/packages/mpi-${DEB_VERSION_UPSTREAM}/hello2dimmat.m /usr/share/doc/octave-mpi/examples/hello2dimmat.m
    /usr/share/octave/packages/mpi-${DEB_VERSION_UPSTREAM}/hellocell.m /usr/share/doc/octave-mpi/examples/hellocell.m

Here, the build supplies `${DEB_VERSION_UPSTREAM}`, and using dh-exec
allows one to have a generic `debian/links` file, that does not need
updating whenever the upstream version changes. We can't use wildcards
here, because `dh_link` does not expand them.

Renaming files
--------------

In case one needs to rename files during the `dh_install` phase,
dh-exec can be put to use for great results:

    ssh-agent-filter.bash-completion => usr/share/bash-completion/completions/ssh-agent-filter

Filtering
---------

Sometimes one would wish to conditionally install something based on
the architecture, or the build profile. In this case, dh-exec is the
tool to turn to:

    <!stage1 !stage2> ../../libdde-linux26/Makeconf* usr/share/libdde_linux26

    usr/lib/gvfs/gvfsd-afc                                          [!hurd-any]
    usr/lib/gvfs/gvfsd-gphoto2                                      [linux-any]
