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
