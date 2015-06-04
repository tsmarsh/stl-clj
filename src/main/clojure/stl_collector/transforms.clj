(ns stl-collector.transforms
  (:require [stl-collector.model :as m]
            [schema.core :as s]))

(s/defn maxima :- m/Vertex
  [vertexes :- [m/Vertex]]
  (map (partial apply max) (apply map vector vertexes)))

(s/defn minima :- m/Vertex
  [vertexes :- [m/Vertex]]
  (map (partial apply min) (apply map vector vertexes)))
