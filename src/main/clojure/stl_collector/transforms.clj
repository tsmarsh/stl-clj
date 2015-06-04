(ns stl-collector.transforms
  (:require [stl-collector.model :as m]
            [schema.core :as s]))

(s/defn columise :- [(s/one [Float] "1s")
                     (s/one [Float] "2s")
                     (s/one [Float] "3s")]
  [vertexes :- [m/Vertex]]
  (apply map vector vertexes))

(s/defn maxima :- m/Vertex
  [vertexes :- [m/Vertex]]
  (map (partial apply max) (columise vertexes)))

(s/defn minima :- m/Vertex
  [vertexes :- [m/Vertex]]
  (map (partial apply min) (columise vertexes)))
