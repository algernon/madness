---
title: "Grepping logs is terrible"
date: 2015-05-05 11:00
tags: [Technology, Rants]
---

To this day, I am surprised at the number of people who complain about
the [Journal][systemd:journal]'s binary storage format. Having spent
years working as a system administrator, and after years of working
with and on [syslog-ng][sng] - in the capacity of
[maintainer of the Open Source Edition][sng:maintainer] for more than
a year -, I am increasingly puzzled about all the hostility towards
non-text storage formats. I am even more puzzled about the arguments
against it. Maybe I'm living in a different world, but there are very,
very few reasons I see for using text based storage, when there is a
better option available. I've been asked numerous times about my
logging needs, and why I am so vigilantly against text based log
storage, and this is my attempt at answering those questions.

Please note that this is not a journal-apology: I've been deploying
binary log storage way before the Journal happened, and my recommended
solution is not the Journal either. (Truth be told, I like the idea of
the Journal, and am enjoying its features on some machines of mine,
but it is not a solution to my needs, and never will be.)

  [systemd:journal]: http://0pointer.de/blog/projects/journalctl.html
  [sng]: https://www.syslog-ng.org/
  [sng:maintainer]: https://bazsi.blogs.balabit.com/2014/05/handing-over-syslog-ng-maintenance/

<!-- more -->

Table of Contents
-----------------

* [Requirements](#requirements)
    * [The Small One](#requirements/the-small-one)
    * [The Big One](#requirements/the-big-one)
* [Why not text?](#why-not-text)
    * [The Small System](#why-not-text/the-small-system)
    * [The Big System](#why-not-text/the-big-system)
* [Example uses](#example-uses)
* [Countering the anti-binary reasons](#countering-the-anti-binary-reasons)
* [Closing thoughts](#closing-thoughts)

<a name="requirements"></a>
Requirements
------------

To start off, I'll describe my systems and my requirements. I have two
setups, with different needs, and I originally used different
solutions, but converged to a single one later.

<a name="requirements/the-small-one"></a>
### The Small One

The easier setup is my personal setup: my own servers, computers and
gadgets. It consists of two VPS machines, a PC at home, two laptops
(all of these run Debian) and a Raspberry Pi B (running either
OpenELEC, whose logs I do not collect or care about; or Raspbian,
which is tied in to the above infrastructure). I used to have a router
with custom firmware, but I no longer have that.

On average, I have a few hundred messages incoming a second.

My requirements here are simple:

<ul>
<li><strong>I need one central place to store my logs.</strong>

<p>
  I do not care - nor store - logs on each computer. They all send to
  a central server, and only hold logs themselves if the central is
  unreachable. If I lose a few messages here and there, that is no
  problem.
</p>
</li>

<li><strong>The central must be easy to change.</strong>

<p>While on the go, possibly without internet access, I do not want my
  laptops to even try sending to central. So I want to be able to pull
  up a dummy node locally.</p>

<p>I don't care whether the temporary local collector node and the
  central one are mergeable. If need be, I can export my logs from the
  local one, and import it into central, but I rarely do that.</p>

</li>

<li><strong>I want to preserve all logs in an efficient way.</strong>

<p>I need historic data for experiments and some toy projects of
mine.</p>
</li>

<li><strong>I post-process data, and store a structured, processed version only.</strong>

<p>I take all my logs, may they come from syslog, the Journal or any
other source, and post-process them. I extract out key fields,
correlate messages, and so on. I'm only interested in this part of the
data, the original messages are discarded.
</p>
</li>

<li><strong>I want to do queries that span programs and machines.</strong>

<p>One thing I reasonably frequently do, is follow the life of e-mail
I send: the logs from Gnus that composed the message from my PC,
through msmtpd on the same machine, through postfix on the raspberry
pi, then postfix on my remote server. That spans three hosts and four
programs.</p>

<p>I want to ask my system this: "<em>Show me all the logs for the
e-mail with message-id <strong>X</strong>!</em>", or
"<em>Show me all the logs of e-mails I sent today that were delayed
more than an hour!</em>", and a number of similar questions.
</p>
</li>

<li><strong>I want reasonably fast and efficient ad-hoc
queries.</strong>

<p>
While I post-process my logs, I want to be able to do queries that I
came up with on the spot, that work on historic data without having to
re-process past logs. Of course, this only has to work for fields that
I actually extracted. If I want to introduce a new field, I'll either
re-process old data, or only care about new logs.
</p>

</li>

</ul>

<a name="requirements/the-big-one"></a>
### The Big One

A more complex setup is a tiny bit bigger. The bulk of it is a
five-node cluster of some beefy machines, which collect about 100Gb of
raw logs per node a day from the main application-group running on
them.

The requirements are similar:

<ul>
<li><strong>We need one central place to store the logs.</strong>

<p>
  We do not care - nor store - logs on each computer. They all send to
  a central server, and only hold logs themselves if the central is
  unreachable. The central storage is redundant, and the local
  machines can buffer half a day's worth of logs easily. The buffered
  logs are only there to be forwarded to central, they are never
  processed in-place.
</p>
</li>

<li><strong>We need to preserve all logs in an efficient way.</strong>

<p>
Due to policies and requirements, all that data needs to be stored. We
do not need the raw data, but post-processed logs must be stored and
preserved for years.
</p>
</li>

<li><strong>We post-process data, and store a structured, processed version only.</strong>

<p>We take all our logs, may they come from syslog, the Journal or any
other source, and post-process them. We extract out key fields,
correlate messages, and so on. We're only interested in this part of
the data, the original messages are discarded. </p> </li>

<li><strong>We want to do queries that span programs and machines.</strong>

<p>
Because the services we run are distributed across machines, different
parts of the same event may be handled by different nodes. We need to
be able to query the logs that belong to the whole session: that may
span multiple machines and programs.
</p>
</li>

<li><strong>I want reasonably fast and efficient ad-hoc
queries.</strong>

<p>
While we have alerts on many things, and the systems will notify us of
issues we thought of or saw in the past, we need to be able to do
queries that were not pre-written. We may need to go back a few
months, and we need an answer in a timely manner.
</p>

</li>

</ul>

<a name="why-not-text"></a>
Why not text?
-------------

<a name="why-not-text/the-small-system"></a>
### The Small System

On the small system, there are two issues with storing logs in a text
format:

<ul>
<li><strong>Text is inefficient for structured, typed storage.</strong>

<p>Once my logs are structured and typed, to store them in a text
format, I'd have to serialise them, and then deserialise them when
processing them further. That's a waste of resources. I'd also lose
the main benefit of textual logs: human readability.</p>
</li>

<li><strong>Ad-hoc queries on text are hard.</strong>

<p>Many opponents of binary log storage cite grep and regexp as a way
to search one's logs. That works fine if your queries are simple, or
where you can pre-filter your logs. In my case, I can set up a few
rules, yes, and in that case grep would be sufficient. But when I want
to query things that came up on the spot, I'd be limited to regexps,
mostly.</p>

<p>The trouble with regexps is that they are rarely human readable,
and iteratively constructing one is an art of its own. I'm much more
comfortable working with a system that serves me, than me having to
serve the system by translating a query to regexps.</p>

<p>Of course, I could use a program to index my logs, and search using
that. But then, I'd have to use a special program to do the
queries... I may as well just scratch the text logs alltogether,
because I'm never going to look at them raw anyway.</p>

</li>

</ul>

<a name="why-not-text/the-big-system"></a>
### The Big System

Now on the big system, there are even more issues with using text to
store the logs, on top of the two listed above (which apply to the big
system too)!

<ul>
<li><strong>Text is verbose.</strong>

<p>Like it or not, text is verbose. While you can compress it, that is
most efficient when you do it in bulk, at logrotate time most
often. That eats a noticable amount of resources. With a binary log, a
lot of the data can be stored more efficiently to begin with (think
numbers, uuids, timestamps), because one does not need to preserve
human readability.</p>

<p>It is a lot easier to compress data when you always have your index
in memory at all times. You rarely have that with text.</p>
</li>

<li><strong>Grepping gigabytes does not scale.</strong>

<p>
Our sessions may span hours, machines and processes. There is no way
we can store that in such a way that it would be small enough to grep
through, yet, would contain all the data we need.</p>

<p> If we have all processes logging to a single file, then we will
need to split the files either by size or by time. In that case, to
find all logs of a session, we may need to grep through many gigabytes
of files for naught (because the session was idle for an hour, for
example). Not to mention, that we will first have to figure out when
the session started, and that's pretty much trial and error, unless
you have that information stored elsewhere.</p>

<p> If we split by application, we will have to manually correlate
messages. And we'll have to split by time or size too, anyway.</p>

<p>If we split by session, then finding all logs for a user will span
a lot of files. We will also need to split by time or size too,
because a session can generate enough logs to make it inefficient to
store it in a single file.</p>

<p>If we duplicate logs, and store them in different ways to support
different kinds of queries, then we'll run out of disk space in no
time. It is also inefficient to do that when you can do better.</p>

</p>

</li>
</ul>

<a name="example-uses"></a>
Example uses
------------

While I'm not allowed to discuss the details of the big system, I am
happy to share some of the things I do with my own, personal logs.

<ol>
<li><strong>Follow the trail of an e-mail.</strong>

<p>From when I started composing it in Gnus (and a `message-id` was
generated for it at that time), I can follow the trail of an e-mail
until it leaves my systems. I can see how much time it spent at each
stage, how headers were modified, whether it ran into any issues. If
it bounces back, I'll see that too, with the same query.</p>

<p>This spans at least two machines, and three programs.</p>

</li>

<li><strong>Find all the pages someone visited at site X, coming from
Y.</strong>

<p>I have a few sites that link to each other, and sometimes I'm
curious how many pages of site X someone visited, after having gone
there from site Y.</p>

<p>I can ask my system to find such pages by finding the start of a
session that has site Y in the referrer, and list me the pages of that
session.</p>

</li>

<li><strong>Find most the logs for my user session yesterday.</strong>

<p>
Suppose I want to work with the logs of yesterday. As an initial step,
I'd like to find them all, but only those that were generated by
programs that started from within my login sessions. Stuff that ran
under my own user id, but were started by cron, should not be
returned. Then, I'd like to filter out logs coming from Banshee.
</p>

<p>
This will span a lot of programs.</p>
</li>

<li><strong>Which sites did my mother visit last month?</strong>

<p>A lot of times, my mom asks me what this-and-that site she visited
last month was, and to answer her questions, I turn to my logging
system. I can query my proxy logs for sites visited during times my
mother was logged in, from her device.</p>

<p>This is about as simple as <code>program="proxy" and client="mom's
device" date="2015-04-01 to 2015-05-01"</code>.</p>

<p>Not a terribly hard query, but this lays the foundation for the
next query...</p>

</li>

<li><strong>Which sites did my mother visit during the past 6 weeks,
where she spent more than an hour browsing?</strong>

<p>For this, I'll need my search engine to aggregate some visits. It
also spans at least two months, a large number of sessions.</p>
</li>

</ol>

<a name="countering-the-anti-binary-reasons"></a>
Countering the anti-binary reasons
----------------------------------

### Binary logs are fragile!

Actually, that is not true. If your binary log is append-only, if it
gets corrupt, you can still extract the valid parts. Granted, you will
need a tool for that, but I don't see that as an issue.

As an upside, it is much easier to notice that your binary log is
corrupt. It's also easier to validate it, to cryptographically sign
it, and so on.

Text logs can get corrupted too, I've seen that happen plenty of
times. It's not any harder to restore the good parts of a binary log.

### Binary logs need their tools!

Yes. And the problem with that is what, exactly? For text logs, you
need your tools too: grep, less and whatever else you may want to
use. You will also need to know your log format, to know what to grep
for.

With a binary log, you can store structured data more easily, in a
more search-friendly manner. If you know you want logs that deal with
a particular e-mail, you can search for the `message-id` field, and
you're done. With text, you'll have to construct a regexp that matches
only the `message-id` field, and nothing else. Doable, but not as
trivial.

### Binary logs lock you in!

If you use a solution that lock you in - yes. You don't have to.

### Text is the Unix way!

Yes, like `wtmp`, `utmp`? Like all the databases?

Text was fine when you had a single machine, with a number of
independent applications which you could separate into different log
files. But once you have a whole system, with inter-dependent
applications, you really need to index the logs if you want ad-hoc
queries.

### You can have a binary index and text logs too!

You can. But what's the point? You will need a special tool to query
your logs anyway. May aswell reap the benefits of binary storage then.

### Binary logs are a new, passing fad!

I'm afraid that is not so. People have been abusing SQL to store their
logs for more than a decade. It's not going to go away. With the
advent of non-relational databases and search engines, there are even
better tools for log storage and search than in ancient times when
people started using SQL for logs.

### Binary logs are opaque!

Just as much as text logs. While text logs are *human readable*, they
are only useful if you know how to make sense of them. If you know
the exact logging format of your application, then you can understand
your logs. If you don't, it's just a sequence of words and symbols.

For example, which field is the referrer in this log line?

<pre>
127.0.0.1 - - [04/May/2015:16:02:53 +0200] "GET / HTTP/1.1" 304 0 "-" "Mozilla/5.0"
</pre>

I don't know about you, but I prefer `referrer="-"` over `HTTP/1\.1"
\d+ \d+ "-"`. Of course, you could pre-process your logs and store
them in, say, JSON, and then use `jq '.referer="-"'`, but that'd be
terribly slow on a large log file. Also, JSON is not friendly to the
human eye, and requires a special tool to search in, too.

### But you are doing text logs wrong!

This one is the most frequent thing I hear.

"*If you have 100Gb of logs a day, you are doing something wrong, and
you're logging too much.*" - I'm sorry, but deciding how much and what
we log is not your job. Its ours, and this is the amount we have to
deal with.

"*Just split the logs into smaller files, so they're easier to
grep!*" - Yeah, no. File size isn't the problem. The problem is the
amount of data. We'd need to limit how much data we grep through,
because hundreds of gigabytes is way too much, especially if we search
iteratively, refining (and possibly extending, not just narrowing) the
search. There's no way to reasonably split the files (see the
[requirements](#requirements/the-big-one) above), we need an index.

"*Just learn regexp properly!*". Oh boy, where do I start?! Regexps
are great for matching text. My data is not text, though, but a
structured set of values. I'd basically have to parse the
representation in the regex too! Or, I'd need to use a special tool,
which defeats the **just use grep!** argument anyway. And frankly, a
two-line regexp is **not** human readable, yet, that's the average
length of a regexp we'd need for some of the searches we do: we
tried. We can simplify the regex by splitting the search into multiple
steps, but that has a serious impact on response time and resource
use, so we're not going to do that.

### Embedded systems don't have the resources!

Some systems are not connected, and having a search engine running on
them, indexing logs would be a colossal waste of time on them. That is
true, and text based log storage is all fine there, if that's your
thing.

I'd still use a binary log storage, because I find that more efficient
to write and parse, but the indexing part is useless in this case. A
syslogd that can write a binary log is not any bigger than one that
can't, nor would it require more resources. What would require more,
is tools to access the logs, but for such small data, all you need is
a logcat program, which is maybe 15-20k, if not less. For most devices
that store their own logs, this ain't much. If it is, you shouldn't
store logs on the device, either.

### Text is easier to browse through!

No, it isn't. Browsing logs with less is an incredibly boring and
daunting process, especially when you have a lot of them. Besides,
browsing binary logs isn't any harder, either. Instead of `less`, you
will use `logcat` or something similar. You can follow the incoming
queue like if you were using `tail -f`, you can do everything. You may
need to use a differently named tool, oh my, what a huge issue!

<a name="closing-thoughts"></a>
Closing thoughts
----------------

Over the past few years since I stopped using text based log storage,
I felt the need to turn to raw text logs exactly... never. The search
and browse tools I have available are an order of magnitude more
convenient than grep and less: I do not need to remember the structure
of my logs, I do not need to bake that knowledge into every regexp,
and my tools are a lot smarter too. It became easier to discover
events which I should add alerts for, it became easier to see trends,
it became easier to make sense of the data I have.

Why? Because I let the computer do the work for me, and to make that
more efficient, I store the logs in a format the computer can
handle. I won't look at the raw data anyway, I have no need for that.

Since I'm a lazy person, I prefer if the computer does the work, and I
enjoy the benefits. I'm not going to lift a finger to write a regexp
if I can avoid it, and instead ask in a more understandable query
language.

You see, the main difference between me and proponents of text-based
log storage is that I want my queries and their results to be human
readable, and I don't care how that is accomplished. They, on the
other hand, want their raw data to be human readable, and would
sacrifice convenience for the sake of being able to keep data
textual.

Sorry, I prefer convenience and efficiency over dogma.
