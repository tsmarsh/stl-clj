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
