---
title: "Grepping logs is still terrible"
date: 2015-05-07 11:50
tags: [Technology, Rants]
---

The other day, I wrote a controversial piece about why
[grepping logs is terrible][blog:grep], the reactions were
interesting. I've got some great feedback (thanks!), and there were a
lot of hostility too. I've been called incompetent, lazy, stupid and a
systemd-fanboy and various other things. By people who didn't read
past the second paragraph, no less. But this is something I
expected.

[blog:grep]: /blog/2015/05/05/grepping-logs-is-terrible/

Today, I will try to address some of the criticism, and point out a
few things certain people likely missed in the original article.

I'll start with this: I'm not advocating neither systemd, nor the
journal. I thought I made that clear in the previous post, but it
seems it needs to be repeated. Neither of my solutions outlined
previously rely on the journal (or systemd). One of them has no trace
of systemd in it at all, the other has journald here and there, but
with storage turned off. It's not an essential part of the system, and
I'd be happier without it, but for my home system, it doesn't matter.

Lets look at some of the other criticism!

<!-- more -->

<a name="but-the-journal"></a>
### *But the journal...*

That's the journal's problem. The journal can be useful at times (I
like that `systemctl status foo` shows me the last few loglines), it
has nothing to do with neither my previous post, nor this one. Stop
assuming I use systemd or the journal.

<a name="get-by-with-grep"></a>
### *Most of us get by with grep just fine*

Yes, and I could shave with a knife. I could also use that knife to
butter my bread, and do a lot more with it than with my electric
razor, which is pretty much only good for shaving my face, costs more
to buy, and to maintain. Not to mention my son won't inherit my razor,
but a good knife would make a great family heirloom. Yet, I use a
razor, as do a lot of other people.

That's the difference between using grep and a dedicated log search
tool.

<a name="because-title-matters"></a>
### *"Software engineer" - clearly a lazy developer!*

Oh, yes. Because my title at my day job happens to be "Senior Software
Engineer", I'm clearly not - and have not been - an admin. I'm afraid
I have to disappoint you: I have far more experience in operating
systems than in developing them. That, and title means nothing. Even
when I was working at an ISP, as an admin, my title was Software
Engineer. When I worked at BalaBit's support department, my title was
Software Engineer.

I am lazy, though, and I'm not afraid of admitting that. It's a
terrific trait to have, and I wish more people would be as lazy as I
am. You see, I want computers to work **for** me. I don't want to do a
task that the computer can do better, my time is better used for
things the computer isn't good at.

<a name="implementing-logrotate"></a>
### *Grepping gigabytes is wrong, implement logrotate!*

As explained in the original article, the size of the file is not an
issue. I can rotate them, no biggie. But if what I'm searching for
needs multiple files, I will have to grep through gigabytes anyway,
one file or not.

The amount of data I am searching in is often huge, because the time
range I need to find information in is long. There's no way around
that, an no amount of log rotation will help - on the contrary, it
will just make things worse.

<a name="use-elk"></a>
### *Just use ELK or Splunk*

I am using something similar. Both store their data in binary, and
that's exactly my point.

<a name="use-a-db"></a>
### *He should use a database!*

I am. Does database store the data in text files? No? That's my point.

<a name="enterprises-not-using-sql"></a>
### *I have worked at enterprises that didn't use SQL for logs.*

And I have worked with a number of them - and helped even more - that
do. Or if not SQL, something else, ranging from MongoDB, through
Cassandra, Riak, ElasticSearch and a number of others, or even a
combination of these tools.

Then there are [syslog-ng PE][sng:pe] customers, or BalaBit's
[syslog-ng Store Box][sng:ssb] who use a custom binary logfile
format. You may think binary log storage in enterprise is low - it's
not.

 [sng:pe]: https://www.balabit.com/network-security/syslog-ng/central-syslog-server
 [sng:ssb]: https://www.balabit.com/network-security/syslog-ng/log-server-appliance

Just because you didn't meet any that used something else than text,
doesn't mean that text is superior, or that binary log storage is just
something crazy people come up with. I've never seen a million dollars
in cash, either. Yet, I'm willing to believe there are people who have
that much, in cash.

<a name="learn-regexes"></a>
### *Learn regexes, damnit!*

While I can understand the following regexp:

<div class="pygmentize" data-language="perl">
/(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*
  |  "(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]
      |  \\[\x01-\x09\x0b\x0c\x0e-\x7f])*")
@ (?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?
  |  \[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}
       (?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:
          (?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]
          |  \\[\x01-\x09\x0b\x0c\x0e-\x7f])+)
          \])/
</div>

...and I find it more succinct and easier to understand than
[RFC5322](http://tools.ietf.org/html/rfc5322#section-3.4), I would not
call this human readable.

Not to mention that there are queries which are much easier with a
search engine, than with regular expressions. For example: find all
logs between 2013-12-24 and 2015-04-11, valid dates only. Is it
doable? Yes:

<div class="pygmentize" data-language="perl">
\b(?:
2013-11-(?:3[01]|2[4-9])|
2014-(?:(?:0[13579]|11-(?:3[01]|[12][0-9]|0[1-9]))|(?:0[468]|1[02]-(?:30|[12][0-9]|0[1-9]))|(?:02-(?:0[1-9]|1[0-9]|2[0-8])))|
2015-(?:(?:0[13]-(?:3[01]|[12][0-9]|0[1-9]))|(?:02-(?:0[1-9]|1[0-9]|2[0-8]))|(?:04-(?:0[1-9]|1[01])))
)\b
</div>

Can it be made simpler? Probably, but it won't be much more
readable. It also takes a while to construct it, and you have to
remember how long february is each year. Compare that to this query
instead: `date="[2013-12-14 TO 2015-04-11]"`. Isn't that much simpler?

Of course, you can write a tool to construct a regexp from a date
range, it's not rocket science. But the regexp won't be simple, and
that's only the date part. Now if you add a few other fields to narrow
it down... this is going to be beautiful.

<a name="forcing-binary-logs"></a>
### *Binary logs may be fine for you, but don't force it on us!*

I maintain that grepping logs is terrible, and one should use a tool
that understands logs to search in them. Why? Because computers are
there to help you. They serve you, not you them. If there are tools
and solutions that make my life easier, and the trade offs are
acceptable, I will use them. There used to be log colorizers out
there, that help you make sens of the logs, highlight important
parts. People loved them, likely still do. I still receive occasional
feedback for [ccze](https://github.com/madhouse/ccze), even though I
have not touched it in years. Now tools have evolved, and there are
more convenient ways to work with your logs than the old knife and
fork, so to say.

But nevertheless, there are cases where text logs are the way to
go. And that is perfectly fine, I just don't care about those
cases. If you want to use text, that's your choice, sometimes there
are good reasons for that. I don't envy you, but then, that is my
choice.

What I'm saying is that there are *very* good reasons for storing your
logs in a binary storage format. Be that a database, or some custom
stuff (although the database is almost always a better choice). What I
am puzzled about is why some people are so vehemently against storing
logs in a binary format, yet recommend using a database (which then
goes and stores the data in a binary format), when the volume of logs
is so high.

That's exactly what I'm saying, people!

<a name="formats-and-analysis"></a>
### *Don't conflate logfile formats and logfile analysis*

I don't know about you, but I analyze my logs, not my log files. The
only reasons I care about the storage format are efficiency and
convenience. Since I let the computers do the work for me, I prefer a
format which is efficient for the computer, and interfaces that are
convenient for me.

It just so happens that to achieve these, using a database and
indexing is a reasonable solution, and those store their data in a
binary format.

I'm not advocating for a custom binary logfile format. That'd be
stupid. I'm advocating for people to accept that the *everything is
text* mantra is not always good.

<a name="an-example"></a>
### *It would be much more useful to show an example instead of
ranting*

There are a number of examples that are very similar to mine. The
[ELK stack](http://logstash.net/docs/1.4.2/tutorials/getting-started-with-logstash)
for example is a good start.

But to be sure, below is a simplified configuration to get people
started. You will need a few tools:
[haproxy](http://www.haproxy.org/),
[syslog-ng](https://www.syslog-ng.org/) (and the
[Incubator](https://github.com/balabit/syslog-ng-incubator)),
[elasticsearch](https://www.elastic.co/products/elasticsearch),
[es2unix](https://github.com/elastic/es2unix) (or something similar).

The idea is that we'll use syslog-ng to collect the logs from all
kinds of sources, then forward it to elasticsearch for indexing and
storage. We'll use haproxy to send to central by default, and to a
local ES when central is unreachable. The `es2unix` tool is there for
queries, but you can also use something like
[kibana](https://www.elastic.co/products/kibana).

The setup below won't do much, it is a starting point only. I may
publish my home config at some point, but as of this writing, it has
so many crude hacks (mostly the message parsing part between syslog-ng
and elasticsearch, which I'm not showing here) that I'm not going to
show it to anyone.

Anyhow, lets start! We'll set up `haproxy` first:

    listen es-fallback 127.0.0.1:19200
       balance roundrobin
       mode tcp
       server central 10.242.42.2:9200 check inter 10s fall 3 rise 3
       server local   127.0.0.1:9200   check backup

Next up is `syslog-ng`! You'll have to install the Incubator too for
this to work:

    @version: 3.5
    @include "scl.conf"
    @include "scl/elasticsearch/plugin.conf"

    source s_system { system(); };
    source s_internal { internal(); };

    destination d_elastic {
      elasticsearch(
        host("127.0.0.1")
        port(19200)
        index("logs")
        );
    };

    log {
      source(s_system);
      source(s_internal);
      destination(d_elastic);
      flags(flow-control);
    };

With a default ElasticSearch setup, this is all you need to get
started. You can experiment with adding further parsing to the
syslog-ng config, or adjust the JSON the elasticsearch plugin emits,
to make it play nicer with Kibana.

If you want to index your apache logs too, something like the
following should help you out:

    parser p_apache {
      csv-parser(
        columns("APACHE.CLIENT_IP", "APACHE.IDENT_NAME", "APACHE.USER_NAME",
                "APACHE.TIMESTAMP", "APACHE.REQUEST_URL", "APACHE.REQUEST_STATUS",
                "APACHE.CONTENT_LENGTH", "APACHE.REFERER", "APACHE.USER_AGENT",
                "APACHE.PROCESS_TIME", "APACHE.SERVER_NAME")
        flags(escape-double-char,strip-whitespace)
        delimiters(" ")
        quote-pairs('""[]')
      );
    };

    source s_apache {
      file("/var/log/apache/access.log");
    };

And then add `source(s_apache)` to the logpath.

To query:

`es search -u "http://localhost:19200/_logs" "PROGRAM:postfix*"`

With this setup, I have all my logs at a central place, unless I'm
offline, in which case it automatically falls back to localhost. There
is no merging between the two, but for my home system, that wasn't a
requirement. The logs are post-processed, and stored in a structured
format (at least partly, I do a lot more, but explaining that is a
topic on its own right). Since all logs are in one place, I can do
queries that span multiple programs and hosts, and the syntax is much
more convenient for a human being than regexps would be. The speed
isn't exactly stellar, we'd need to fine-tune that a lot, but for a
simple system with few logs, this works well enough.

Thus, with about an hour of work, all requirements of my small system
are satisfied.

There are some text files in use - apache logs, namely -, because I
wanted to skip the apache config parts. Those are only used as a
source for log collection though, and are trivial to rotate away into
oblivion. Doing so is left as an exercise for the reader.
