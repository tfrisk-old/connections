(ns sidonnaisuudet.core
  (:require
    [clojurewerkz.neocons.rest :as nr]
    [clojurewerkz.neocons.rest.nodes :as nn]
    [clojurewerkz.neocons.rest.relationships :as nrl]
    [clojurewerkz.neocons.rest.cypher :as cy]))

(def conn (nr/connect "http://localhost:7475/db/data/"))

(defn get-person [name]
  (clojurewerkz.neocons.rest.cypher/tquery conn (str "MATCH (p:Person) WHERE p.name='"name"' RETURN p")))
