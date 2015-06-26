(ns stl-collector.flattener
  (:require [stl-collector.transforms :as t]
            [stl-collector.model :as m]
            [schema.core :as s]))

(s/defn fit :- s/Bool
  [container :- m/Vertex
   object :- m/Vertex]
  (= 0 (->> (map - container object)
            (filter (partial > 0 ))
            count)))

(s/defn collect :- m/STL
  [machine :- m/Vertex
   stls :- [m/STL]]
  (let [faces (t/facify (first stls))
        cube (t/bounding-cube faces)]
    (if (fit machine cube) 
      (first stls)
      [])))
