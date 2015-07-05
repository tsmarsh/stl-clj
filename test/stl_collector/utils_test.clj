(ns stl-collector.util-test
  (:require [stl-collector.utils :as u]
            [clojure.test :refer :all]))

(deftest find-and-extract
  (testing "returns nil and the map if no value found"
    (is (= [nil {}] (u/find-and-extract {} :a))))

  (testing "returns the value and the map with the value removed"
    (is (= [5 {}] (u/find-and-extract {:a [5]} :a))))

  (testing "returns the value and the map with the value removed if there is a list of values"
    (is (= [5 {:a [3]}] (u/find-and-extract {:a [5 3]} :a))))

  (testing "returns the value and the map"
    (is (= [5 {:b [3]}] (u/find-and-extract {:a [5] :b [3]} :a)))))

(deftest merge-to-list
  (testing "empty keys returns empty map"
    (is (= {} (u/merge-to-list [] []))))
  
  (testing "one key value pairs returns a map with a list"
    (is (= {:a [5]} (u/merge-to-list [:a] [5]))))

  (testing "two key values pairs returns two map entries with lists"
    (is (= {:a [5] :b [5]} (u/merge-to-list [:a :b] [5 5]))))

  (testing "two key values pairs with same key becomes map entry with list"
    (is (= {:a [5 5]} (u/merge-to-list [:a :a] [5 5])))))
