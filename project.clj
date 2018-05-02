(defproject madness "0.0.0-SNAPSHOT"
  :description "Static site generator, based on [Enlive][1] and
  [Bootstrap][2].

  [1]: https://github.com/cgrand/enlive/wiki
  [2]: http://twitter.github.com/bootstrap/"
  :url "https://github.com/algernon/madness"
  :license {:name "Creative Commons Attribution-ShareAlike 3.0"
            :url "http://creativecommons.org/licenses/by-sa/3.0/"
            :distribution :repo}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-html-compressor "0.1.1"]
                 [enlive/enlive "1.1.1" :exclusions [org.clojure/clojure]]
                 [clj-time "0.4.4" :exclusions [org.clojure/clojure]]
                 [fs "1.3.2"]
                 [clj-yaml "0.4.0"]
                 [org.pegdown/pegdown "1.1.0"]
                 [me.raynes/conch "0.4.0"]
                 [garden "1.3.5"]]
  :plugins [[lein-garden "0.3.0"]]
  :aliases {"madness" ["run" "-m" "madness.core"]
            "madness-fragment" ["run" "-m" "madness.core/madness-fragments"]}
  :garden {:builds [{:id "asylum8"
                     :source-paths ["src"]
                     :stylesheet madness.styles/asylum8
                     :compiler {:output-to "resources/assets/asylum/css/asylum8.1.min.css"
                                :pretty-print? false}}]})
