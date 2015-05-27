(ns stl-collector.writer
  (:require
   [stl-collector.model :as stl]
   [stl-collector.file :as stl-file]
   [clojure.java.io :as io]
   [nio.core :as nio]
   [clojure.pprint :as pp])
  
  (:import (java.nio ByteOrder)))

(defn pad [buffer num_bytes]
  (doseq [_ (range num_bytes)]
    (.put buffer (byte 0))))

(defn write-vector
  [buffer vs]
  (let [{x :x
         y :y
         z :z} vs
         _ (println [x y z])]
    (.putFloat buffer (float x))
    (.putFloat buffer (float y))
    (.putFloat buffer (float z))
    [x y z]))

(defn write-header
  [buffer count]
  (pad buffer stl-file/MESSAGE_LENGTH)
  (.putInt buffer count))

(defn write-facet
  [buffer facet]
  (let [n  (write-vector buffer (:normal facet))
        vs (map (partial write-vector buffer) (:vertices facet))]
    (pad buffer 2)))

(defn calculate-file-size
  [num_facets]
  (+ stl-file/HEADER_LENGTH (* num_facets stl-file/BYTES_PER_FACET)))

(defn write-stl
  [stl-seq ^String filename]
  (let [num_facets (count stl-seq)
        offset 0
        length (calculate-file-size num_facets)]
    (let [buffer (doto (nio/mmap filename offset length)
                  (.order ByteOrder/LITTLE_ENDIAN))]
      (write-header buffer num_facets)
      (doseq [facet stl-seq]
        (write-facet buffer facet)))))




