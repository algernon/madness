---
title: "Walking in my (Electron) shoes"
date: 2018-10-26 11:30
tags: [Rants, Technology]
---

A few of my readers might perhaps know that I'm working on a graphical configurator of sorts for the Keyboardio Model01, called [Chrysalis][chrysalis]. It is built on top of Electron. Even though it is a niche project, one that doesn't even have a usable alpha yet, I got called out for building on Electron a few times in the not too distant past. I've also seen pretty much every Electron-based project receive disproportionate amounts of hate, for the single reason of being built with it. The usual argument goes like this: "Electron is an insecure, bloated piece of shit, that is also a resource hog! Just build native apps instead!"

This is so short-sighted elitism that it makes my blood boil. For future reference, I'll break it down why.

 [chrysalis]: https://github.com/keyboardio/chrysalis-bundle-keyboardio

<!-- more -->

## Context

First, a bit of context about both me and the application I'm writing - both of these are required to be able to properly evaluate the situation. I'm not a UI guy. I'm not even a web developer. The most amount of web-related things I wrote, were in Perl at the turn of the century. I'm much more at home in C/C++ (and Clojure), as far away from the web as possible. I'm reasonably familiar with GTK (including GTK3), and I find my way around QT as well. Web things? I heard about React, and I can copy & paste code. I can read most JavaScript, but writing it is an entirely different matter. I can do it, but I'm at best a junior JS developer. One that doesn't even like neither the language, nor the ecosystem around it.

As for the application, it is a GUI configurator for a niche keyboard that costs upward of $300. I started developing the tool in my free time, because I saw a need for it, and noone else was doing it, so someone had to. I wanted to help all the enthusiastic users of the keyboard who were very supportive of everything related to the keyboard, including the firmware work I did, and my attempts at creating a GUI configurator.

This application is not network-connected, it doesn't run for extended periods of time: you fire it up, change your keymap, then close it. That is the intended workflow. It's also not the only tool we provide. The protocol between the host and the keyboard is intentionally human-readable (at the cost of being much less efficient, but human readability was a strong design requirement), we have CLI tools to work with it. I've been using those tools for well over a year. Point is, the GUI configurator is not the only option. The protocol is in no way tied to the application either. Anyone can write a tool at any time. Heck, there's even an [Emacs package][kaleidoscope.el] that lets Emacs talk to the keyboard!

 [kaleidoscope.el]: https://github.com/algernon/kaleidoscope.el

## Why not native?

For those who assert that I should just write a native application, I ask this: have you actually *tried*? Because I have, and decided it is cost-prohibitive. I did not decide so because I'm a web developer who has expertise there. I decided so *because* I'm familiar with native toolkits and libraries, and they are not an option in this case.

First of all, to write natively, I'd have to port and test on all three major operating systems. While I do have access to OSX and Windows 8, I have near zero experience developing for them, and no desire to learn it. As such, if I went the native way, the application would be restricted to Linux, with any other OS being supported on a best-effort basis. That's already a huge step back from doing it with Electron, where I can easily cross-compile on Linux, and just test the binaries on the target platform. Being Linux-only would make the application useless for a large part of our user base.

But, lets say we ignore the platform problem. I need access to the USB virtual serial port of the device - no problem, libusb can help with that. I can easily talk with the keyboard. Problems arise when creating the user interface: I need to display the current keymap, in a way that is recognisable. That is, I need to draw the real keyboard on-screen. I can't just display 64 buttons in matrix order and be done with it, that'd be a terrible experience. No, I need to draw the keyboard. Alas, that is not a trivial task, because this keyboard is a peculiar one: its keys are custom sculpted, the arrangement non-standard. There is no existing widget, nor is it trivial to build one. I do have an SVG of the keyboard, however! Sadly, while all of the major toolkits support displaying an SVG, none of them support interactivity. As in, I can't bind events to certain paths or areas of the drawing. That makes it hard to create a keymap editor, because how would you select the key to use? There are a number of ways to do this, of course - nothing is impossible. But at a certain point, one will have to ask themselves if it is worth the trouble?

And this is where Electron comes in.

## Advantages of Electron

With Electron, I can display an SVG and bind events to certain paths and regions trivially. I can even make the SVG a part of a React component, and have it update automatically as state changes. This is immensely powerful. What would take a lot of effort in a native app, is doable in a few lines of JavaScript with Electron. It's trivial.

With Electron, even if I'm a junior JS developer, development speed is quite fast, because the tooling around React and Redux are amazing. The React and Redux developer tools are unmatched, there's nothing like them for native toolkits. The ability to move around in time and observe behaviour is invaluable.

With Electron, I can easily target all three major platforms, with a lot less headache.

## Why not WebUSB?

Since this is specifically about an USB keyboard configurator, one may rightfully ask why we were not going with WebUSB. The application would still not be native, but at least we wouldn't be tied to Electron. That's actually a great question, one most people complaining about Electron fail to ask (they never ask). We did evaluate WebUSB, but apart from Chrome and Opera, nothing else supports it, so we traded Electron for Chrome. Not a particularly useful trade. Seeing as Mozilla is not keen on implementing WebUSB, I don't see much point in using it.

But even if there were other browsers supporting it, it requires non-negligible amounts of code on the firmware side. We do not have enough space on the keyboard to support WebUSB. We could support it optionally, so one would have the choice between the current firmware-side implementation and a WebUSB-enabled one (but never both at the same time), but then we'd have to maintain two plugins for the firmware, and two set of tools for configuration. That's... not a reasonable option, so WebUSB is out too.

## Short-sighted elitism

What makes my blood boil about this whole "just write native apps!" mantra is that proponents ignore all the reasons one may have for choosing Electron. They ignore development costs, speed, distribution, experience, just to name a few things. To match what I can provide with an Electron application, I would have to:

- Learn how to write native apps for OSX and Windows. Even if I use GTK or QT, there's plenty of OS-specific stuff in there which I'd need to write, and therefore learn first.
- I'd have to figure out how to properly distribute the application, along with its dependencies (because I'm sure as hell not going to build everything from scratch, nor am I going to force my users to install dependencies manually).

Since I already know GTK and QT, the toolkit itself wouldn't be a big issue. I'm also familiar with Linux, so would only need to learn a bit of OSX and Windows APIs, enough to enumerate USB devices, and communicate over a virtual serial port. Doesn't sound much, but as I have zero experience developing for these platforms, and I have no desire to learn them either, this part becomes a bit problematic. One might say that as an app developer, it would be my duty to support all platforms. It's not. There are very few people who are willing to, and are able to develop for multiple platforms. Projects and companies that do multi-platform usually have different people, different teams for the platforms. Why would you expect a single developer to cover all of them?

Then comes distribution. On Linux, there's native OS packages, debs and RPMs. There are Flatpaks, Snaps, and AppImages. All with their pros and cons, and everyone wants a different one. This doesn't scale. Even if I choose one (most likely Flatpak), I'd still have to learn how to build those, and then maintain them. On Windows and OSX, I don't even have an idea how I would approach distribution. I'd need to learn at least two things, on operating systems I'm not familiar with, just to be able to distribute the app.

I wonder how many of the haters are capable of shipping a native application on multiple platforms. A non-trivial application that talks to real hardware, and has dependencies outside of the toolkit.

In contrast, for Electron, I had to learn one language (JavaScript), which I already played with over the years, it wasn't exactly new. My first bigger project in it, but learning a new language isn't that big a deal. Native apps would have an edge here, because I'm more familiar with C and C++, so would have needed to learn less. I had to learn a toolkit (React + Redux), but that wasn't a big deal either.

For Electron, I don't need to care much about portability, because the libraries I use already provide that for me. I don't need to care much about distribution, because Electron comes with tooling that helps with that. I don't need to care about setting up a cross-compiling environment, because Electron has tools to help with that. It also comes with developer tools that none of the native toolkits have: the React and Redux developer extensions are unparalleled. They make development time significantly faster, which means I can ship a useful application to users sooner.

If I had to go native, I would need to spend considerably more resources on development. Development pace would be much, much slower, and less platforms would be supported. Would all those constraints be worth it? What would we gain?

Something a bit less resource hungry? Yeah, that certainly is a great thing, if the app doesn't suck up all RAM. But this thing won't be running for hours. Start up, do your thing, shut down, done. Besides, it is for a $300 device. Chances are, if you can afford the device, you have a computer than can run an Electron app for 15 minutes every once in a while. If you don't, there are still alternative tools - albeit all CLI.

Something that looks native? Nah, it won't look native. It may have native borders, native-looking buttons, but the primary interface, the keymap editor, will not look native. Because there are no native widgets for this kind of thing, and it *needs* to look like the hardware anyway. Besides, if written in GTK or QT, even if I'd make the keymap editor look native, it would only look native in a GTK or QT environment. Anywhere else, it would feel as alien as the Electron app. Have you seen any QT app under GTK or vice versa? Do they look native? They don't. Now imagine the same thing under Windows. Or OSX. To look truly native, I'd have to write at least three different apps. Four, if I want to support GTK and QT with native looks.

Another complaint I hear often is that by using Electron to speed up development, I'm pushing the costs down to every user. That is true to some degree, indeed. The application will use more resources, and have all kinds of downsides. But at least it exists, and exists for more than a single platform. If my users would need to choose between a slightly bloated Electron application or no application at all, they'd go with Electron any time of day.

You see, one of the things the Electron hating crowd ignores is that if it weren't for Electron, some applications wouldn't even exist, because in many cases, native is cost-prohibitive. It's not a choice between Electron and native. It's a choice between Electron or nothing at all.

## To the haters

If you are one of those who refuses to use Electron apps - it is your choice, and I respect that. I too, prefer to use native applications if available. I too, would prefer writing native applications, and I do when that's the right choice, when I can afford it.

Before going on a rampant hate campaign against all things Electron, do consider what it would take to go native. Do you have the resources, the knowledge, and the manpower to develop for all three major platforms? Do you really think that a small team, with very limited resources, would be able to pull off writing native applications for all three major platforms? Do you think they did not consider going native? Do you believe they did not do due diligence before settling on Electron? Think again.

Even if we consider big companies with thousands of employees and plenty of money and other resources, native may still be a cost-prohibitive option. Consider Slack. They already built a web app, it works, looks the same across all platforms. Why would they invest time and other resources into writing a native application? What benefit would that have? They'd have to maintain the native ones in addition to the web application. Completely separate code bases. Why? Because resource use would be smaller? Doesn't matter. Their target audience - paying customers - have the resources to buy their employees machines that are able to run Slack, webapp or Electron. There is exactly zero reason for them to spend the effort. They would not gain enough new paying users to justify the effort of not only developing native applications, but then distributing, maintaining and supporting them. Maintenance and support costs are not negligible. And if you don't provide those, then the native application is useless.

One might say that they should have built an API first, and native applications on top of it to begin with, they wouldn't need to deal with the 'legacy' web app that way. Ask yourself this: at that point, did Slack have the amount of employees, money, and resources to do that? Do you think that developing native Windows, OSX, Android and iOS applications at the start would have been efficient use of their time and resources? Do you think they would have achieved more (or even the same) reach they did with starting with a web application? Because I don't think so.

Keep in mind, that companies are for-profit. They're not there to make you happy, not there to cater to your whims. They're out there to make profit. There are a few who are generous enough to do things that don't net them money in the short term, but that's an exception. And even then, there must be something the company is trying to gain. Be that trust, or an initial set of users - there's always something. Is this a sad status quo? Yes, it is. Go ahead and change it. I'll wait.

Meanwhile, I will provide my users with what they *need*, with technologies that make it possible for me to provide that service. You see, my first priority are my users: they want a tool to be able to change they keymaps without having to compile a new firmware. *Everything* is better than that, even an Electron app.

Before complaining about Electron again, consider all of the above. Before spewing your hate on developers who chose to use Electron, look at what led to that decision. Electron is not perfect - far from it - but unconditional hate towards anything Electron is counter productive. For those of us who ended up working with it for whatever reason, the near-constant stream of hate is incredibly discouraging. We are painfully aware of all the drawbacks. We did not choose it blindly. We did not choose it because we don't want to learn native toolkits. We did not choose it because it's the current fad. We did not choose it because we hate you. We did not choose it because we don't care about RAM. We did not choose it because we don't care about security. We did not choose it because we are too dumb to use anything else. We chose it because we evaluated all other options, and deemed them unfit for the purpose. We are aware it's not going to work for every user - but neither would native applications. Software development is full of compromises, Electron is just one of those compromises.

Before you hate on someone, try walking in their shoes first.
