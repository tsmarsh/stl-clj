(ns stl-collector.model)

(defrecord Vertex [^Double x ^Double y ^Double z])

(defrecord Facet [^Vertex normal vertices])
