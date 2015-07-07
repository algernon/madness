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
                 [enlive/enlive "1.1.1" :exclusions [org.clojure/clojure]]
                 [clj-time "0.4.4" :exclusions [org.clojure/clojure]]
                 [fs "1.3.2"]
                 [clj-yaml "0.4.0"]
                 [org.pegdown/pegdown "1.1.0"]
                 [me.raynes/conch "0.4.0"]]
  :profiles {:dev {:dependencies [[marginalia "0.7.0"]]}}
  :aliases {"build-docs" ["with-profile" "dev" "run" "-m" "madness.docs/generate-docs"]
            "madness" ["run" "-m" "madness.core"]
            "madness-fragment" ["run" "-m" "madness.core/madness-fragments"]})
