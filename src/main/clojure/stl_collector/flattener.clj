(ns stl-collector.flattener
  (:require [stl-collector.transforms :as t]
            [stl-collector.model :as m]
            [schema.core :as s]))

(s/defn fit :- s/Bool
  [container :- m/Vertex
   object :- m/Vertex]
  (= 0 (->> (map - container object)
            (filter (partial > 0))
            count)))

(s/defn package :- [m/Face]
  [[s & stls] :- [m/STL]
   buffer :- Double
   machine :- m/Vertex
   d :- [m/Face]]
  (let [sf (t/facify s)
        dx (t/distribute-x [d sf] buffer)
        cx (t/combine dx)
        cubex (t/bounding-cube cx)]
    (if (fit machine cubex)
      (if (seq stls)
        (recur stls buffer machine cx)
        cx)
      (let [dz (t/distribute-z [d sf] buffer)
            cz (t/combine dz)
            cubez (t/bounding-cube cz)]
        (if (fit machine cubez)
          (if (seq stls)
            (recur stls buffer machine cz)
            cz)
          d)))))

(s/defn collect :- m/STL
  [machine :- m/Vertex
   buffer :- Double
   [f & stls] :- [m/STL]]
  (let [faces (t/facify f)
        cube (t/bounding-cube faces)]
    (t/normalize (if (fit machine cube) 
                   (if (seq stls)
                     (package stls buffer machine faces)
                     faces)
                   []))))
