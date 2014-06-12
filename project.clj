(defproject connections "0.1.0-SNAPSHOT"
  :description "Visualize political connections"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]
		 [clojurewerkz/neocons "3.0.0"]
		 [compojure "1.1.5"]
		 [hiccup "1.0.4"]]

  :plugins [[lein-cljsbuild "1.0.2"]
	    [lein-ring "0.8.3"]]

  :source-paths ["src/clj" "src/cljs"]

  :ring {
	  :handler connections.routes/app
	  :auto-reload? true}

  :cljsbuild { 
    :builds [{:id "connections"
              :source-paths ["src/cljs"]
              :compiler {
                :output-to "resources/public/js/connections.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})
