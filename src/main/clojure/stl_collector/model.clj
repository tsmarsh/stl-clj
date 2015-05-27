(ns stl-collector.model)

(defrecord Vertex [x  y  z])

(defrecord Facet [^Vertex normal vertices])
