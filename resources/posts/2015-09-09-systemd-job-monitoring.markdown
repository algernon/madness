---
title: "Monitoring systemd jobs"
date: 2015-09-09 10:55
tags: [Technology, HOWTO]
---

At the end of last year, I got a bit more serious about
[monitoring my own systems][monitoring-setup], and the effort was well
worth it. Yet, there were some parts of the system I had no metrics or
information about: for example, there are services running on my
systems that get restarted from time to time, which is ok, but I'd
still like to know when and how often that happens, so I can judge
whether that matches my expectations. Then, while there, services can
also crash - and that, too, happens from time to time -, and while I
can figure that out from the logs, this fact does not appear on my
monitoring dashboard.

The missing piece I wanted was a way to monitor the crashes and
restarts in an uniform way, one that does not require me to parse a
number of wildly different logs. Turns out, this [missing piece][mmt]
was a lot easier to write than anticipated!

 [monitoring-setup]: /blog/2014/12/09/monitoring-setup/
 [mmt]: https://github.com/algernon/mad-monitoring-tools

<!-- more -->

I'm running [Debian Jessie][debian8] on my servers, which comes with
[systemd][systemd] by default. I've been a systemd user for a good
while now, and am enjoying the benefits greatly. One such benefit is
that it has knowledge about the state of the whole system, and even
better, one can subscribe to job creation / removal events! Since a
job is created every time something happens with a service, be that
manual restarting, reloading, or an automatic one after a crash,
monitoring the jobs gets us most of the information we need.

All we need to do is to subscribe to the `JobNew` or `JobRemoved`
events of `org.freedesktop.systemd1.Manager` via DBus, and we get the
`job-id`, the `job` object itself, and the `unit` in the callback. For
my purposes, this last one is the only thing I need. I can slap on a
timestamp and a fake metric, and send it over to [Riemann][riemann]
and I'll have a fancy new widget on the dashboard!

 [debian8]: https://www.debian.org/releases/jessie/
 [systemd]: http://www.freedesktop.org/wiki/Software/systemd/
 [riemann]: http://riemann.io/

Another neat possibility would be to monitor the time between `JobNew`
and `JobRemoved`, which would make a nice metric in and of itself. I
don't need that information yet, however.

In any case, after discovering how easy it is to monitor systemd jobs,
I needed to write a tool to do it for me. I didn't choose collectd,
because that would not only require an awful lot of C code to write a
plugin for, but fitting a subscriber into the collectd model seems
like a terrible mismatch. Instead, I turned to my go-to language for
quick hacks: [Hy][hylang].

 [hylang]: http://hylang.org/

With it, it was just a few dozen lines to implement what I need, in a
way that is reasonably easy to extend later. So right now, my
monitoring service looks like this:

<div class="pygmentize" data-language="clojure">
(import [mad-monitoring-tools.core [*]])
(require mad-monitoring-tools.core)

(mmt/connect-> SystemdJobSource [:on-new]
               RiemannWriter)

(mmt/run!)
</div>

And that's all. As usual, the sources are available on [GitHub][mmt],
for anyone to see and extend. Documentation is, well, pretty much
missing, because I got bored writing it. I'll get there,
eventually. For now, the sources are a few dozen lines, should be
reasonably easy to find one's way through.

 [mmt]: https://github.com/algernon/mad-monitoring-tools

This exercise would have been considerably harder with sysvinit. Mind
you, the current solution also leaves a lot to be desired: it would be
nice to know why a job was started, and whether it succeeded or
not. It is somewhat possible to accomplish that, by checking the unit
state in a `JobRemoved` handle, but that too, is racy, and may not be
the most reliable thing to do.

Nevertheless, the original problem is solved, in a short and elegant
way. Hurray!
