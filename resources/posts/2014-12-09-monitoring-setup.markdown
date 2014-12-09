---
title: "Monitoring with collectd and Riemann"
date: 2014-12-09 11:30
tags: [Technology, Monitoring, HOWTO]
---

For some time, I have been toying with [Riemann][riemann], and have
been gradually moving my monitoring infrastructure over to it.
Initially, I used a horrible hacky mess of Riemann, [syslog-ng][sng],
[riemann-tools][riemann-tools], and custom scripts. This had multiple
issues: from being inefficient, through having way too much Ruby, to
being inflexible and messy. I wanted a cleaner architecture,
preferably with less parts, because I like to trim dependencies.

Recently, at [work][LogMeIn], one of my tasks is to plug a few of the
things we're experimenting with (such as [Riak][riak]) into a
monitoring solution. This was a perfect opportunity to play more with
[Riemann][riemann], and as a proof of concept, make it work for my
production environment too, with a clean implementation.

For this, I used [Riemann][riemann], [collectd][collectd], and
[riemann-extra][riemann-extra], and today, I will explain how these
were connected together, and show the configs I used.

 [riemann]: http://riemann.io/
 [sng]: http://www.syslog-ng.org/
 [riemann-tools]: https://github.com/aphyr/riemann-tools
 [LogMeIn]: https://secure.logmein.com/about/aboutus.aspx
 [Riak]: https://basho.com/riak/
 [collectd]: https://collectd.org/
 [riemann-extra]: https://github.com/pyr/riemann-extra

<!-- more -->

What we'll end up with, is something like this:

<div style="text-align: center">
<a href="/assets/asylum/images/posts/monitoring-setup/riemann-dash-1.png"
   class="thumbnail" style="display: inline-block">
 <img src="/assets/asylum/images/posts/monitoring-setup/riemann-dash-1.thumb.png"
       alt="[Riemann Dashboard]">
</a>
</div>

What we see here, is the health of servers, at a quick glance. We'll
be using [collectd][collectd] to collect the events, and send it to
[Riemann][riemann]. We'll collect CPU use (and average all cores into
a single metric), load, memory, swap and process stats, and free disk
space. This is trivial with collectd, with the following configuration
snippet:

 [riemann]: http://riemann.io/
 [collectd]: https://collectd.org/

<div class="pygmentize" data-language="apache">
LoadPlugin cpu
LoadPlugin df
LoadPlugin load
LoadPlugin memory
LoadPlugin processes
LoadPlugin swap

LoadPlugin aggregation

&lt;Plugin "aggregation"&gt;
    &lt;Aggregation&gt;
        Plugin "cpu"
        Type "cpu"

        SetPlugin "cpu"
        SetPluginInstance "%{aggregation}"

        GroupBy "Host"
        GroupBy "TypeInstance"

        CalculateNum false
        CalculateSum false
        CalculateAverage true
        CalculateMinimum false
        CalculateMaximum false
        CalculateStddev false
    &lt;/Aggregation&gt;
&lt;/Plugin&gt;
</div>

Then, to send it all over to Riemann:

<div class="pygmentize" data-language="apache">
LoadPlugin write_riemann

&lt;Plugin "write_riemann"&gt;
    &lt;Node "local"&gt;
        Host "your-riemann-server"
        Port "5555"
        Protocol TCP
        StoreRates true
        AlwaysAppendDS false
    &lt;/Node&gt;
    Tag "collectd"
&lt;/Plugin&gt;

&lt;Target "write"&gt;
    Plugin "write_riemann/local"
&lt;/Target&gt;
</div>

Great. Except we do not have a Riemann config yet, so there's nothing
to accept these metrics! We can't let that happen! Lets have a very
simple Riemann setup:

<div class="pygmentize" data-language="clojure">
(tcp-server :host "your-riemann-server")
(udp-server :host "your-riemann-server")
(ws-server :host "your-riemann-server")

(periodically-expire 1)

(let [index (tap :index (index))]
  (streams
    (default :ttl 3
      (expired #(prn "Expired" %))
      index)))
</div>

Now we can set up a dashboard to display (most of) this information.
I'm not going to reproduce the dashboard config here, but the
dashboard config I use is [available on GitHub][ms:dashboard-config].

 [ms:dashboard-config]: https://github.com/algernon/monitoring-setup/blob/master/riemann-dash/dashboard.json

There's a problem, however: a lot of metrics, such as memory, swap and
disk use are displayed in bytes, while we'd much prefer gigabytes
instead. We can easily solve this on the Riemann side, by adjusting
the metrics, with a block of code very similar to this one:

<div class="pygmentize" data-language="clojure">
(where (= (:plugin event) "memory")
       (adjust [:service str " Gb"]
               (scale (/ 1024 1024 1024) index)))
</div>

Adding this just below the `(expired ...)` line, above `index`, we
will have a few new metrics available to us: `memory/memory-used Gb`,
for example. Add this for all other plugins, adjusting the scale as
you see fit, and reload Riemann, and modify the dashboard. Behold! We
now have pretty numbers!

But before we go further, let me explain what the above bit of code
does.

First, it will apply only to events which ave their `plugin` attribute
set to `memory`, that is, the memory plugin. Then, it will adjust the
event, by appending `" Gb"` to the end of the `service` field. Then it
scales it from bytes to gigabytes. The new event is inserted into the
stream, and the original one remains there too (due to the original
`index` we had in the first Riemann config). We get both events,
because with Riemann, the stream is split every time we do a branch,
data will flow to all of these branches, and their results are merged
back together to form the main stream. This is a bit hard to get used
to, in my experience, and can be surprising, but once we figure this
out, things start to make much more sense.

Now, to make the dashboard even prettier, we'd like to have states for
our events. States describe whether a service is ok, if it has a
warning, or if it is in a critical state. We'll be using the
`threshold-check` function from [riemann-extra][riemann-extra] to help
us with this task.

 [riemann-extra]: https://github.com/pyr/riemann-extra

First, we'll need to import the module (don't forget to set the class
path too, before restarting Riemann!), and set up some thresholds:

<div class="pygmentize" data-language="clojure">
(require '[org.spootnik.riemann.thresholds :refer [threshold-check]])

(def thresholds
  {"cpu-average/cpu-user" {:warning 30 :critical 60}
   "cpu-average/cpu-system" {:warning 30 :critical 60}
   "cpu-average/cpu-nice" {:warning 50 :critical 20}
   "cpu-average/cpu-idle" {:warning 50 :critical 20 :invert true}
   "cpu-average/cpu-steal" {:warning 50 :critical 20}
   })
</div>

This goes to the top of our config. Once there, we can replace every
occurrence of `index` within our `(streams ...)` with an `(smap)`:

<div class="pygmentize" data-language="clojure">
(smap (threshold-check thresholds) index)
</div>

This does a streaming map over the index, applying the threshold-check
function, with our defined thresholds to each event that comes in.
With this, we'll have nice green/yellow/red states all over our
dashboard!

This is the bulk of it all, really. The full configuration is
available in my [monitoring-setup repository][ms:repo], for those
who'd like to take a look. The code in there may be different than
what I outlined here, because I'll keep tweaking it. There's a
[`blog/monitoring-setup`][ms:blog-tag] tag, that shows the state of
the setup as of this writing.

 [ms:blog-tag]: https://github.com/algernon/monitoring-setup/tree/blog/monitoring-setup

There are a lot more possibilities with both [collectd][collectd] and
[Riemann][riemann], which are not explored in this post. I will write
another article about those, once I had an opportunity to experiment
with them, and put them into production.

May your dashboard remain green forever!
