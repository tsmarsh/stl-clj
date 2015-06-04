(ns stl-collector.model
  (:require [schema.core :as s]))

(def Vertex [ (s/one Double "x") (s/one Double "y") (s/one Double "z")])

(def Face [(s/one Vertex "1") (s/one Vertex "2") (s/one Vertex "3")])

(def Facet {:normal Vertex
            :vertices Face})
