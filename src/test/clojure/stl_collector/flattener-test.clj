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
              r/read-stl))

(def faces (t/facify stl))

(deftest fitting
  (testing "does fit"
    (let [machine [20.0 20.0 20.0]]
      (is (f/fit machine [20.0 20.0 20.0]))))
  (testing "doesn't fit"
    (let [machine [19.0 20.0 20.0]]
      (is (not (f/fit machine [20.0 20.0 20.0]))))))

(deftest collecting
  (testing "returns the originial stl if it fits"
    (let [machine [21.0, 21.0, 21.0]]

      (is (= stl (f/collect machine [stl])))))
  (testing "return an empty list if the stl doesn't fit"
    (let [machine [19.0, 19.0, 19.0]]
      (is (= [] (f/collect machine [stl]))))))
