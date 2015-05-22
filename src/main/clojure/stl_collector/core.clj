(ns stl-collector.core
  (:require [clojure.java.io :as io]
            [nio.core :as nio])
  (:import (org.j3d.loaders.stl STLFileReader)
           (java.io DataOutputStream)))

(def MESSAGE_LENGTH 80)
(def BYTES_PER_FACET 50)

(defrecord Vertex [^Double x ^Double y ^Double z])

(defrecord Facet [^Vertex normal vertices])

(defn get-next-facet [reader normal vertices]
  (.getNextFacet reader normal vertices)
  (let [n (into [] normal)
        [vx vy vz :as vs] vertices]
    (lazy-seq (cons (Facet. (apply ->Vertex normal)
                            (map #(apply ->Vertex %) vertices))          
               (get-next-facet reader normal vertices)))))

(defn open-stl [handle]
  (let [reader (STLFileReader. handle)
        normal (make-array Double/TYPE 3)
        vertices (into-array (repeat 3 (make-array Double/TYPE 3)))]
    (get-next-facet reader normal vertices)))

(defn pad [buffer num_bytes]
  (doseq [_ (range num_bytes)]
    (.put buffer (byte 0))))

(defn write-vector
  [buffer
   vs]
  (doseq [v vs]
    (.putFloat buffer (float v)))
  (pad buffer 2))

(defn write-header
  [buffer message count]
  (pad buffer MESSAGE_LENGTH)
  (.putInt buffer count))

(defn write-stl
  [stl-seq ^String filename]
  (let [num_facets (count stl-seq)
        offset 0
        length (+ MESSAGE_LENGTH 4 (* num_facets BYTES_PER_FACET))
        buffer (doto (nio/mmap filename offset length)
                  (.order java.nio.ByteOrder/LITTLE_ENDIAN)
                  (.asFloatBuffer))]
    (write-header buffer "" num_facets)
    (doseq [facet stl-seq]
        (do 
          (write-vector buffer (:normal stl-seq))
          (map (partial write-vector buffer) (:vertices stl-seq))))))
