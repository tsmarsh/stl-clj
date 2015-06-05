(ns stl-collector.transforms-test
  (:require [stl-collector.transforms :as t]
            [schema.test :as st]
            [clojure.test :refer :all]))

(use-fixtures :once st/validate-schemas)

(deftest normal
  (testing "calculate the normal of the origin"
    (let [v [[0.0 0.0 0.0]
             [0.0 0.0 0.0]
             [0.0 0.0 0.0]]]
      (is (= [0.0 0.0 0.0] (t/normal v)))))
  
  (testing "calculate an interesting normal"
    (let [v  [[74.60932159423828 53.5815315246582 4.022638320922852]
             [74.60932159423828 53.5815315246582 5.902111053466797]
             [74.6712875366211 49.58250045776367 4.022638320922852]]]
      (is (= [7.516069846824394 0.11646329905488528 -0.0] (t/normal v))))))

(deftest maximum
  (testing "no maximum" 
    (let [vertices [[0.0 0.0 0.0]]]
      (is (= [0.0 0.0 0.0] (t/maxima vertices)))))
  (testing "a maximum"
    (let [vertices [[1.0 8.0 3.0]
                    [4.0 5.0 9.0]
                    [7.0 2.0 6.0]]]
      (is (= [7.0 8.0 9.0] (t/maxima vertices))))))

(deftest minimum
  (testing "no minimum"
    (let [vertices [[0.0 0.0 0.0]]]
      (is (= [0.0 0.0 0.0] (t/minima vertices)))))
  
  (testing "a minimum"
    (let [vertices  [[1.0 8.0 3.0]
                    [4.0 5.0 9.0]
                    [7.0 2.0 6.0]]]
      (is (= [1.0 2.0 3.0] (t/minima vertices))))))

(deftest translation
  (testing "can move a model in the x axis"
    (let [vertices  [[1.0 8.0 3.0]
                    [4.0 5.0 9.0]
                    [7.0 2.0 6.0]]
          expected [[4.0 8.0 3.0]
                    [7.0 5.0 9.0]
                    [10.0 2.0 6.0]]]
      (is (= expected
             (t/translate vertices [3.0 0.0 0.0])))))

  (testing "can move a model in the y axis"
    (let [vertices  [[1.0 8.0 3.0]
                    [4.0 5.0 9.0]
                    [7.0 2.0 6.0]]
          expected [[1.0 11.0 3.0]
                    [4.0 8.0 9.0]
                    [7.0 5.0 6.0]]]
      (is (= expected
             (t/translate vertices [0.0 3.0 0.0])))))
  
  (testing "can move a model in the z axis"
    (let [vertices [[1.0 8.0 3.0]
                    [4.0 5.0 9.0]
                    [7.0 2.0 6.0]]
          expected [[1.0 8.0 6.0]
                    [4.0 5.0 12.0]
                    [7.0 2.0 9.0]]]
      (is (= expected
             (t/translate vertices [0.0 0.0 3.0]))))))
