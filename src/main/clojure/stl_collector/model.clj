(ns stl-collector.model
  (:require [schema.core :as s]))

(def Vertex [ (s/one Float "x") (s/one Float "y") (s/one Float "z")])

(def Facet {:normal Vertex
            :vertices [(s/one Vertex "xs") (s/one Vertex "ys") (s/one Vertex "zs")]})
