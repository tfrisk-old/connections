(defproject connections "0.1.0-SNAPSHOT"
  :description "Visualize political connections"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]
		 [clojurewerkz/neocons "3.0.0"]]

  :plugins [[lein-cljsbuild "1.0.2"]
            [lein-simpleton "1.3.0"]]

  :source-paths ["src/clj" "src/cljs"]

  :cljsbuild { 
    :builds [{:id "connections"
              :source-paths ["src"]
              :compiler {
                :output-to "connections.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})
