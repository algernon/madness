(ns madness.blog.index
  ^{:author "Gergely Nagy <algernon@madhouse-project.org>"
    :copyright "Copyright (C) 2012-2018 Gergely Nagy <algernon@madhouse-project.org>"
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

(h/deftemplate blog-index (cfg/template)
  [blog-posts _]

  [:#madness-article] nil
  [:#madness-recent-posts] nil

  ; Cleanup
  [:#main-rss] (h/remove-attr :id)
  [:#main-css] (h/do->
                (h/remove-attr :id)
                (h/replace-vars (cfg/vars)))
  [:#rss-feed] (h/remove-attr :id)
  [:.no-index] nil)
