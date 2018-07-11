(ns madness.utils
  "Assorted utilities."

  ^{:author "Gergely Nagy <algernon@madhouse-project.org>"
    :copyright "Copyright (C) 2012 Gergely Nagy <algernon@madhouse-project.org>"
    :license {:name "Creative Commons Attribution-ShareAlike 3.0"
              :url "http://creativecommons.org/licenses/by-sa/3.0/"}}

  (:require [clojure.string :as s]
            [clj-time.core :as time-core]
            [clj-time.format :as time-format]
            [net.cgrand.enlive-html :as h]
            [fs.core :as fs]
            [conch.sh :refer [let-programs]]
            [madness.config :as cfg]))

;; A &lt;hr> element that is only visible on desktop resolutions.
(def hr-desktop [{:tag :hr :attrs {:class "visible-desktop"}}])

(defn tags
  "Return the list of unique tags, sorted alphabetically."

  [blog]

  (apply sorted-set (mapcat :tags blog)))

(defn tag-to-url
  "Given a tag, return a relative URL that points to it."

  [tag]

  (str "/blog/tags/" (s/replace (s/lower-case tag) " " "-") "/"))

(defn date-format
  "Format a date object into human-readable form."

  [date]

  (time-format/unparse (time-format/formatter "yyyy-MM-dd") date))

(defn datetime-format
  [date]
  (time-format/unparse (time-format/with-zone
                         (time-format/formatter-local "yyyy-MM-dd HH:mm:ssZ")
                         (time-core/default-time-zone)) date))

(defn date-to-url
  "Given a date, return a relative URL that points to the yearly
  archives."

  [date]

  (str "/blog/" (time-format/unparse (time-format/formatter "yyyy") date) "/"))

(defn post-tagged?
  "Determine whether a post is tagged with a given tag."

  [post tag]

  (some #(= tag %1) (:tags post)))

(defn posts-tagged
  "Return a list of posts tagged with a given tag."

  [posts tag]

  (filter #(post-tagged? %1 tag) posts))

(defn group-blog-by-tags
  "Group all blog posts by their tags. Posts may appear under multiple
  tags. Returns a hash-map, with the tags as keys, and the list of
  posts as values."

  [blog]

  (reduce #(assoc %1 %2 (posts-tagged blog %2)) {} (tags blog)))

(defn- blog-dates
  "Returns a set of all the blog posts that match the filter."

  [blog f]

  (set (map #(f (:date %1)) blog)))

(defn- post-with-date?
  "Checks whether a given post was made on a particular date. The `f`
  function is used to transform the posts date before the comparsion."

  [date f post]

  (let [fdate (f (:date post))]
    (= date fdate)))

(defn- posts-with-date
  "Returns all posts within a blog that have a particular date, where
  the date of the posts is determined after transforming them with
  function `f`."

  [blog date f]

  (filter (partial post-with-date? date f) blog))

(defn group-blog-by-date
  "Group all posts within a blog by date. Uses the supplied `f`
  function to format and compare dates."

  [blog f]

  (reduce #(assoc %1 %2 (posts-with-date blog %2 f)) {}
          (blog-dates blog f)))

(defn posts-by-day
  [d]

  (time-format/unparse (time-format/formatter "yyyy/MM/dd") d))

(defn posts-by-month
  [d]

  (time-format/unparse (time-format/formatter "yyyy/MM") d))

(defn posts-by-year
  [d]

  (time-format/unparse (time-format/formatter "yyyy") d))

(defn neighbours
  "Given a full blog, and a single post, find the previous and the
  next post that surround the current one."

  [blog post]

  (if (= (first blog) post)
    [nil (second blog)]
    (loop [prev (first blog)
           posts (rest blog)]
      (if (= (first posts) post)
        [prev (second posts)]
        (recur (first posts) (rest posts))))))

(defn blog->table
  "Given a number of columns, rows and a list of blog-posts, arrange
  them into a table with the given number of rows and columns. If rows
  is set to zero, there will be no limit on how many rows the
  resulting table may have.

  Returns an array where each element is a list of posts for that
  row."

  [columns rows blog-posts]

  (if (zero? rows)
    []
    (loop [posts blog-posts
           c 0
           result []]
      (if (or (empty? posts) (and (> rows 0) (>= c rows)))
        result
        (recur (drop columns posts)
               (inc c)
               (conj result (take columns posts)))))))

(defn rewrite-link
  "Rewrite an anchor element's href and content."
  [url content]

  (h/do->
   (h/set-attr :href url)
   (h/content content)))

(defn rewrite-link-with-title
  "Rewrite an anchor element's href, content and title. The content is
  the same as the title, with a single space prepended."

  [url title]

  (h/do->
   (h/set-attr :href url)
   (h/set-attr :title title)
   (h/content " " title)))

(defn replace-extension
  "Replace the extension of a file with another."
  [fn ext]

  (let [components (fs/split fn)
        fn (str (fs/name (last components)) ext)]
    (if (= "/" (first components))
      (s/join "/" (conj (vec (butlast (rest components))) fn))
      (s/join "/" (conj (vec (butlast components)) fn)))))

(defn enabled?
  "A very dumb little helper function, that merely checks if a value
  is set or not - it's mostly here to make some of the code below
  clearer."
  [value]

  (if (or
       (nil? value)
       (= value ""))
    false
    true))

(defn pygmentize
  "Syntax highlight some code."
  [language text]
  (let-programs [pygmentize "/usr/bin/pygmentize"]
                (pygmentize "-fhtml" (str "-l" language)
                            (str "-Ostripnl=False,encoding=utf-8")
                            {:in text})))

(defn pygmentize-node
  "Syntax highlight a node. The node must have the language in the
  data-language attribute."
  [node]

  (let [language (-> node :attrs :data-language)
        new-content (pygmentize language (:content node))]
    (if (cfg/syntax-highlight)
      ((h/html-content new-content) node)
      {:tag :pre :content (:content node)})))
