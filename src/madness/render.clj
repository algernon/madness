(ns madness.render
  "## High-level rendering

  In this part of the documentation, the various parts of the
  templates will be explained in detail: how they are built up, how
  madness recognises various parts of it, and so on and so forth. This
  is a high-level overview, but pointers will be given to places that
  go into the tinies details.

  For a full-blown example, check the [asylum][1] branch of this
  project, which has templates, posts and pages too.

  [1]: https://github.com/algernon/madness/tree/asylum"

  ^{:author "Gergely Nagy <algernon@madhouse-project.org>"
    :copyright "Copyright (C) 2012-2013 Gergely Nagy <algernon@madhouse-project.org>"
    :license {:name "Creative Commons Attribution-ShareAlike 3.0"
              :url "http://creativecommons.org/licenses/by-sa/3.0/"}}

  (:require [clj-html-compressor.core :as compressor]
            [madness.io :as io]
            [madness.blog :as blog]
            [madness.blog.index :as blog-index]
            [madness.blog.archive :as blog-archive]
            [madness.blog.post :as blog-post]
            [madness.blog.page :as blog-page]
            [madness.blog.atom :as blog-feed]
            [madness.config :as cfg]
            [madness.utils :as utils]
            [madness.resources :as res]))

(defn- compress [html]
  (compressor/compress html {:remove-intertag-spaces false
                             :remove-quotes true
                             :simple-boolean-attributes true
                             :simple-doctype true}))

(defn- render-to-file
  "Render a post or page to a file, using a custom render function."
  [all-posts current-post render-fn file]

  (io/write-out-dir (utils/replace-extension file ".html")
                    (compress (apply str (render-fn current-post all-posts)))))

(defn- render-to-stdout
  "Render a post or page to stdout, using a custom render function."
  [all-posts current-post render-fn]

  (println (apply str (render-fn current-post all-posts))))

;; ### Rendering
;;
;; Every part of the site - at least the HTML parts - have a common
;; structure: a [navigation bar][1], the content area, and a footer,
;; and whatever else the template may contain. These are common to all
;; pages and posts, archives and everything else too.
;;
;; What is inside the content area, varies by what page we're talking
;; about - as it will be explained just below.
;;
;; [1]: #madness.blog.nav
;;
(defmulti render
  "Render a part of the site to files."
  (fn [part & args] part))

;; ### The site index
;;
;; Renders the main entry point of the site into the content area, and
;; saves the result into a file named `index.html`.
;;
;; The content area will consist of the latest blog post, followed by
;; a limited number of recent items: the configuration controls how
;; many of these are displayed at most.
;;
;; Blog entries that are too old to fit, will only be available
;; through the archive, and they're not displayed on the index at all.
;;
;; For more information  about how the content area is  built, see the
;; [blog.index][1] namespace.
;;
;; [1]: #madness.blog.index
;;
(defmethod render :index [_]
  (render-to-file nil res/posts blog-index/blog-index "index.html"))

;; ### The global archive
;;
;; Renders the global archive to a file named `blog/archives/index.html`.
;;
;; The archive is not much more than a title in place of a highlighted
;; post, followed by a limited set of recent posts, then the archived posts.
;;
;; The archived posts are rendered differently than the recent posts
;; (see the [blog.archive][1] namespace!), and by default, their
;; number is not limited. Madness does not implement any kind of
;; archive paging.
;;
;; [1]: #madness.blog.archive
;;
(defmethod render :archive [_]

  (let [dated-archive (utils/group-blog-by-date res/posts utils/posts-by-year)
        sorted-archive (sort #(compare (first %2) (first %1)) dated-archive)]
    (render-to-file sorted-archive sorted-archive
                    (partial blog-archive/blog-archive-grouped "Archive"
                             "/blog/atom.xml" "/blog/archives/")
                    "blog/archives/index.html")))

;; ### The tag archives
;;
;; Each tag has an archive of its own, in files named after the tag,
;; something like `blog/tag/index.html`.
;;
;; These pages are exactly the same as the global archive above,
;; except that in the recent and archived posts area, only those posts
;; are shown, that are tagged with the appropriate tag.
;;

;; To render all the archives for each and every tag, we need a
;; function that can render one.
(defmethod render :tag-archive
  [_ all-posts tag tagged-posts]

  (let [fn (str "." (utils/tag-to-url tag) "index.html")
        dated-archive (utils/group-blog-by-date tagged-posts utils/posts-by-year)
        sorted-archive (sort #(compare (first %2) (first %1)) dated-archive)]
    (render-to-file all-posts sorted-archive
                    (partial blog-archive/blog-archive-grouped (str "Tag: " tag)
                             (str "" (utils/tag-to-url tag) "atom.xml")
                             (str "" (utils/tag-to-url tag))) fn)))

;; And another, that maps through the tags, and using the previous
;; method, renders an archive for each of them.
(defmethod render :tags [_]
  (dorun (map #(render :tag-archive res/posts %1
                       (get res/posts-tag-grouped %1))
              (keys res/posts-tag-grouped))))

;; ### Date-based archives
;;
;; Because posts are rendered into locations based on their creation
;; date, and because it makes it easier to navigate bigger blogs, we
;; need archives for each year, month and date.
;;
;; These dated archives are exactly the same as the global archive,
;; except that the recent and archived posts are limited to a
;; particular date: a year, a month, or a day.

;; To render all the archives for each and every date a post was
;; created on, we need a function that can render one.
(defmethod render :date-archive
  [_ all-posts date dated-posts]

  (let [uri (str "/blog/" date "/")
        fn (str "." uri "index.html")]
    (render-to-file all-posts dated-posts
                    (partial blog-archive/blog-archive
                             (str "Archive of posts @ " date)
                             (str "" uri "atom.xml")
                             uri) fn)))

;; And since all the dated archives follow the same pattern, lets
;; introduce a helper function!

(defn render-dated-archive
  "Group blog posts by date, using the function `f` (which is expected
  to return a formatted date when given a blog post), and render the
  archives for each and every key within the group."

  [render-type f]

  (let [dated-archive (utils/group-blog-by-date res/posts f)]
    (dorun (map #(render render-type res/posts %1 (get dated-archive %1))
                (keys dated-archive)))))

;; With these, we can render daily, montly and yearly archives easily.
(defmethod render :daily-archives [_]
  (render-dated-archive :date-archive utils/posts-by-day))

(defmethod render :monthly-archives [_]
  (render-dated-archive :date-archive utils/posts-by-month)
  (render :daily-archives))

(defmethod render :yearly-archives [_]
  (render-dated-archive :date-archive utils/posts-by-year)
  (render :monthly-archives))

;; And for convenience, we have a function that can be called from the
;; command line, and will generate all of the above dated archives.
(defmethod render :date-archives [_]
  (render :yearly-archives))

;; ### Blog posts
;;
;; Blog posts are pretty simple: they show the post itself, the date
;; it was posted, the tags it is tagged with, and any previous or next
;; articles, for easier navigation.
;;
;; There is no recent post or archived posts area here, but commenting
;; can be enabled on a per-post basis.
;;
;; For more information on how a post is rendered, see the
;; [blog.post][1] namespace.
;;
;; [1]: #madness.blog.post
;;

;; Similar to how it was done with the tag archives before, to render
;; all posts, we must first be able to render one. Fortunately for us,
;; blog posts have an `:url` property, which we can re-use for their
;; filename.
;;
;; By default (see [blog.post[1]), this will be of the
;; `YEAR/MONTH/DATE/title/` format, to which, we append `index.html`.
;;
;; [1]: #madness.blog.post
(defmethod render :post
  [_ all-posts post]

  (let [fn (str "." (:url post) "index.html")]
    (render-to-file all-posts post blog-post/blog-post fn)))

;; And now that we can render a single post, we shall render them all!
(defmethod render :posts [_]
  (dorun (map (partial render :post res/posts) res/posts)))

;; To help cross-posting to other engines, it is useful to be able to
;; render only the content of a post, a fragment, without the rest of
;; the page wrapped around it.
;;
;; These fragments shall be rendered to standard output, as they
;; should not hit the disk by default.
(defmethod render :post-fragment
  [_ post-url]

  (let [post (first (filter #(= (:url %) post-url) res/posts))]
    (dorun (render-to-stdout nil post blog-post/blog-post-fragment))))

;; ### Static pages
;;
;; Static pages are very much like blog posts, except they are not
;; tagged, there is no previous or next page, and their URL is not
;; date based, but determined (see [blog.page][1]) by their place on
;; the filesystem.
;;
;; [1]: #madness.blog.page
;;

;; As always, we render a single page first.
(defmethod render :page
  [_ all-posts page]

  (let [fn (str "." (:url page))]
    (render-to-file all-posts page blog-page/blog-page fn)))

;; Then map through all of them, to render them all.
(defmethod render :pages [_]
  (dorun (map (partial render :page res/posts) res/pages)))

;; ### Atom feeds
;;
;; Atom feeds are a bit different than the HTML representation, the
;; feed has a well defined [spec][1]. We're going to generate two
;; kinds of feeds: a global one, including all blog posts (but only
;; blog posts, not pages), and per-tag feeds, that only include posts
;; for the particular tag.
;;
;; For more information about how a feed is assembled, see the
;; [blog.atom][2] namespace!
;;
;; [1]: http://atomspec.org/
;; [2]: #madness.blog.atom

;; The main feed will be saved into `blog/atom.xml`.
(defmethod render :main-feed [_]
  (io/write-out-dir "blog/atom.xml"
                    (blog-feed/emit-atom (cfg/atom-feed :title) "/blog/"
                                         res/posts)))

;; A single per-tag feed is saved into something like
;; `blog/tag/atom.xml`, similarly to how the per-tag archives were
;; rendered.
(defmethod render :tag-feed
  [_ tag tagged-posts]

  (let [fn (str "." (utils/tag-to-url tag) "atom.xml")]
    (io/write-out-dir fn
                      (blog-feed/emit-atom
                       (str (cfg/atom-feed :title) ": " tag)
                       (utils/tag-to-url tag)
                       tagged-posts))))

;; And as usual, since we can render a feed for a single tag, mapping
;; through all the tags is all it takes to render them all.
(defmethod render :tag-feeds [_]
  (dorun (map #(render :tag-feed %1 (get res/posts-tag-grouped %1))
              (keys res/posts-tag-grouped))))

;; As with archives, we render atom feeds for each year, month and day
;; a blog post was posted on, in a very similar manner the archives
;; are rendered.

(defmethod render :date-feed
  [_ _ date dated-posts]

  (let [uri (str "/blog/" date "/")
        fn (str "." uri "atom.xml")]
    (io/write-out-dir fn
                      (blog-feed/emit-atom
                       (str (cfg/atom-feed :title) " @ " date)
                       uri
                       dated-posts))))

(defmethod render :daily-feeds [_]
  (render-dated-archive :date-feed utils/posts-by-day))

(defmethod render :monthly-feeds [_]
  (render-dated-archive :date-feed utils/posts-by-month)
  (render :daily-feeds))

(defmethod render :yearly-feeds [_]
  (render-dated-archive :date-feed utils/posts-by-year)
  (render :monthly-feeds))

(defmethod render :date-feeds [_]
  (render :yearly-feeds))
