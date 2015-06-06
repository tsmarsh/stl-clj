(ns stl-collector.transforms
  (:require [stl-collector.model :as m]
            [schema.core :as s]
            [clojure.core.matrix.operators :as x]
            [clojure.core.matrix.protocols :as mp]))

(s/defn columise :- [(s/one [Double] "1s")
                     (s/one [Double] "2s")
                     (s/one [Double] "3s")]
  [vertexes :- [m/Vertex]]
  (apply map vector vertexes))

(s/defn normal :- m/Vertex
  [[p0 p1 p2] :- m/Face]
  (mp/cross-product (x/- p1 p0) (x/- p2 p0)))

(s/defn maxima :- m/Vertex
  [vertexes :- [m/Vertex]]
  (map (partial apply max) (columise vertexes)))

(s/defn minima :- m/Vertex
  [vertexes :- [m/Vertex]]
  (map (partial apply min) (columise vertexes)))

(s/defn translate :- [m/Vertex]
  [vertexes :- [m/Vertex]
   d :- m/Vertex]
  (x/+ d vertexes))

(s/defn dimensions :- [(s/one m/Vertex "min")
                       (s/one m/Vertex "max")]
  [vertexes :- [m/Vertex]]
  [(minima vertexes) (maxima vertexes)])

(s/defn bounding-cube :- m/Vertex
  [vertexes :- [m/Vertex]]
  (apply x/- (reverse (dimensions vertexes))))

(s/defn combine-x :- [m/Vertex]
  [vertex :- [[m/Vertex]]
   gap :- Double]
  (loop [[m1 m2 & ms] vertex stl []]
    (let [[width height depth] (bounding-cube m1)
          translation-matrix [(+ gap width) 0.0 0.0] 
          m2' (translate m2 translation-matrix)]
      (if (seq ms)
        (recur (cons m2 ms) (apply conj stl m1))
        (apply conj m1 m2')))))
