(ns stl-collector.reader
  (:require
   [stl-collector.model :as stl]
   [stl-collector.file :as stl-file]
   [clojure.java.io :as io]
   [nio.core :as nio]
   [clojure.pprint :as pp])
  
   (:import (java.nio ByteOrder)))

(defn ignore [buffer num_bytes]
  (dotimes [_ num_bytes]
    (.get buffer)))

(defn read-vector
  [buffer]
  (let [[x y z] (doall (for [_ (range 3)] (.getFloat buffer)))]
    (stl/->Vertex x y z)))

(defn read-header
  [buffer]
  (ignore buffer stl-file/MESSAGE_LENGTH)
  (.getInt buffer))

(defn read-facet
  [buffer]
  (let [normal    (read-vector buffer)
        vertices  (doall (for [_ (range 3)] (read-vector buffer)))]
    (ignore buffer 2) 
    (stl/->Facet  normal vertices)))

(defn read-stl
  [filename]
  (let [buffer (doto (nio/mmap filename)
                 (.order ByteOrder/LITTLE_ENDIAN))]
    (let [num_facets (read-header buffer)
          _ (print num_facets)]
      (for [_ (range num_facets)]
        (read-facet buffer)))))
