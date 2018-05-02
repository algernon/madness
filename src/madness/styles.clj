(ns madness.styles
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.stylesheet :refer [at-media]]
            [garden.units :refer [px vw em vh]]))

(defstyles asylum8
  [[(at-media {:min-width (px 1000)}
              [:#container :footer {:width (vw 70)}])
    (at-media {:min-width (px 800)
               :max-width (px 1000)}
              [:#container :footer {:width (vw 70)}])
    (at-media {:max-width (px 800)}
              [:#container :footer {:width (vw 95)}])]

   [:html {:position :relative
           :min-height "100%"}]
   [:body {:font-family :sans-serif
           :margin-bottom (em 5)
           :color "#333"}]
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
              :margin-top (em 0.25)
              :margin-bottom 0
              :display :inline-block}
    [:&:hover {:border "1px solid #3333ff"
               :color "#3333ff"}]]
   [:.madness-article-meta {:font-size "75%"
                            :opacity 0.6}]
   [:.madness-article-meta [:span [:a {:text-decoration :none}
                                   [:&:hover {:text-decoration :underline}]]]]
   [:#madness-article-date {:text-decoration :none}
    [:&:hover {:text-decoration :underline}]]
   [:.madness-post-title [:a {:color "#333"
                              :text-decoration :none}
                          [:&:hover {:text-decoration :underline}]]]
   [:.madness-article-title :.madness-post-title
    [:a.madness-post-section-sign {:color "#333"
                                   :margin-left (em -0.75)
                                   :visibility :hidden}]]
   [:.madness-article-title:hover :.madness-post-title:hover
    [:a.madness-post-section-sign {:visibility :visible
                                   :color "#808080"
                                   :text-decoration :none}]]
   [:#madness-archive {:font-size "80%"
                       :margin-top (em 2.5)
                       :opacity 0.9}]
   [:#brand {:font-family :cursive
             :font-size (em 2)
             :text-decoration :none
             :color "#999"
             :text-shadow "1px 1px #333"}
    [:&:hover {:color "#158cba"}]]
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
   [:.thumnails [:.li {:padding (em 1)
                       :margin (em 1)}]]
   [:.thumbnail :.lightbox {:display :inline-block
                            :width (vh 40)}]])
