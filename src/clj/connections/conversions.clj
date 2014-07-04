(ns connections.conversions
  (:require [clojure.data.json :as json]))

;; Clojure format:
; {:key "value" :another "value"}

;; Neo4j cypher format:
; {key: "value", another: "value"}

;; JSON format:
; {"key": "value", "another": "value"}

(defn clj-to-cypher [input])

(defn cypher-to-clj [input])

(defn clj-to-json [input]
  (json/write-str input))

(defn json-to-clj [input]
  (json/read-str input
    :key-fn keyword))
