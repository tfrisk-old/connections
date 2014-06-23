(ns connections.neo4j
  (:require
    [clojurewerkz.neocons.rest :as nr]
    [clojurewerkz.neocons.rest.nodes :as nn]
    [clojurewerkz.neocons.rest.relationships :as nrl]
    [clojurewerkz.neocons.rest.cypher :as cy]
    [clojurewerkz.neocons.rest.paths :as paths]))

; Connection details
(def conn (nr/connect "http://localhost:7475/db/data/"))

(defn cypher [query]
  (clojurewerkz.neocons.rest.cypher/tquery conn query))

(defn read-id-from-url [url]
  (Integer/parseInt
    (last (clojure.string/split
      url #"/"))))

; Very haxxy way of reading node id, takes cypher return value as an input
(defn read-id-from-cypher-entry [entry]
  (read-id-from-url
    (get (second (first entry)) :self)))

(defn read-name-from-cypher-entry [entry]
  (get-in (second (first entry)) [:data :name]))

(defn clean-search-results [results]
  (map
    #(hash-map
      :id (read-id-from-cypher-entry %)
      :name (read-name-from-cypher-entry %))
    results))

(defn search-entries-by-name [regex]
  (clean-search-results
    (cypher (str "MATCH (search) WHERE search.name=~'"regex"' RETURN search"))))

(defn get-entry-by-name [name]
  (cypher (str "MATCH (n) WHERE n.name='"name"' RETURN n")))

(defn search-all-persons []
  (clean-search-results
    (cypher (str "MATCH (p:Person) RETURN p"))))

(defn search-all-organizations []
  (clean-search-results
    (cypher (str "MATCH (o:Organization) RETURN o"))))

(defn get-node-by-id [id]
  (nn/get conn id))

(defn read-name-by-id [id]
  (get-in (get-node-by-id id) [:data :name]))

(defn get-node-connections-out [id]
  (nrl/outgoing-for conn (get-node-by-id id)))

(defn get-node-connections-in [id]
  (nrl/incoming-for conn (get-node-by-id id)))

(defn get-connections-out-by-name [name]
  (get-node-connections-out (read-id-from-cypher-entry (first (get-entry-by-name name)))))

(defn get-connections-in-by-name [name]
  (get-node-connections-in (read-id-from-cypher-entry (first (get-entry-by-name name)))))

(defn get-connection-details [connection]
  (let [sid (read-id-from-url (get connection :start))
        eid (read-id-from-url (get connection :end))]
    (hash-map
      :cid (get connection :id)
      :startid sid
      :startname (read-name-by-id sid)
      :endid eid
      :endname (read-name-by-id eid)
      :type (get connection :type)
      :role (get-in connection [:data :role])
      :startdate (get-in connection [:data :startdate])
      :enddate (get-in connection [:data :enddate]))))

;get all connections for an entry
(defn get-connections-by-name [name]
  (filter #(not (empty? %))
    (conj
      '()
      (map #(get-connection-details %) (get-connections-in-by-name name))
      (map #(get-connection-details %) (get-connections-out-by-name name)))))

(defn get-path-details [path]
  (into #{} (map #(hash-map
    :id (read-id-from-url %)
    :name (read-name-by-id (read-id-from-url %)))
    (get path :nodes))))

(defn get-paths-between-nodes [id1 id2 depth]
  (map
    #(get-path-details %)
    (paths/all-shortest-between conn id1 id2 :max-depth depth)))

