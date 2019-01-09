(ns madness.styles
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.stylesheet :refer [at-media]]
            [garden.units :refer [px vw em vh]]
            [madness.config :as cfg]))

(def syntax
  [:.pygmentize
   [:.hll {:background-color "#ffffcc"}]
   [:.c :.cm :.c1 {:color "#999988" :font-style :italic}]
   [:.err {:color "#a61717" :background-color "#e3d2d2"}]
   [:.k :.o :.kc :.kd :.kn :.kp :.kr {:color "#000000" :font-weight :bold}]
   [:.cp :.cs {:color "#999999" :font-weight :bold :font-style :italic}]
   [:.gd {:color "#000000" :background-color "#ffdddd"}]
   [:.ge {:color "#000000" :font-style :italic}]
   [:.gr :.gt {:color "#aa0000"}]
   [:.gh :.bp {:color "#999999"}]
   [:.gi {:color "#000000" :background-color "#ddffdd"}]
   [:.go {:color "#888888"}]
   [:.gp :.nn {:color "#555555"}]
   [:.gs {:font-weight :bold}]
   [:.gu {:color "#aaaaaa"}]
   [:.kt {:color "#445588" :font-weight :bold}]
   [:.m :.mf :.mh :.mi :.mo :.il {:color "#009999"}]
   [:.s :.sb :.sc :.sd :.s2 :.se :.sh :.si :.sx :.s1 {:color "#d01040"}]
   [:.na :.no :.nv :.vc :.vg :.vi {:color "#008080"}]
   [:.nb {:color "#0086B3"}]
   [:.nc {:color "#445588" :font-weight :bold}]
   [:.nd {:color "#3c5d5d" :font-weight :bold}]
   [:.ni {:color "#800080"}]
   [:.ne :.nf :.nl {:color "#990000" :font-weight :bold}]
   [:.nt {:color "#000080"}]
   [:.ow {:color "#000000" :font-weight :bold}]
   [:.w {:color "#bbbbbb"}]
   [:.sr {:color "#009926"}]
   [:.ss {:color "#990073"}]])

(defn syntax-highlight []
  (when (cfg/syntax-highlight)
    syntax))

(defstyles asylum9
  (concat
   [[(at-media {:min-width (px 1000)}
               [:#container :footer {:width (vw 70)}]
               [:body {:font-size (px 24)}])
     (at-media {:min-width (px 800)
                :max-width (px 1000)}
               [:#container :footer {:width (vw 70)}])
     (at-media {:max-width (px 800)}
               [:#container :footer {:width (vw 95)}])]

    [:html {:position :relative
            :min-height "100%"}]
    [:body {:font-family :sans-serif
            :color "#333"}]
    [:.h-card {:display :none}]
    [:a {:color "#333"}
     [:&:hover {:color "#00e"}]]
    [:#container :footer {:margin-left :auto
                          :margin-right :auto}]
    [:hr {:border-style :none
          :border "1px none #333333"
          :borderd-bottom-style :solid}]
    [:.button {:text-decoration :none
               :color "#333333"
               :border "1px solid #333333"
               :padding (em 0.5)
               :margin (em 0.25)
               :display :inline-block
               :transition "0.3s ease-in-out"}
     [:&:hover {:border "1px solid #3333ff"
                :border-radius "10px"
                :color "#3333ff"}]]
    [:.madness-article-title {:margin-bottom 0}]
    [:.madness-article-meta {:font-size "65%"}]
    [:.madness-article-meta [:span [:a {:text-decoration :none}
                                    [:&:hover {:text-decoration :underline}]]]]
    [:#madness-article-date {:text-decoration :none}
     [:&:hover {:text-decoration :underline}]]
    [:.madness-post-date {:color "#888"
                          :margin-right (em 0.5)}]
    [:.madness-post-title {:margin-left (em 1)}]
    [:.madness-post-title [:a {:color "#333"
                               :text-decoration :none}
                           [:&:hover {:text-decoration :underline}]]]
    [:#madness-archive {:font-size "80%"
                        :margin-top (em 2.5)
                        :opacity 0.9}]
    [:#brand {:font-family :monospace
              :text-decoration :none}]
    [:footer {:position :relative
              :bottom 0
              :height (em 4)
              :font-size "80%"
              :padding-top (em 1)
              :border-top "1px solid #999"}]
    [:footer [:div {:display :inline-block
                    :float :right}]]
    [:blockquote :pre {:color "#555"}]
    [:pre {:margin (em 1)}]
    [:kbd :code {:color "#c7254e"}]
    [:td {:padding (em 0.5)
          :margin 0}]
    [:td :th {:border-bottom "1px solid #333"}]

    ;; Legacy stuff
    [:.thumbnails {:list-style :none
                   :display :flex
                   :flex-wrap :wrap
                   :margin 0
                   :padding 0
                   :flex-grow 1}]
    [:.thumbnails [:li {:padding (em 1)
                        :margin (em 1)}]]
    [:.thumbnail :.lightbox {:display :inline-block
                             :width (vh 40)}]
    (syntax-highlight)]))
