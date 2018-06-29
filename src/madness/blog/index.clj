(ns madness.blog.index
  "## Rendering the index page

  The index is the root document of the generated site, it has a
  featured - the most recent - blog post, and a list of recent items,
  but no archived ones."

  ^{:author "Gergely Nagy <algernon@madhouse-project.org>"
    :copyright "Copyright (C) 2012-2013 Gergely Nagy <algernon@madhouse-project.org>"
    :license {:name "Creative Commons Attribution-ShareAlike 3.0"
              :url "http://creativecommons.org/licenses/by-sa/3.0/"}}

  (:require [net.cgrand.enlive-html :as h]
            [madness.blog :as blog]
            [madness.blog.nav :as blog-nav]
            [madness.utils :as utils]
            [madness.config :as cfg]
            [madness.blog.recent :as blog-recent]
            [madness.blog.post :as blog-post]
            [clojure.string :as str]))

;; The featured article has a "Read on" button, which is the
;; `#madness-article-read-more` element. It should be, or should have
;; an `a` child, whose `href` this snippet sill replace, and remove
;; the id.
(h/defsnippet index-read-on (cfg/template) [:#madness-article-read-more]
  [post]

  [:#madness-article-read-more :a] (h/set-attr :href (:url post))
  [:#madness-article-read-more] (h/remove-attr :id))

;; ### Putting it all together
;;
;; The index page has a featured article, in the `#madness-article`
;; element, a set of recent posts in the `#madness-recent-posts`
;; element, generated using the tools provided by [blog.recent][1].
;;
;; There are no neighbours, no archive, no comments, but the global
;; tag and recent post lists are, of course, filled out, using the
;; functions provided by [blog.nav][2].
;;
;; [1]: #madness.blog.recent
;; [2]: #madness.blog.nav
;;
(h/deftemplate blog-index (cfg/template)
  [blog-posts _]

  [:#madness-article :h2] (h/substitute (blog-post/blog-post-title (first blog-posts)))
  [:#madness-article-content] (h/substitute (:summary (first blog-posts)))
  [:.madness-article-meta] (h/substitute
                            (blog-post/blog-post-meta
                             (first blog-posts)
                             (remove #(.startsWith % ".") (:tags
                                                           (first blog-posts)))))

  [:#madness-article-read-more :a] (h/set-attr :href (:url (first blog-posts)))
  [:#madness-article-read-more] (when-not (empty? (:content (first
                                                             blog-posts)))
                                  identity)
  [:#madness-article-read-more] (h/remove-attr :id)

  [:#madness-article-comments] nil
  [:#madness-article-neighbours] nil
  [:#madness-article-share] nil

  ; Navigation bar
  [:#madness-recent-posts :li] (blog-nav/recent-posts blog-posts)
  [:#madness-recent-posts] (h/remove-attr :id)
  [:#madness-tags :.madness-tag] (blog-nav/all-tags blog-posts)
  [:#madness-tags] (h/remove-attr :id)

  ; Index
  [:#madness-archive-recent-posts] (h/remove-attr :id)
  [:#madness-archive-recent-post-row]
    (h/clone-for [rows (utils/blog->table
                        (cfg/recent-posts :columns)
                        (cfg/recent-posts :rows) (rest blog-posts))]
                 (h/do->
                  (h/substitute (blog-recent/recent-post-row rows false))
                  (h/before utils/hr-desktop)))

  [:#madness-archive-archived-posts] nil

  ; Misc
  [:.pygmentize] utils/pygmentize-node

  ; Cleanup
  [:#main-rss] (h/remove-attr :id)
  [:#main-css] (h/do->
                (h/remove-attr :id)
                (h/replace-vars (cfg/vars)))
  [:#rss-feed] (h/remove-attr :id)
  [:#madness-content-area] (h/remove-attr :id)
  [:#madness-article] (h/remove-attr :id)
  [:.no-index] nil)
