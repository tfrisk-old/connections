(ns connections.conversions-test
  (:require [clojure.test :refer :all]
            [connections.conversions :refer :all]))

(def clj-test-data {:testkey "testvalue" :testvector ["first" "second"]})
(def neo4j-test-data "{testkey: \"testvalue\"}")
(def json-test-data "{\"testkey\":\"testvalue\",\"testvector\":[\"first\",\"second\"]}")

(deftest clj-to-json-test
	(testing "Clojure to JSON"
		(is (=
      json-test-data
      (clj-to-json clj-test-data)))))

(deftest json-to-clj-test
  (testing "JSON to Clojure"
    (is (=
      clj-test-data
      (json-to-clj json-test-data)))))
