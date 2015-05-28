(ns stl-collector.reader
  (:require
   [stl-collector.model :as stl]
   [stl-collector.file :as stl-file]
   [clojure.java.io :as io]
   [nio.core :as nio]
   [clojure.pprint :as pp])
  
   (:import (java.nio ByteOrder DirectByteBuffer)))

(defn ignore [^DirectByteBuffer buffer
              ^Integer offset
              ^Integer num_bytes]
  (if (> num_bytes 0)
    (do 
      (.get buffer offset)
      (recur buffer (inc offset) (dec num_bytes)))
    offset))

(defn read-vector
  [^DirectByteBuffer buffer
   ^Integer offset]
  (let [[x y z] (for [n (range 3)] (.getFloat buffer (+ offset (* 4 n))))]
    (stl/->Vertex x y z)))

(defn read-header
  [^DirectByteBuffer buffer]
  (ignore buffer stl-file/MESSAGE_LENGTH 0)
  (.getInt buffer stl-file/MESSAGE_LENGTH))

(defn read-facet
  [^DirectByteBuffer buffer
   ^Integer offset]
  (let [normal    (read-vector buffer offset)
        new_offset (+ offset 12)
        vertices  (for [n (range 3)] (read-vector buffer (+ new_offset (* 12 n))))]
    (ignore buffer 2 (+ offset 16)) 
    (stl/->Facet  normal vertices)))

(defn read-stl
  [filename]
  (let [buffer (doto (nio/mmap filename)
                 (.order ByteOrder/LITTLE_ENDIAN))]
    (let [num_facets (read-header buffer)]
      (for [n (range num_facets)]
        (read-facet buffer
                    (+ stl-file/HEADER_LENGTH
                       (* n stl-file/BYTES_PER_FACET)))))))
