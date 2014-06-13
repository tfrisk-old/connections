(ns connections.views
  (:use [connections.neo4j :as neo4j])
  (:use [hiccup core form page element]))

(def header
  [:head
    [:script {:src "/js/goog/base.js" :type "text/javascript"}]
    [:script {:src "/js/connections.js" :type "text/javascript"}]
    [:script {:type "text/javascript"} (str "goog.require(\"connections.core\");")]
  ])

(def search-form
  (form-to {:id "search-form"} [:post "/search" "search-form"]
    (text-field "search-text")
    (submit-button "Search")))

(defn layout [& content]
  (html5
    header
    [:body
      search-form
      content]))

(defn index-page []
  (layout
    [:h2 "Connections"]))

(defn list-connection-entry [entry]
  [:li
    (str entry)
    ;(str (get entry :startname) " -> " (get entry :endname))
  ])

(defn list-connections [coll]
  [:ul
    (map #(list-connection-entry %) coll)
  ])

(defn details-page [id]
  (layout
    (let [name (read-name-by-id (Integer/parseInt id))]
      [:h2 name]
      (list-connections (first (get-connections-by-name name)))
    )))

(defn search-page [params]
  (layout
    [:h2 "Search for: "(get params :search-text)]
    ;TODO: input sanitation
    (neo4j/search-entries-by-name (get params :search-text))))

(defn list-all-persons []
  (layout
    [:h2 "All persons"]
    (list-connections (search-all-persons))))

(defn list-all-organizations []
  (layout
    [:h2 "All organizations"]
    (list-connections (search-all-organizations))))

