(ns madness.config
  "## Configuration handling for Madness.

  Madness can be configured by placing a file called `settings.clj` in
  the root directory. Settings therein will override the defaults when
  the file gets evaluated."

  ^{:author "Gergely Nagy <algernon@madhouse-project.org>"
    :copyright "Copyright (C) 2012-2013 Gergely Nagy <algernon@madhouse-project.org>"
    :license {:name "Creative Commons Attribution-ShareAlike 3.0"
              :url "http://creativecommons.org/licenses/by-sa/3.0/"}}

  ; Dummy :use, so that the meta-data above gets parsed correctly.
  (:use [clojure.core]))

(def default-config
  "The default configuration values, settings in `settings.clj`
  override these values.

  The settings are as follows:

  * `:template` is a map of filenames to use as templates for various
    roles, such as: `:default`, `:atom`, and `:empty`.

    The first is a one-file template to use for the whole site. It
    should include all the bits and pieces needed to build the site,
    more about that in the [next section][1].

    The second is the template for Atom feeds: the main feed and the
    per-tag feeds alike.

    The last one should be an almost empty HTML file, containing only
    an empty body element.

  * `:dirs`, the directories where the various parts of the sources
    are to be found.

  * `:recent-posts` and `:archived-posts` both have a `:columns` and a
    `:rows` setting, which determines how many columns and rows each
    will have. Setting `:rows` to 0 disables limiting.

  * `:atom` controls the `:base-url` and the `:title` of the generated
    Atom feeds.

  [1]: #madness.render"

  {:template {:default "default.html"
              :atom "atom.xml"
              :empty "empty.html"
              :base-url "http://localhost"
              :syntax-highlight true}
   :dirs {:posts "resources/posts"
          :pages "resources/pages"
          :output "public"}
   :recent-posts {:columns 3
                  :rows 2}
   :archive-posts {:columns 3
                   :rows 0}
   :atom {:base-url "http://localhost"
          :title nil}
   :vars {:build-date (str (.getTime (java.util.Date.)))}})

(def config
  "The final configuration for Madness - `settings.clj` merged into
  the `default-config`."
  (merge-with merge default-config (eval (read-string (slurp "settings.clj")))))

(defn syntax-highlight
  "Get the syntax-highlight setting."
  []

  (-> config :template :syntax-highlight))

(defn template
  "Get the location of a template. Without arguments, returns the
  location of the default template, otherwise the specified one."
  [& id]

  (str "templates/" ((or (first id) :default) (:template config))))

(defn base-url
  "Get the base URL for the site."
  []

  (-> config :template :base-url))

(defn vars
  []
  (-> config :vars))

(defmulti recent-posts
  "Return various settings for recent-posts. Apart from the settable
  `:rows` and `:columns` setting, this understands `:total` and
  `:span` too, where the first returns the total number of recent
  posts to display, and the latter the amount of Bootstrap grid
  columns a single item should span."

  identity)

;; By default, whatever setting was asked for, we look that up in
;; `config`'s `:recent-posts` map.
(defmethod recent-posts :default [setting]
  (-> config :recent-posts setting))

;; However, if we want the `:total` number of recent posts, we either
;; look that up from the config, or simply multiply the number of rows
;; and columns, and add one, so that the result is suitable for
;; `range`.
(defmethod recent-posts :total [_]
  (or (-> config :recent-posts :total inc)
      (inc (* (recent-posts :columns)
              (recent-posts :rows)))))

;; And to determine how many columns a single recent item should span,
;; we divide 12 by the number of columns, and round it.
(defmethod recent-posts :span [_]
  (int (/ 12 (recent-posts :columns))))

(defmulti archive-posts
  "Return various settings for archive-posts. Apart from the settable
  `:rows` and `:columns` setting, this understands `:span` too, which
  is the amount of Bootstrap grid columns a single archived item
  should span"

  identity)

;; By default, whatever setting was asked for, we look that up in
;; `config`'s `:archive-posts` map.
(defmethod archive-posts :default [setting]
  (-> config :archive-posts setting))

;; And to determine how many columns a single archive item should
;; span, we divide 12 by the number of columns, and round it.
(defmethod archive-posts :span [_]
  (int (/ 12 (archive-posts :columns))))

(defn dirs
  "Looks up a directory in `config`'s `:dirs` map."
  [role]

  (-> config :dirs role))

(defn atom-feed
  "Looks up a setting in `config`'s `:atom` map."
  [setting]

  (-> config :atom setting))
