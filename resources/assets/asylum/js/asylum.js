$(document).ready(function () {
  $("a[rel=tooltip]").tooltip();
  $(".rel-tooltip").tooltip();
  $(".row-fluid *[rel=tooltip]").tooltip();
  $("a[href=#distro-debian-wheezy]").tab('show');

  $(".madness-share-twitter").attr("href", "https://twitter.com/share?via=algernoone&url=" + encodeURIComponent(document.location.toString()) + "&text=" + encodeURIComponent(document.title));
  $(".madness-share-google-plus").attr("href", "https://plus.google.com/share?url=" + encodeURIComponent(document.location.toString()));
  $(".madness-share-flattr").attr
    ("href",
     "https://flattr.com/submit/auto?user_id=algernon&title=" + encodeURIComponent(document.title) + "&category=text&url=" + encodeURIComponent(document.location.toString()))

  function load_page(o) {
      var x = o.attr("href");
      if (x) {
          document.location = x;
      }
  }

  Mousetrap.bind('j', function () {
      load_page($(".next a"));
  });

  Mousetrap.bind('k', function () {
      load_page($(".previous a"));
  });

  Mousetrap.bind('d', function () {
      load_page($("#madness-article-date"));
  });
    /*
  $(".madness-article-frame").focus();
     */
});
