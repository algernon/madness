(ns madness.blog.page
  "## Loading & rendering of individual static pages

  For the sake of ease, static pages are fully loaded first, and
  turned into a structure that is easy to work with. This namespace
  implements the low-level loading, restructuring and rendering of
  individual static pages.

  Static pages are under `resources/pages` by default, and their path
  below that will be reused as-is for the URL of the generated
  page. That is, `resources/pages/foo/bar/index.html` becomes
  `/foo/bar/index.html`."

  ^{:author "Gergely Nagy <algernon@madhouse-project.org>"
    :copyright "Copyright (C) 2012-2013 Gergely Nagy <algernon@madhouse-project.org>"
    :license {:name "Creative Commons Attribution-ShareAlike 3.0"
              :url "http://creativecommons.org/licenses/by-sa/3.0/"}}
  
  (:require [net.cgrand.enlive-html :as h]
            [madness.blog.nav :as blog-nav]
            [madness.utils :as utils]
            [madness.config :as cfg]
            [madness.io :as io]
            [clojure.string :as str]
            [clj-time.format :as time-format]))

(defn- page-url
  "Given a file path, strips the pages directory, and returns the
  result, which will be used as the URL for a given page."
  [path]

  (second (first (re-seq (re-pattern (str ".*" (cfg/dirs :pages) "(.*)"))
                         path))))

(defn read-page
  "Read a static page from a while, and restructure it into a
  representation that is easy to work with.

  Each static page must have an `article` element, where the only
  required child is the `title`. It can also - optionally - have a
  `comments` property, which, when set, will enable commenting on the
  particular page.

  Apart from the `article` element, only a `section` element is
  required, whose contents will be used as the main content of the
  static page.

  The structure this function generates, should be pretty clear by
  glancing over the code here."
  
  [file]

  (let [page (io/read-file file)]
    {:title (apply h/text (h/select page [:article :title])),
     :url (page-url (.getPath file))
     :comments (or
                (-> (first (h/select page [:article])) :attrs :comments utils/enabled?)
                (-> (h/text (first (h/select page [:article :comments]))) utils/enabled?)),
     :content (h/select page [:section])}))

;; ### Static page templates

;; The first thing about a page, is its header, the `h2` element of
;; the `#madness-article` in the template.
;;
;; This snippet uses that element as the title template, replacing the
;; `title` attribute of it, and its textual content with the title of
;; the page itself.
(h/defsnippet blog-page-title (cfg/template) [:#madness-article :h2]
  [title]
  
  [:h2] (h/do->
         (h/content title)
         (h/set-attr :title title))
  [:#madness-article] (h/remove-attr :id))

;; If commenting is enabled for a post, the `#madness-article-comment`
;; element should be left intact, as-is. Otherwise, it will be
;; removed, that is all this snippet does.
(h/defsnippet blog-page-comments (cfg/template) [:#madness-article-comments]
  [page]

  [:#madness-article-comments] (when (:comments page) (h/remove-attr :id)))

;; #### Putting it all together
;;
;; To put a full page together, we alter the page title, disable the
;; recent and archived post areas, along with the
;; `#madness-article-neighbours`, as pages do not have those. We also
;; rearrange the `#madness-article`, and last but not least, fill out
;; the global tag & recent post list, using the tools provided by
;; [blog.nav][1].
;;
;; [1]: #madness.blog.nav
;;
(h/deftemplate blog-page (cfg/template)
  [page all-posts]

  [:title] (h/content (:title page) " - Asylum")
  [:#madness-og-title] (h/set-attr :content (str (:title page) " - Asylum"))
  [:#madness-og-url] (h/set-attr :content (str (cfg/base-url) (:url page)))

  ; Article
  [:#madness-article :h2] (h/substitute (blog-page-title (:title page)))
  [:#madness-article-content] (h/substitute
                               (:content page))
  [:.madness-article-meta] nil
  [:#madness-article-read-more] nil

  ; Footer
  [:#madness-article-comments] (h/substitute (blog-page-comments page))
  [:#madness-article-neighbours] nil

  [:#madness-archive-recent-posts] nil
  [:#madness-archive-archived-posts] nil

  ; Misc
  [:.pygmentize] utils/pygmentize-node

  ; Recent posts & other stuff
  [:#madness-recent-posts] nil
  [:#madness-tags] nil

  [:#madness-content-area] (h/remove-attr :id)
  [:#madness-article] (h/remove-attr :id)
  [:#main-rss] (h/remove-attr :id)
  [:#rss-feed] (h/remove-attr :id))
