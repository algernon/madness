---
title: Debian repository
---

For some selected packages ([syslog-ng][sng] 3.3, 3.4 & 3.5, and
[syslog-ng incubator][sng-incubator]), I provide up-to-date packages
for a number of [Debian][debian] and [Ubuntu][ubuntu] releases - see
the <kbd>sources.list</kbd> assemby area just below. The packages will
be kept up to date for all platforms, until at least after a month of
the platforms end of life.

To see what changed between versions, please see either of the
[syslog-ng 3.3][1], [syslog-ng 3.4][2], [syslog-ng 3.5][3] or
[syslog-ng incubator][4] changelogs.

 [sng]: https://www.balabit.com/network-security/syslog-ng/opensource-logging-system/overview
 [sng-incubator]: https://github.com/balabit/syslog-ng-incubator
 [debian]: http://www.debian.org/
 [ubuntu]: http://www.ubuntu.com/

 [1]: http://git.madhouse-project.org/debian/syslog-ng/plain/debian/changelog?h=packaging/debian/3.3
 [2]: http://git.madhouse-project.org/debian/syslog-ng/plain/debian/changelog?h=packaging/debian/3.4
 [3]: http://git.madhouse-project.org/debian/syslog-ng/plain/debian/changelog?h=packaging/debian/3.5
 [4]: https://github.com/algernon/syslog-ng-incubator/raw/debian/master/debian/changelog

The repository is signed by the GPG key
[6CE70C6E8B9E0644][archive-key], which one can add to apt with the following command:

    curl http://packages.madhouse-project.org/debian/archive-key.txt | sudo apt-key add -

 [archive-key]: http://packages.madhouse-project.org/debian/archive-key.txt

Packages are available for the **i386** and **amd64** architectures on
both Debian and Ubuntu, but in addition to those, the Debian
repositories also contain packages for the **powerpc** architecture.
If there's enough interest, I can pull in more build machines, and
compile for other platforms as well.

# Choose your components!

There are two easy ways to enable the syslog-ng repositories: one is
to use the form below to select your platform and components and
copy-paste the lines to <code>/etc/apt/sources.list</code>:

<form class="form-inline" id="dist-select">
 <fieldset>
  <select class="inline input-xlarge" id="distro-select">
    <optgroup label="Debian">
     <option value="debian-squeeze">Debian 6.0 (Squeeze; oldstable)</option>
     <option value="debian-wheezy" selected>Debian 7.0 (Wheezy; stable)</option>
     <option value="debian-jessie">Debian 8.0 (Jessie; testing)</option>
     <option value="debian-sid">Debian unstable</option>
    </optgroup>
    <optgroup label="Ubuntu">
     <option value="ubuntu-lucid">Ubuntu 10.04 LTS (Lucid Lynx)</option>
     <option value="ubuntu-precise">Ubuntu 12.04 LTS (Precise Pangolin)</option>
     <option value="ubuntu-saucy">Ubuntu 13.10 (Saucy Salamander)</option>
     <option value="ubuntu-trusty">Ubuntu 14.04 LTS (Trusty Tahr)</option>
     <option value="ubuntu-utopic">Ubuntu 14.10 (Utopic Unicorn)</option>
    </optgroup>
  </select>
  <select class="inline input-xlarge" id="sng-select">
    <option value="syslog-ng-none">No syslog-ng</option>
    <optgroup label="syslog-ng old stable">
     <option value="syslog-ng-3.3">syslog-ng 3.3</option>
    </optgroup>
    <optgroup label="syslog-ng stable">
     <option value="syslog-ng">Latest syslog-ng stable release (3.4)</option>
     <option value="syslog-ng-3.4" selected>syslog-ng 3.4</option>
    </optgroup>
    <optgroup label="syslog-ng devel">
     <option value="syslog-ng-devel">Latest syslog-ng devel release (3.5)</option>
     <option value="syslog-ng-3.5">syslog-ng 3.5</option>
    </optgroup>
  </select>
 </fieldset>
</form>

    deb       http://packages.madhouse-project.org/debian   wheezy   syslog-ng-3.4
    deb-src   http://packages.madhouse-project.org/debian   wheezy   syslog-ng-3.4


The other option is to use a [hosted script][add-release] that figures
out your platform, adds the key to apt keyring, and the appropriate
lines to <code>/etc/apt/sources.list.d/syslog-ng.list</code>:

    curl http://packages.madhouse-project.org/debian/add-release.sh | sudo sh

 [add-release]: http://packages.madhouse-project.org/debian/add-release.sh

<script src="/assets/asylum/js/sources.list.js" type="text/javascript"></script>

<div id="alert-box" class="alert alert-block alert-danger fade in"
     style="display: none">
</div>
