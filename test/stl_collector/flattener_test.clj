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

(deftest collect
  (testing "if the stl fits"
    (is (= stl (f/collect [40.0 40.0 40.0] 5.0 [stl]))))

  (testing "works for N"
    (is  (= 48 (count (f/collect [50.0 50.0 50.0] 5.0 [stl stl stl stl])))))

  (testing "works for N"
    (is  (= 48 (count (f/collect [50.0 50.0 50.0] 5.0 [stl stl stl stl stl])))))

  (testing "keeps track of rejected"
    (is  (= [stl] (:rejections (f/collect-with-rejections
                                [50.0 50.0 50.0]
                                5.0
                                [stl stl stl stl stl]))))))
