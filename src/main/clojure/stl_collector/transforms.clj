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


(s/defn distribute :- [[m/Vertex]]
  [vertex :- [[m/Vertex]]
   gap :- Double
   dimension :- s/Int]
  (loop [[m1 m2 & ms] vertex
         offset 0
         stl [m1]]
    (let [width (get (bounding-cube m1) dimension)
          offset' (+ offset width gap)
          translation-matrix  (assoc [0.0 0.0 0.0] dimension offset')
          m2' (translate m2 translation-matrix)]
      (if (seq ms)
        (recur (cons m2' ms)
               offset'
               (conj stl m2'))
        (conj stl m2')))))

(s/defn distribute-x :- [[m/Vertex]]
  [vertex :- [[m/Vertex]]
   gap :- Double]
  (distribute vertex gap 0))

(s/defn distribute-y :- [[m/Vertex]]
  [vertex :- [[m/Vertex]]
   gap :- Double]
  (distribute vertex gap 1))

(s/defn distribute-z :- [[m/Vertex]]
  [vertex :- [[m/Vertex]]
   gap :- Double]
  (distribute vertex gap 2))

(s/defn combine :- [m/Vertex]
  [stls :- [[m/Vertex]]]
  (apply concat stls))
