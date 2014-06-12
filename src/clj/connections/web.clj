(ns connections.web
  (:use [connections.neo4j :as neo4j])
  (:use [hiccup core form page element]))

(def header
  [:head
    [:script {:src "/js/goog/base.js" :type "text/javascript"}]
    [:script {:src "/js/connections.js" :type "text/javascript"}]
    [:script {:type "text/javascript"} (str "goog.require(\"connections.core\");")]
  ])

(defn layout [& content]
  (html5
    header
    [:body
      content]))

(def search-form
  (form-to [:post "/search"]
    (text-field "search-text")
    (submit-button "Search")))

(defn index-page []
  (layout
    search-form))

(defn details-page [id]
  (layout (str id)))

(defn search-page [params]
  (layout
    [:h2 "Search for: "(get params :search-text)]
    ;TODO: input sanitation
    (neo4j/search-entries-by-name (get params :search-text))))

