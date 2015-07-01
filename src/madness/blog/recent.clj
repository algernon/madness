(ns madness.blog.recent
  "## Recent posts on archive & index pages

  The archive pages - along with the site index - will display a
  number of recent and (in case of archive pages) archived posts. The
  snippets herein lay the foundation for rendering those on all these
  pages.

  One important thing to note, is that recent and archived posts are
  not displayed on non-desktop resolutions, unless we're rendering an
  archive page. That is, on a smartphone, the index page will not have
  any recent posts shown."

  ^{:author "Gergely Nagy <algernon@madhouse-project.org>"
    :copyright "Copyright (C) 2012-2013 Gergely Nagy <algernon@madhouse-project.org>"
    :license {:name "Creative Commons Attribution-ShareAlike 3.0"
              :url "http://creativecommons.org/licenses/by-sa/3.0/"}}
  
  (:require [net.cgrand.enlive-html :as h]
            [madness.utils :as utils]
            [madness.blog.post :as blog-post]
            [madness.config :as cfg]))

;; The first nippet to care about is the tag list of a recent post
;; item. We use the element with a `tag` class under the
;; `#recent-posts` element of the template as a basis for the snippet.
;;
;; We replace the `href` (as it should be an anchor), and pick out a
;; `&lt;span>` element from under it, and replace that with the tag's
;; name. The reason we use a separate element for the tag name, is
;; because we want to allow the template to also add an icon somewhere
;; onto the tag button.
;;
;; Madness does not enforce the icon, but makes it possible to use one.
;;
;; This snippet is for a single tag, it will need to be cloned for all
;; tags, see later!
(h/defsnippet recent-post-tag (cfg/template) [:#recent-posts :.tag]
  [tag]

  [:a] (h/do->
        (h/remove-class "tag")
        (h/set-attr :href (utils/tag-to-url tag))
        (h/after " "))
  [:a :span] (h/substitute tag))

;; We'll use the element with a `recent-post` class as the basis for a
;; single recent post item. This should have a header (&lt;h2>), and
;; an link under it - the anchor's `href` and textual content will be
;; rewritten appropriately.
;;
;; The same `recent-post` element must also have a child with a
;; `post-date` class, whose content will be substituted by the date
;; the blog post was made upon.
;;
;; Then there is the paragraph, with a class of `summary`, which will
;; be replaced by the blog post's summary, and of course the `tag`
;; classed element will be replaced by the list of tags
;; (`recent-post-tag` above cloned for all tags).
;;

(h/defsnippet blog-recent-meta (cfg/template)
  [:#madness-recent-article-meta]

  [post]

  [:#madness-recent-article-date] (h/do->
                                   (h/set-attr :href (utils/date-to-url (:date post)))
                                   (h/content (utils/date-format (:date post))))
  [:#madness-recent-article-tags :a] (h/clone-for
                               [tag (butlast (:tags post))]
                               (h/do->
                                (h/substitute (blog-post/blog-post-tag tag))
                                (h/after ", ")))
  [:#madness-recent-article-tags] (h/append
                            (blog-post/blog-post-tag (last (:tags post))))
  [:#madness-recent-article-tags] (h/remove-attr :id)
  
  [:#madness-recent-article-meta] (h/remove-attr :id))

(h/defsnippet recent-post-item (cfg/template) [:#madness-archive-recent-post]
  [post]

  [:#madness-archive-recent-post] (h/remove-attr :id)
  [:h3 :a.madness-post-article-link] (h/do->
                                      (utils/rewrite-link-with-title
                                       (:url post)
                                       (:title post))
                                      (h/remove-attr :class))
  [:h3 :a.madness-post-section-sign] (h/do->
                                      (h/set-attr :href (:url post)))

  [:#madness-recent-article-meta] (h/substitute (blog-recent-meta post))
  [:#madness-recent-article-summary] (h/substitute (:summary post)))

;; And finally, we are now able to assemble a whole row of recent
;; posts! We'll use the `#recents` element as a basis for our
;; template.
;;
;; Since there can be multiple rows, we'll remove the id, and for each
;; item on the row, we'll clone the `recent-post-item` snippet,
;; replacing the `.recent-post` element in the template.
;;
;; If we're rendering an archive, we'll also remove the
;; `visible-desktop` class, since archives should be visible on
;; non-desktop resolutions too.
(h/defsnippet recent-post-row (cfg/template) [:#madness-archive-recent-post-row]
  [posts archive?]

  [:#madness-archive-recent-post-row] (h/remove-attr :id)
  [:#madness-archive-recent-post]
    (h/clone-for [p posts]
                 (h/do->
                  (h/substitute (recent-post-item p))
                  (h/set-attr :class (str "col-xs-" (cfg/archive-posts :span)
                                          " span" (cfg/archive-posts :span)))
                  (h/remove-attr :id))))
