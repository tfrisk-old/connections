(ns connections.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(def app-state (atom
 {:search {:text "" :results []}
  :persons [
   {:id 192, :name "Anne-Mari Virolainen", :other_names [], :identifiers [], :email "", :gender "female", :birth_date "", :death_date "", :image "", :summary "", :biography "", :national_identity "", :contact_details [], :links []}
   {:id 193, :name "Erkki Virtanen", :other_names [], :identifiers [], :email "", :gender "male", :birth_date "", :death_date "", :image "", :summary "", :biography "", :national_identity "", :contact_details [], :links []}
   {:id 194, :name "Pertti Virtanen", :other_names [], :identifiers [], :email "", :gender "male", :birth_date "", :death_date "", :image "", :summary "", :biography "", :national_identity "", :contact_details [], :links []}
  ]
  :organizations [
   {:id 1010, :classification "yhdistys", :name "Lounais-Suomen liikunta- ja urheilu ry:n hallitus", :other_names [], :identifiers [], :parent_id nil, :founding_date "", :dissolution_date "", :image "", :contact_details [], :links []}
   {:id 692, :classification "puolue", :name "Kansallinen Kokoomus rp", :other_names [], :identifiers [], :parent_id nil, :founding_date "", :dissolution_date "", :image "", :contact_details [], :links []}
   {:id 750, :classification "yhdistys", :name "Keski-Suomen Teiniyhdistyksen hallitus", :other_names [], :identifiers [], :parent_id nil, :founding_date "", :dissolution_date "", :image "", :contact_details [], :links []}
   {:id 1878, :classification "yritys", :name "Tradekan edustajisto", :other_names [], :identifiers [], :parent_id nil, :founding_date "", :dissolution_date "", :image "", :contact_details [], :links []}
   {:id 1817, :classification "kunta", :name "Tampereen kaupungin valtuusto", :other_names [], :identifiers [], :parent_id nil, :founding_date "", :dissolution_date "", :image "", :contact_details [], :links []}
   {:id 1266, :classification "puolue", :name "Perussuomalaisten eduskuntaryhmä", :other_names [], :identifiers [], :parent_id nil, :founding_date "", :dissolution_date "", :image "", :contact_details [], :links []}
  ]
  :memberships [
   {:start_date "2011", :organization_id 1010, :end_date "", :id 6140, :role "vpj", :person_id 192, :label "", :post_id "", :contact_details [], :links []}
   {:start_date "2010", :organization_id 692, :end_date "", :id 6139, :role "vpj", :person_id 192, :label "", :post_id "", :contact_details [], :links []}
   {:start_date "1971", :organization_id 750, :end_date "", :id 6182, :role "", :person_id 193, :label "", :post_id "", :contact_details [], :links []}
   {:start_date "2004", :organization_id 1878, :end_date "2009", :id 6181, :role "", :person_id 193, :label "", :post_id "", :contact_details [], :links []}
   {:start_date "2001", :organization_id 1817, :end_date "2005", :id 6202, :role "", :person_id 194, :label "", :post_id "", :contact_details [], :links []}
   {:start_date "3/21/2007", :organization_id 1266, :end_date "", :id 6201, :role "", :person_id 194, :label "", :post_id "", :contact_details [], :links []}
  ]
  }))

(defn find-search-matches [app search-term owner]
  (vec (filter
    #(re-matches (re-pattern search-term) (:name %))
    (:persons app))))
;Alternative solution:
;  (->> app :persons
;    (filter
;      (comp (partial re-find (re-pattern search-term)) :name))))

(defn commit-search [app owner]
  (let [search-term-el (om/get-node owner "search-term")
        search-term    (.-value search-term-el)]
    (om/transact! app [:search :text] (fn [_] search-term))
    (set! (.-value search-term-el) "")
    (om/transact! app [:search :results]
      (fn [_] (find-search-matches @app search-term owner)))
    (js/console.log (str "search:" (:search @app)))))

(defn search-view [app owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
        (dom/input
          #js {:ref "search-term"
               :onKeyPress #(when (== (.-keyCode %) 13)
                              (commit-search app owner))})
        (dom/button
          #js {:onClick #(commit-search app owner)}
          "Search")))))

(defn person-item [cursor owner]
  (reify
    om/IRender
    (render [_]
      (dom/li nil
        (dom/div nil (str (:name cursor)))))))

(defn persons-list [cursor owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
        (apply dom/ul nil
          (om/build-all person-item cursor))))))

(defn app-view [app owner]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
        (dom/h2 nil "Persons")
          (om/build persons-list (:persons app))
        (dom/h2 nil "Search results")
          (om/build persons-list (:results (:search app)))))))

(om/root search-view app-state
  {:target (. js/document (getElementById "header-search"))})

(om/root app-view app-state
  {:target (. js/document (getElementById "app"))})
