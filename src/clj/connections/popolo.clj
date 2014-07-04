(ns connections.popolo
  (:require
    [connections.neo4j :as neo4j]))

;=== read data from database ===
(defn load-persons-vector []
  "Read all persons from database to new vector"
  (into []
    (map
      #(conj
        {:id (% :id)}
        (neo4j/get-node-data (% :id)))
      (neo4j/search-all-persons))))

(defn load-organizations-vector []
  "Read all organizations from database to new vector"
  (into []
    (map
      #(conj
        {:id (% :id)}
        (neo4j/get-node-data (% :id)))
      (neo4j/search-all-organizations))))

(def persons (atom (load-persons-vector)))
;(def organizations (atom (load-organizations-vector)))

(defn load-connections-vector []
  "Read all connections from persons.
  Assumes that every connection has assigned person.
  Iterating thru organizations would take much longer."
  (into []
    (map
      #(hash-map
        :id (% :id)
        :connections (neo4j/get-connections-by-id (% :id)))
      @persons)))

;(def connections (atom (load-connections-vector)))

(defn get-person-by-id [id]
  (filter #(= id (get % :id)) @persons))

;(defn get-organization-by-id [id]
;  (filter #(= id (get % :id)) @organizations))

;===============================================================
(def person
  {
  :id "john-q-public"
  :name "Mr. John Q. Public, Esq."
  :other_names [
    {
      :name "Mr. Ziggy Q. Public, Esq."
      :start_date "1920-01"
      :end_date "1949-12-31"
      :note "Birth name"
    }
    {
      :name "Dragonsbane"
      :note "LARP character name"
    }
  ]
  :identifiers [
    {
      :identifier "046454286"
      :scheme "SIN"
    }
  ]
  :email "jqpublic@xyz.example.com"
  :gender "male"
  :birth_date "1920-01"
  :death_date "2010-01-01"
  :image "http://www.example.com/pub/photos/jqpublic.gif"
  :summary "A hypothetical member of society deemed a 'common man'"
  :biography "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ..."
  :national_identity "Scottish"
  :contact_details [
    {
      :type "cell"
      :label "Mobile number"
      :value "+1-555-555-0100"
      :note "Free evenings and weekends"
    }
  ]
  :links [
    {
      :url "http://en.wikipedia.org/wiki/John_Q._Public"
      :note "Wikipedia page"
    }
  ]
})

(def person-name
  {
  :id "john-q-public"
  :name "Mr. John Q. Public, Esq."
  :family_name "Public"
  :given_name "John"
  :additional_name "Quinlan"
  :honorific_prefix "Mr."
  :honorific_suffix "Esq."
  :sort_name "Public, John Quinlan Esq."
})

(def organization
  {
  :id "abc-inc"
  :name "ABC, Inc."
  :other_names [
    {
      :name "Bob's Diner"
      :start_date "1950-01-01"
      :end_date "1954-12-31"
    }
    {
      :name "Joe's Diner"
      :start_date "1955-01-01"
    }
    {
      :name "Famous Joe's"
    }
  ]
  :identifiers [
    {
      :identifier "123456789"
      :scheme "DUNS"
    }
    {
      :identifier "US0123456789"
      :scheme "ISIN"
    }
  ]
  :classification "Corporation"
  :parent_id "holding-company-corp"
  :founding_date "1950-01-01"
  :dissolution_date "2000-01-01"
  :image "http://www.example.com/pub/photos/logo.gif"
  :contact_details [
    {
      :type "voice"
      :label "Toll-free number"
      :value "+1-800-555-0199"
      :note "9am to 5pm weekdays"
    }
  ]
  :links [
    {
      :url "http://en.wikipedia.org/wiki/Joe's_Diner_(placeholder_name)"
      :note "Wikipedia page"
    }
  ]
})

(def membership
  {
  :id "593"
  :label "Kitchen assistant at Joe's Diner"
  :role "Kitchen assistant"
  :person_id "john-q-public"
  :organization_id "abc-inc"
  :post_id "abc-inc-kitchen-assistant"
  :start_date "1970-01"
  :end_date "1971-12-31"
  :contact_details [
    {
      :type "voice"
      :label "Take-out and delivery"
      :value "+1-800-555-0199"
      :note "12pm to midnight"
    }
  ]
  :links [
    {
      :url "http://example.com/abc-inc/staff"
      :note "ABC, Inc. staff page"
    }
  ]
})

(def post
  {
  :id "abc-inc-chef"
  :label "Chef at Joe's Diner"
  :other_label ["Cook"]
  :role "Chef"
  :organization_id "abc-inc"
  :start_date "1950"
  :end_date "2010"
  :contact_details [
    {
      :type "voice"
      :label "Take-out and delivery"
      :value "+1-800-555-0199"
      :note "12pm to midnight"
    }
  ],
  :links [
    {
      :url "http://example.com/abc-inc/staff"
      :note "ABC, Inc. staff page"
    }
  ]
})

(def contact-detail
  {
  :type "voice"
  :label "Phone number"
  :value "+1-555-555-0199;ext=555"
  :note "Capitol Hill"
  :valid_from "1970-01"
  :valid_until "1971-12-31"
})

(def area
  {
  :name "Boston Ward 1"
  :identifier "ocd-division/country:us/state:ma/place:boston/ward:1"
  :classification "ward"
  :parent_id "boston"
})
