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

(def navigation-items
  [:div {:id "navigation"}
  (link-to "/" "home")
  (link-to "/persons" "persons")
  (link-to "/organizations" "organizations")
  ])

(defn layout [& content]
  (html5
    header
    [:body
      navigation-items
      search-form
      content]))

(defn index-page []
  (layout
    [:h2 "Connections"]
    [:p "Please use top navigation or search."]
    [:p "Search examples:"]
    [:ul
      [:li "Sivistysvaliokunta"]
      [:li ".*eläke.* (wildcard search requires '.*')"]
    ]))

(defn list-connection-entry-simple [entry]
  [:li
    (link-to
      (str "/details/" (get entry :id))
      (str (get entry :name) " " entry))
  ])

(defn list-connection-entry-detailed [entry entryid]
  [:li
    (link-to (str "/details/"
          ; handle startid and endid linking correctly
	  (if (= entryid (get entry :startid))
            (get entry :endid)
	    (get entry :startid)))
          (if (= entryid (get entry :startid))
	    (str (get entry :endname) " " entry)
	    (str (get entry :startname) " " entry)))
  ])

(defn list-connections-simple [coll]
  [:ul
    (map #(list-connection-entry-simple %) coll)
  ])

(defn list-connections-detailed [coll id]
  [:ul
    (map #(list-connection-entry-detailed % id) coll)
  ])

(defn details-page [id]
  (layout
    [:h2 "Details for: "(read-name-by-id (Integer/parseInt id))]
    (let [name (read-name-by-id (Integer/parseInt id))]
      (list-connections-detailed (first (get-connections-by-name name)) (Integer/parseInt id))
    )))

(defn search-page [params]
  (layout
    [:h2 "Search for: "(get params :search-text)]
    ;TODO: input sanitation
    (list-connections-simple
      (neo4j/search-entries-by-name (get params :search-text)))))

(defn list-all-persons []
  (layout
    [:h2 "All persons"]
    (list-connections-simple (search-all-persons))))

(defn list-all-organizations []
  (layout
    [:h2 "All organizations"]
    (list-connections-simple (search-all-organizations))))

