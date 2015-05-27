---
title: "Potential Happiness"
date: 2015-05-27 16:30
tags: [Technology, Monitoring]
---

Ever since I [redid my monitoring setup][blog:monitoring], two issues
were coming up over and over again: I needed a browser to have a
reasonable overview of my systems, and I needed ruby to run
riemann-dash. Granted, it is easy to query [Riemann][riemann] from the
command-line, and I can even put up a screen or tmux session with
watch commands running in its many windows, but that's horribly
ugly. I wanted a dashboard, one that I can look at, and see trends
right away. A few months ago, I came accross
[blessed-contrib][blessed-contrib], and knew I could build my
dashboard using it. After a few initial tries and failures, today, I
have a solution that works for me.

 [blog:monitoring]: /blog/2014/12/09/monitoring-setup/
 [riemann]: http://riemann.io/
 [blessed-contrib]: https://github.com/yaronn/blessed-contrib

<!-- more -->

<div style="text-align: center">
<a href="/assets/asylum/images/posts/potential-happiness/ph-1.png"
   class="thumbnail" style="display: inline-block">
 <img src="/assets/asylum/images/posts/potential-happiness/ph-1.thumb.png"
       alt="[Potential Happiness]">
</a>
</div>

## Potential Happiness

It is with great joy that I announce the existence of
[potential-happiness][ph], a dashboard for the terminal! It's far from
being perfect, or even friendly (you have to edit JSON to set up your
dashboard, and the layout engine is dumb as a rock). But after using
it for a day, I'm happy enough with its state to talk about it
publicly. Use it with care and patience, happiness is only a
potential!

 [ph]: https://github.com/algernon/potential-happiness

### What problems does it solve?

If you follow this blog, you already know that I use
[collectd][collectd] and [Riemann][riemann] for
[monitoring][blog:monitoring], and a combination of [syslog-ng][sng]
and [elasticsearch][es] to [manage my logs][blog:logging].

 [riemann]: http://riemann.io/
 [sng]: http://www.syslog-ng.org/
 [collectd]: https://collectd.org/
 [es]: https://www.elastic.co/products/elasticsearch
 [blog:monitoring]: /blog/2014/12/09/monitoring-setup/
 [blog:logging]: /blog/2015/05/07/grepping-logs-is-still-terrible/#an-example

I wanted to see relevant information on my terminal, because I'm more
comfortable with that, than with running two browser tabs (one for the
Riemann dashboard, one for logs). Running a browser feels like an
overkill. So, we need something that can receive data (either via
pulling, or by subscribing to a channel) from both Riemann and
ElasticSearch, and that works on the terminal. This is what
[potential-happiness][ph] does.

It gives you a way to construct a dashboard, and then feeds that board
with events received. Currently, there aren't many bells and whistles,
because what there is already satisfies my immediate needs. But don't
fret, I'm known to fiddle around with things, so this thing will grow,
too.

### What's with the name?

Don't ask.

## Creating a dashboard

Lets suppose we want to create a dashboard for Riemann statistics, and
spice it up with some logs from ElasticSearch too. We want three rows:

1. The top row with boxes, for latency and stream rate data (just
   text).
2. A graph of stream latencies and input rate (by source).
3. Logs from ElasticSearch.

The top row would need to be much smaller than the other two
rows. Based on a quick look at what stats riemann provides, we can
conclude that we'll need six small boxes on top. After some trial and
error, lets settle that we want a 18x9 grid. That should do.

The end result should look something like this:

<div style="text-align: center">
<a href="/assets/asylum/images/posts/potential-happiness/ph-2.png"
   class="thumbnail" style="display: inline-block">
 <img src="/assets/asylum/images/posts/potential-happiness/ph-2.thumb.png"
       alt="[Potential Happiness]">
</a>
</div>

First, we will require a few libraries:

<div class="pygmentize" data-language="javascript">
var ph   = require ('../lib/index'),
    util = require ('../lib/util');</div>

The first one, `ph` is `potential-happiness` itself: it provides the
widgets, transformations and stuff. The second one, `utils`, we want
for the `syslog_to_log` function, which we will use to display our
logs from ElasticSearch in a log widget box.

Then, we tell the thing that we're exporting this whole config:

<div class="pygmentize" data-language="javascript">
module.exports = {</div>

After which, comes the meat of the configuration! First, we ask the
layout engine to give us a 18x9 grid:

<div class="pygmentize" data-language="javascript">
    grid: {rows: 9, cols: 18},</div>

Then set up a default host: if `RIEMANN_HOST` is set in the
environment, use that, otherwise fall back to `127.0.0.1`. This is the
host where Riemann and ElasticSearch are running.

<div class="pygmentize" data-language="javascript">
    defaults: {
        source: {host: process.env.RIEMANN_HOST || "127.0.0.1"}
    },</div>

The come the widgets! To add widgets to the grid, we add them to the
`widgets` key in the configuration, which is a list of hash maps.

<div class="pygmentize" data-language="javascript">
    widgets: [</div>

Each item in that list **must** contain the following keys:

* `widget`: The constructor for the widget. Usually, a widget from the
  `ph.widgets` module.
* `options`: Options for the widget. This includes the label and the
  source (more about the source later).
* `width` and `height`: The width and height of the widget,
  respectively, in terms of grid columns and rows.

It can also contain `pos_x` and `pos_y` keys, in case one wants to
explicitly position a widget. That is sometimes necessary, when the
layout engine is too dumb to figure the proper place out on its own.

As for the source, we need a `query` (in case of Riemann, a string in
riemann query DSL format; in case of ElasticSearch, a hash-map
suitable for passing to the official JavaScript client's search
function). The source can also have a `transform` option, which is a
function that takes a single parameter (the data fetched from the
source), can apply transformations on it, and that returns the
results. We can use transformers to trim floating point numbers to a
certain precision, or to remove the date part of a timestamp, and so
on.

Lets set up the first row! We want six `ph.widgets.text` boxes, all
of them rounded to two decimal points, with a height of one row, and
a width of three columns. We'll query `stream latency 0.999`, `0.99`,
`0.95`, `0.5` `0.0`, and finally `streams rate`. Pretty
straightforward:

<div class="pygmentize" data-language="javascript">
        {widget: ph.widgets.text,
         options: {label: "# Streams latency 0.999",
                   source: {query: 'service = "riemann streams latency 0.999"',
                            transform: ph.transform.trim_to_fixed (2)}},
         width: 3,
         height: 1},
        {widget: ph.widgets.text,
         options: {label: "# Streams latency 0.99",
                   source: {query: 'service = "riemann streams latency 0.99"',
                            transform: ph.transform.trim_to_fixed (2)}},
         width: 3,
         height: 1},
        {widget: ph.widgets.text,
         options: {label: "# Streams latency 0.95",
                   source: {query: 'service = "riemann streams latency 0.95"',
                            transform: ph.transform.trim_to_fixed (2)}},
         width: 3,
         height: 1},
        {widget: ph.widgets.text,
         options: {label: "# Streams latency 0.5",
                   source: {query: 'service = "riemann streams latency 0.5"',
                            transform: ph.transform.trim_to_fixed (2)}},
         width: 3,
         height: 1},
        {widget: ph.widgets.text,
         options: {label: "# Streams latency 0.0",
                   source: {query: 'service = "riemann streams latency 0.0"',
                            transform: ph.transform.trim_to_fixed (2)}},
         width: 3,
         height: 1},
        {widget: ph.widgets.text,
         options: {label: "# Streams rate",
                   source: {query: 'service = "riemann streams rate"',
                            transform: ph.transform.trim_to_fixed (2)}},
         width: 3,
         height: 1},</div>

The next row will contain two line charts, which by default uses a
differently colored line for each `host` property, `metric` for the
`y` axis, and `time` for the `x` axis. Instead of the host, we'd
rather use the `service` (minus the `riemann streams latency` prefix),
and we only want the time part of the timestamp. So we use a custom
`transform` function that does these for us. We also set the width of
the data inside the widget to 100, so it becomes a nice long chart.

The setup is very similar for both charts:

<div class="pygmentize" data-language="javascript">
        {widget: ph.widgets.line_chart,
         options: {label: "Streams latencies",
                   width: 100,
                   source: {query: 'service =~ "riemann streams latency %"',
                            transform: function (data) {
                                d = new Date (Date.parse (data.time));
                                data.host = data.service.substr (24);
                                data.time = d.toLocaleTimeString ();
                                return data;
                            }}},
         width: 9,
         height: 4},
        {widget: ph.widgets.line_chart,
         options: {label: "Servers in rate",
                   width: 100,
                   legend: {width: 24},
                   source: {query: 'service =~ "riemann server % in rate"',
                            transform: function (data) {
                                d = new Date (Date.parse (data.time));
                                data.host = data.service.substr (15).slice (0, -8);
                                data.time = d.toLocaleTimeString ();
                                return data;
                            }}},
         width: 9,
         height: 4},</div>

And that's the second row! In the last row, we want a single widget
that displays logs from ElasticSearch. We'll use the default search
(which is a range query on the `@timestamp` field, N messages before
now), with a limit of 34 results (because that's how much fits on my
terminal, your results may vary), using the `syslog-ng` index (because
syslog-ng uses that by default). To display messages correctly, we
also need a custom `on_message` handler, helpfully provided by
`util.syslog_to_log`. All in all, this widget is relatively
straightforward too:

<div class="pygmentize" data-language="javascript">
        {widget: ph.widgets.log,
         options: {label: "Logs (elastic)",
                   source: {host: "localhost",
                            index: "syslog-ng",
                            method: "elasticsearch",
                            limit: 34},
                   on_message: util.syslog_to_log},
         width: 18,
         height: 4}</div>

After that, we close up the `widgets` array and the module export:

<div class="pygmentize" data-language="javascript">
    ]
};
</div>

(The full source for the above experiment is available
[here](https://raw.githubusercontent.com/algernon/potential-happiness/951ee11b0e63c413e47a5bc7bef362c610f4b492/examples/local-log.js).)

That's it, you're done! Run it with `src/dashboard.js your-config.js`!
