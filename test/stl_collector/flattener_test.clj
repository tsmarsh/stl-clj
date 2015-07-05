(ns stl-collector.flattener-test
  (:require [stl-collector.flattener :as f]
            [clojure.test :refer :all]
            [clojure.java.io :as io]
            [stl-collector.reader :as r]
            [stl-collector.transforms :as t]
            [schema.test :as st]))

(use-fixtures :once st/validate-schemas)

;;A 20 x 20 x 20 cube
(def stl (->> "stl/cube.stl"
              io/resource
              io/file
              r/read-stl
              t/facify
              t/normalize))

(deftest merge-to-list
  (testing "empty keys returns empty map"
    (is (= {} (f/merge-to-list [] []))))
  
  (testing "one key value pairs returns a map with a list"
    (is (= {:a [5]} (f/merge-to-list [:a] [5]))))

  (testing "two key values pairs returns two map entries with lists"
    (is (= {:a [5] :b [5]} (f/merge-to-list [:a :b] [5 5]))))

  (testing "two key values pairs with same key becomes map entry with list"
    (is (= {:a [5 5]} (f/merge-to-list [:a :a] [5 5])))))

(deftest find-and-extract
  (testing "returns nil and the map if no value found"
    (is (= [nil {}] (f/find-and-extract {} :a))))

  (testing "returns the value and the map with the value removed"
    (is (= [5 {}] (f/find-and-extract {:a [5]} :a))))

  (testing "returns the value and the map with the value removed if there is a list of values"
    (is (= [5 {:a [3]}] (f/find-and-extract {:a [5 3]} :a))))

  (testing "returns the value and the map"
    (is (= [5 {:b [3]}] (f/find-and-extract {:a [5] :b [3]} :a)))))
