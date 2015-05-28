(ns stl-collector.writer
  (:require
   [stl-collector.model :as stl]
   [stl-collector.file :as stl-file]
   [clojure.java.io :as io]
   [nio.core :as nio]
   [clojure.pprint :as pp])
  
  (:import (java.nio ByteOrder DirectByteBuffer)))

(defn calculate-offset [offset n size]
  (+ offset (* n size)))

(defn pad [buffer offset num_bytes]
  (if (> num_bytes 0)
    (do 
      (.put buffer offset (byte 0))
      (recur buffer (inc offset) (dec num_bytes)))
    offset))

(defn write-vector
  [^DirectByteBuffer buffer ^Integer offset vertex]
  (let [{x :x y :y z :z} vertex
        values [x y z]
        put-float (fn [o vs]
                    (if (seq vs)
                      (let [[v & vss] vs]
                        (.putFloat buffer o v)
                        (recur (+ o 4) vss))
                      o))]
    (put-float offset values)))

(defn write-header
  [buffer count]
  (pad buffer 0 stl-file/MESSAGE_LENGTH)
  (.putInt buffer 0 count)
  stl-file/HEADER_LENGTH)

(defn write-facet
  [buffer offset facet]
  (let [post_normal_offset (write-vector buffer offset (:normal facet))
        process_vertices (fn [buffer offset facets]
                           (if (seq facets)
                             (let [[f & fs] facets
                                   new_offset (write-vector buffer offset f)]
                               (recur buffer new_offset fs))
                             offset))
        post_vertices_offset (process_vertices buffer
                                               post_normal_offset
                                               (:vertices facet))]
    pad buffer post_vertices_offset 2))

(defn calculate-file-size
  [num_facets]
  (+ stl-file/HEADER_LENGTH (* num_facets stl-file/BYTES_PER_FACET)))

(defn write-stl
  [stl-seq ^String filename]
  (let [num_facets (count stl-seq)
        offset 0
        length (calculate-file-size num_facets)]
    (let [buffer             (doto (nio/mmap filename
                                             offset
                                             length)
                               (.order ByteOrder/LITTLE_ENDIAN))
          post-header-offset (write-header buffer num_facets)
          process-facets     (fn [buffer offset facets]
                               (if (seq facets)
                                 (let [[f & fs] facets
                                       new-offset (write-facet buffer offset f)]
                                   (recur buffer new-offset fs))
                                 offset))]
      (process-facets buffer post-header-offset stl-seq))))




