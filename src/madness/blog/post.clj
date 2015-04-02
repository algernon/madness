(ns madness.blog.post
  "## Loading & rendering individual blog posts

  For the sake of ease, blog posts are fully loaded first, and turned
  into a structure that is easy to work with. This namespace
  implements the low-level loading, restructuring and rendering of
  individual blog posts.

  Blog posts are under `resources/posts` by default, and they all must
  have a filename that starts with a date (in YYYY-MM-DD format),
  followed by the short title of the post, that will be used for the
  URL."

  ^{:author "Gergely Nagy <algernon@madhouse-project.org>"
    :copyright "Copyright (C) 2012-2013 Gergely Nagy <algernon@madhouse-project.org>"
    :license {:name "Creative Commons Attribution-ShareAlike 3.0"
              :url "http://creativecommons.org/licenses/by-sa/3.0/"}}
  
  (:require [net.cgrand.enlive-html :as h]
            [madness.blog.nav :as blog-nav]
            [madness.utils :as utils]
            [madness.config :as cfg]
            [clojure.string :as str]
            [madness.io :as io]
            [clj-time.format :as time-format]))

;; The date format used within blog files. Note that this is not the
;; format we render dates as, but the format we expect them to be
;; within blog posts!
(def blog-date-format ^{:private true}
  (time-format/formatter "yyyy-MM-dd HH:mm"))

(defn- post-url
  "Given a parsed date and a source filename, return the URI for the blog post.

  The source filenames will be stripped of the date part, and the date
  within the post itself will be used to construct the final URL of
  the form `/blog/yyyy/MM/dd/title/`."

  [date fn]

  (str "/blog/" (time-format/unparse (time-format/formatter "yyyy/MM/dd") date)
       "/" (second (first (re-seq #"....-..-..-(.*)\.([^\.]*)$" fn))) "/"))

(defn read-post
  "Read a blog post from a file, and restructure it into a
  representation that is easy to work with.

  Each blog post must have an `article` element, which must also have
  `title`, `date`, and `tags` children - all of their purpose should
  already be clear. The `article` element may also have a `comments`
  propery, which, when set, will enable commenting on the particular
  post.

  A blog post must also have a `summary` element, the contents of
  which will be used when rendering the post for the purposes of
  recent posts, or as the summary on the main index page. The summary
  is also part of the blog post, and when viewing the entire post, it
  will start with the summary.

  Following that, the entire contents of the `section` element of the
  blog post will be displayed.

  The structure this function generates, should be pretty clear by
  glancing over the code here."

  [file]

  (let [post (io/read-file file)
        date (time-format/parse blog-date-format (apply h/text (h/select post [:article :date])))]
    {:title (apply h/text (h/select post [:article :title])),
     :tags (map h/text (h/select post [:article :tags :tag])),
     :summary (h/select post [:summary :> h/any-node]),
     :date date,
     :url (post-url date (.getName file))
     :comments (or
                (-> (first (h/select post [:article])) :attrs :comments utils/enabled?)
                (-> (h/text (first (h/select post [:article :comments]))) utils/enabled?)),
     :content (h/select post [:section :> h/any-node])}))

;; ### Blog post templates
;;
;; Blog posts are almost entirely contained within the
;; `#madness-article` element, with only the previous/next links
;; outside of it.

;; One of the first things one sees about a post, is its title, which
;; is the `h2` element of the `#madness-article` in the template.
;;
;; This snippet uses that element as the title template, replacing the
;; `title` attribute of it, and its textual content with the title of
;; the post itself.
(h/defsnippet blog-post-title (cfg/template) [:#madness-article :h2]
  [title]
  [:h2] (h/do->
         (h/content title)
         (h/set-attr :title title)))

;; #### Full article footer
;;
;; Posts, when viewed in full, and not only their summary, have a
;; footer, which holds their tags and the date they were posted on,
;; these all live under the `#madness-article-meta` element.

;; The full article footer has only a single link in the template: the
;; snippet that we'll use to render a single tag.
(h/defsnippet blog-post-tag (cfg/template) [:#madness-article-tags :a]
  [tag]

  [:a] (h/set-attr :href (utils/tag-to-url tag))
  [:a :span] (h/substitute tag))

;; The post meta-data - as mentioned just before - lives in
;; `#madness-article-meta`, and contains the date of the post, and the
;; tags. The tag list is passed in as an argument, separate from the
;; post.
(h/defsnippet blog-post-meta (cfg/template)
  [:.madness-article-meta]

  [post taglist]

  [:#madness-article-date] (h/do->
                            (h/set-attr :href (utils/date-to-url (:date post)))
                            (h/content (utils/date-format (:date post))))
  [:#madness-article-tags :a] (h/clone-for
                               [tag (butlast taglist)]
                               (h/do->
                                (h/substitute (blog-post-tag tag))
                                (h/after ", ")))
  [:#madness-article-tags] (h/append
                            (blog-post-tag (last taglist)))
  [:#madness-article-tags] (h/remove-attr :id))

;; #### Post navigation
;;
;; To ease navigating between posts, previous and next posts (if
;; available) will be shown outside of the `#madness-article`. These
;; we'll call `#madness-article-neighbours`, and this element must
;; have two children:  `#madness-article-next` and
;; `#madness-article-prev` for links to the next and previous posts,
;; respectively.
;;
;; Both of these need to have an `a` element, whose `href` will be
;; rewritten, and that element must have a `span` child, to be
;; replaced by the title of the previous or next post.
;;
(h/defsnippet blog-post-neighbours (cfg/template) [:#madness-article-neighbours]
  [neighbours]

  [:#madness-article-next :a] (h/set-attr :href (:url (first neighbours)))
  [:#madness-article-next :a :span] (h/substitute (:title (first neighbours)))
  [:#madness-article-next] (if (empty? (first neighbours))
                             nil
                             (h/remove-attr :id))
  
  [:#madness-article-prev :a :span] (h/substitute (:title (last neighbours)))
  [:#madness-article-prev :a] (h/set-attr :href (:url (last neighbours)))
  [:#madness-article-prev] (if (empty? (last neighbours))
                             nil
                             (h/remove-attr :id))
  [:#madness-article-neighbours] (h/remove-attr :id))

;; #### Commenting
;;
;; If commenting is enabled for a post, the
;; `#madness-article-comments` element should be left intact,
;; as-is. Otherwise, it will be removed, that is all this snippet
;; does.
(h/defsnippet blog-post-comments (cfg/template) [:#madness-article-comments]
  [post]

  [:#madness-article-comments] (when (:comments post) (h/remove-attr :id)))

;; A blog post title is contained within `#madness-article`, in a `h2`
;; element, which must have an `a` child. These two will be updated to
;; contain the title of the given blog post.
(h/defsnippet blog-post-title (cfg/template)
  [:#madness-article :h2]

  [post]

  [:h2] (h/set-attr :title (:title post))
  [:h2 :a] (utils/rewrite-link (:url post) (:title post))
  [:#madness-article] (h/remove-attr :id))

;; #### Putting it all together
;;
;; To put a full blog post together, we alter the page title, disable
;; the recent and archived post areas, rearrange the
;; `#madness-article`, pull in the next/prev links into
;; `#post-neighbours`, and last but not least, fill out the global tag
;; & recent post lists, using the tools provided by [blog.nav][1].
;;
;; Tags that start with a dot will not be displayed along with the rest.
;;
;; [1]: #madness.blog.nav
;;
(h/deftemplate blog-post (cfg/template)
  [post all-posts]

  [:title] (h/content (:title post) " - Asylum")
  [:#madness-og-title] (h/set-attr :content (str (:title post) " - Asylum"))
  [:#madness-og-url] (h/set-attr :content (str (cfg/base-url) (:url post)))

  ; Navigation bar
  [:#madness-recent-posts] nil
  [:#madness-tags] nil

  ; Article
  [:#madness-article :h2] (h/substitute
                           (blog-post-title post))
  [:#madness-article-content] (h/substitute
                               (:summary post)
                               (:content post))
  [:.madness-article-meta] (h/substitute
                            (blog-post-meta post
                                            (remove #(.startsWith % ".") 
                                                    (:tags post))))

  [:#madness-article-read-more] nil

  ; Footer
  [:#madness-article-comments] (h/substitute (blog-post-comments post))
  [:#madness-article-neighbours] (h/substitute (blog-post-neighbours (utils/neighbours all-posts post)))

  ; Archive
  [:#madness-archive-recent-posts] nil
  [:#madness-archive-archived-posts] nil

  ; Misc
  [:.pygmentize] utils/pygmentize-node

  ; Cleanup
  [:#main-feed] (h/remove-attr :id)
  [:#rss-feed] (h/remove-attr :id)
  [:#madness-content-area] (h/remove-attr :id)
  [:#madness-article] (h/remove-attr :id))

;; To help cross posting to other engines, lets have a template that
;; only contains the rendered summary and content of the post, and
;; nothing else.
;;
;; This uses an empty template, but still does code highlighting.
;;
(h/deftemplate blog-post-fragment (cfg/template :empty)
  [post _]

  [:html] (h/do->
           (h/substitute (:summary post)
                         (:content post)))
  [:.pygmentize] utils/pygmentize-node)
