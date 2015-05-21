(ns stl-collector.core
  (:import (org.j3d.loaders.stl STLFileReader)))


(defn get-next-facet [reader normal vertices]
  (.getNextFacet reader normal vertices)
  (let [n (into [] normal)
        [vx vy vz :as vs] vertices]
    (lazy-seq (cons
               {:normal n
                :vertices (map #(into [] %) vs)}          
               (get-next-facet reader normal vertices)))))

(defn open-stl [^String filename]
  (let [reader (STLFileReader. filename)
        normal (make-array Double/TYPE 3)
        vertices (into-array (repeat 3 (make-array Double/TYPE 3)))]
    (get-next-facet reader normal vertices)))
