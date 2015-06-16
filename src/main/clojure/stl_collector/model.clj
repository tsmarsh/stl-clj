(ns stl-collector.model
  (:require [schema.core :as s]))

;; [0.0 0.0 0.0] 
(def Vertex [ (s/one Double "x") (s/one Double "y") (s/one Double "z")])

;;[[0.0 0.0 0.0]
;;  [0.0 0.0 0.0]
;;  [0.0 0.0 0.0]]
(def Face [(s/one Vertex "1") (s/one Vertex "2") (s/one Vertex "3")])

;;{:normal [0.0 0.0 0.0]
;; :vertices [[0.0 0.0 0.0]
;;            [0.0 0.0 0.0]
;;            [0.0 0.0 0.0]]}

(def Facet {:normal Vertex
            :vertices Face})

;;[ {:normal [0.0 0.0 0.0]
;; :vertices [[0.0 0.0 0.0]
;;            [0.0 0.0 0.0]
;;            [0.0 0.0 0.0]]}
;;{:normal [0.0 0.0 0.0]
;; :vertices [[0.0 0.0 0.0]
;;            [0.0 0.0 0.0]
;;            [0.0 0.0 0.0]]}]
(def STL [Facet])

