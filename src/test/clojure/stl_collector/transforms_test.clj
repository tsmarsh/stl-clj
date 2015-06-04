(ns stl-collector.transforms-test
  (:require [stl-collector.transforms :as t]
            [clojure.test :refer :all]))

(deftest maximum
  (testing "no maximum" 
    (let [vertices [[0 0 0]]]
      (is (= [0 0 0] (t/maxima vertices)))))
  (testing "a maximum"
    (let [vertices [[1 8 3] [4 5 9] [7 2 6]]]
      (is (= [7 8 9] (t/maxima vertices))))))

(deftest minimum
  (testing "no minimum"
    (let [vertices [[0 0 0]]]
      (is (= [0 0 0] (t/minima vertices))))
    )
  (testing "a minimum"
    (let [vertices [[1 8 3] [4 5 9] [7 2 9]]]
      (is (= [1 2 3] (t/minima vertices))))))

