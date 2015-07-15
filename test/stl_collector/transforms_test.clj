(ns stl-collector.transforms-test
  (:require [stl-collector.transforms :as t]
            [schema.core :as s]
            [schema.test :as st]
            [clojure.test :refer :all]
            [stl-collector.model :as m]))

(use-fixtures :once st/validate-schemas)

(deftest normalize
  (testing "finds the normal of a vertex"
    (let [v [[1.0 8.0 3.0]
             [4.0 5.0 9.0]
             [7.0 2.0 6.0]]]
      (is (= [27.0 27.0 0.0] (t/normal v))))))

(deftest facification
  (testing "turns an stl into something that is useful"
    (let [facet {:normal [0.0 0.0 0.0]
                 :vertices [[1.0 2.0 3.0]
                            [4.0 5.0 6.0]
                            [7.0 8.0 9.0]]}
          expected [[[1.0 2.0 3.0]
                     [4.0 5.0 6.0]
                     [7.0 8.0 9.0]]
                    [[1.0 2.0 3.0]
                     [4.0 5.0 6.0]
                     [7.0 8.0 9.0]]]]
      (is (= expected (t/facify [facet facet]))))))

(deftest maximum
  (testing " no maximum" 
    (let [face [[0.0 0.0 0.0]
                [0.0 0.0 0.0]
                [0.0 0.0 0.0]]
          vertices [face face]]
      (is (= [0.0 0.0 0.0] (t/maxima vertices)))))
  (testing "a maximum"
    (let [vertices [[[1.0 8.0 3.0]
                     [4.0 5.0 9.0]
                     [7.0 2.0 6.0]]]]
      (is (= [7.0 8.0 9.0] (t/maxima vertices))))))

(deftest minimum
  (testing "no minimum"
    (let [vertices [[[0.0 0.0 0.0]
                     [0.0 0.0 0.0]
                     [0.0 0.0 0.0]]]]
      (is (= [0.0 0.0 0.0] (t/minima vertices)))))

  (testing "a minimum"
    (let [vertices [[[1.0 8.0 3.0]
                     [4.0 5.0 9.0]
                     [7.0 2.0 6.0]]]]
      (is (= [1.0 2.0 3.0] (t/minima vertices))))))

(deftest translation
  (testing "can move a model in the x axis"
    (let [vertices  [[[1.0 8.0 3.0]
                      [4.0 5.0 9.0]
                      [7.0 2.0 6.0]]]
          expected [[[4.0 8.0 3.0]
                     [7.0 5.0 9.0]
                     [10.0 2.0 6.0]]]]
      (is (= expected
             (t/translate vertices [3.0 0.0 0.0])))))

  (testing "can move a model in the y axis"
    (let [vertices [[[1.0 8.0 3.0]
                     [4.0 5.0 9.0]
                     [7.0 2.0 6.0]]]
          expected [[[1.0 11.0 3.0]
                     [4.0 8.0 9.0]
                     [7.0 5.0 6.0]]]]
      (is (= expected
             (t/translate vertices [0.0 3.0 0.0])))))

  (testing "can move a model in the z axis"
    (let [vertices [[[1.0 8.0 3.0]
                     [4.0 5.0 9.0]
                     [7.0 2.0 6.0]]]
          expected [[[1.0 8.0 6.0]
                     [4.0 5.0 12.0]
                     [7.0 2.0 9.0]]]]
      (is (= expected
             (t/translate vertices [0.0 0.0 3.0]))))))

(deftest dimensional
  (testing "can find the bounding dimensions"
    (let  [vertices [[[1.0 8.0 3.0]
                      [4.0 5.0 9.0]
                      [7.0 2.0 6.0]]]
           expected [[1.0 2.0 3.0] [7.0 8.0 9.0]]]
      (is (= expected (t/dimensions vertices))))))

(deftest bounding
  (testing "can find the smallest cube that can wrap this object"
    (let [vertices [[[1.0 8.0 3.0]
                     [4.0 5.0 9.0]
                     [7.0 2.0 6.0]]]
          expected [6.0 6.0 6.0]]
      (is (= expected (t/bounding-cube vertices))))))

(deftest distribution
  (testing "can distribute two stl files across and axis"
    (let [stl [[[1.0 8.0 3.0]
                [4.0 5.0 9.0]
                [7.0 2.0 6.0]]]
          expected [[[[1.0 8.0 3.0] [4.0 5.0 9.0] [7.0 2.0 6.0]]]
                    [[[8.0 8.0 3.0] [11.0 5.0 9.0] [14.0 2.0 6.0]]]]]
      (is (= expected (t/distribute-x [stl stl] 1.0)))))

  (testing "can distribute N stl across an axis"
    (let [stl [[[1.0 8.0 3.0]
                [4.0 5.0 9.0]
                [7.0 2.0 6.0]]
               [[2.0 9.0 4.0]
                [5.0 6.0 7.0]
                [8.0 3.0 7.0]]]

          expected-x [[[[1.0 8.0 3.0] [4.0 5.0 9.0] [7.0 2.0 6.0]]
                       [[2.0 9.0 4.0] [5.0 6.0 7.0] [8.0 3.0 7.0]]]
                      [[[9.0 8.0 3.0] [12.0 5.0 9.0] [15.0 2.0 6.0]]
                       [[10.0 9.0 4.0] [13.0 6.0 7.0] [16.0 3.0 7.0]]]]

          expected-y [[[[1.0 8.0 3.0] [4.0 5.0 9.0] [7.0 2.0 6.0]]
                       [[2.0 9.0 4.0] [5.0 6.0 7.0] [8.0 3.0 7.0]]]
                      [[[1.0 16.0 3.0] [4.0 13.0 9.0] [7.0 10.0 6.0]]
                       [[2.0 17.0 4.0] [5.0 14.0 7.0] [8.0 11.0 7.0]]]
                      [[[1.0 24.0 3.0] [4.0 21.0 9.0] [7.0 18.0 6.0]]
                       [[2.0 25.0 4.0] [5.0 22.0 7.0] [8.0 19.0 7.0]]]]

          expected-z [[[[1.0 8.0 3.0] [4.0 5.0 9.0] [7.0 2.0 6.0]]
                       [[2.0 9.0 4.0] [5.0 6.0 7.0] [8.0 3.0 7.0]]]
                      [[[1.0 8.0 10.0] [4.0 5.0 16.0] [7.0 2.0 13.0]]
                       [[2.0 9.0 11.0] [5.0 6.0 14.0] [8.0 3.0 14.0]]]
                      [[[1.0 8.0 17.0] [4.0 5.0 23.0] [7.0 2.0 20.0]]
                       [[2.0 9.0 18.0] [5.0 6.0 21.0] [8.0 3.0 21.0]]]
                      [[[1.0 8.0 24.0] [4.0 5.0 30.0] [7.0 2.0 27.0]]
                       [[2.0 9.0 25.0] [5.0 6.0 28.0] [8.0 3.0 28.0]]]]]

      (is (= [stl] (t/distribute-x [stl] 1.0)))
      (is (= expected-x (t/distribute-x [stl stl] 1.0)))
      (is (= expected-y (t/distribute-y [stl stl stl] 1.0)))
      (is (= expected-z (t/distribute-z [stl stl stl stl] 1.0))))))

(deftest combine
  (testing "combines multiple stl files into one"
    (let [stl [[[1.0 8.0 3.0]
                [4.0 5.0 9.0]
                [7.0 2.0 6.0]]]
          expected [[[1.0 8.0 3.0]
                     [4.0 5.0 9.0]
                     [7.0 2.0 6.0]]

                    [[1.0 8.0 3.0]
                     [4.0 5.0 9.0]
                     [7.0 2.0 6.0]]]]
      (is (= expected (t/combine [stl stl]))))))

(deftest normalizing
  (testing "does nothing on empty list"
    (is (= [] (t/normalize []))))

  (testing "takes a list of faces and create a list of facets"
    (let [stl [[[1.0 8.0 3.0]
                [4.0 5.0 9.0]
                [7.0 2.0 6.0]]
               [[2.0 9.0 4.0]
                [5.0 6.0 7.0]
                [8.0 3.0 7.0]]]]
      (is (= [{:normal [27.0 27.0 0.0],
               :vertices [[1.0 8.0 3.0] [4.0 5.0 9.0] [7.0 2.0 6.0]]}
              {:normal [9.0 9.0 0.0],
               :vertices [[2.0 9.0 4.0] [5.0 6.0 7.0] [8.0 3.0 7.0]]}]
             (t/normalize stl))))))
