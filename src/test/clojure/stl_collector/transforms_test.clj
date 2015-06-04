(ns stl-collector.transforms-test
  (:require [stl-collector.transforms :as t]
            [schema.test :as st]
            [clojure.test :refer :all]))

(use-fixtures :once st/validate-schemas)

(deftest maximum
  (testing "no maximum" 
    (let [vertices [[(float 0)(float 0)(float 0)]]]
      (is (= [0.0 00. 0.0] (t/maxima vertices)))))
  (testing "a maximum"
    (let [vertices [[(float 1) (float 8) (float 3)]
                    [(float 4) (float 5) (float 9)]
                    [(float 7) (float 2) (float 6)]]]
      (is (= [7.0 8.0 9.0] (t/maxima vertices))))))

(deftest minimum
  (testing "no minimum"
    (let [vertices [[(float 0)(float 0)(float 0)]]]
      (is (= [0.0 0.0 0.0] (t/minima vertices)))))
  
  (testing "a minimum"
    (let [vertices [[(float 1) (float 8) (float 3)]
                    [(float 4) (float 5) (float 9)]
                    [(float 7) (float 2) (float 6)]]]
      (is (= [1.0 2.0 3.0] (t/minima vertices))))))

