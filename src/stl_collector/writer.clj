(ns stl-collector.writer
  (:require
   [stl-collector.file :as stl-file]
   [stl-collector.model :as m]
   [clojure.java.io :as io]
   [nio.core :as nio]
   [schema.core :as s])

  (:import (java.nio ByteOrder DirectByteBuffer)))

(set! *warn-on-reflection* true)

(s/defn pad :- s/Int
  [buffer :- DirectByteBuffer
   offset :- s/Int
   num_bytes :- s/Int]
  (if (> num_bytes 0)
    (do 
      (.put buffer offset (byte 0))
      (recur buffer (inc offset) (dec num_bytes)))
    offset))

(s/defn put-floats :- s/Int
  [buffer :- DirectByteBuffer
   o      :- s/Int
   vs     :- m/Vertex]
  (if (seq vs)
    (let [[v & vss] vs]
      (.putFloat buffer o (float v))
      (recur buffer (+ o 4) vss))
    o))

(s/defn write-vector :- s/Int
  [buffer :- DirectByteBuffer
   offset :- s/Int
   values :- m/Vertex]
  (put-floats buffer offset values))

(s/defn write-header :- s/Int
  [buffer :- DirectByteBuffer
   count  :- s/Int]

  (pad buffer 0 stl-file/MESSAGE_LENGTH)
  (.putInt buffer stl-file/MESSAGE_LENGTH count)
  stl-file/HEADER_LENGTH)

(s/defn write-facet :- s/Int
  [buffer :- DirectByteBuffer
   offset :- s/Int
   facet :- m/Facet]
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
    (pad buffer post_vertices_offset 2)))

(s/defn calculate-file-size :- s/Int
  [num_facets :- s/Int]
  (+ stl-file/HEADER_LENGTH (* num_facets stl-file/BYTES_PER_FACET)))

(s/defn process-facets :- s/Int
  [buffer :- DirectByteBuffer
   offset :- s/Int
   facets :- [m/Facet]] 
  (if (seq facets)
    (let [[f & fs] facets
          new-offset (write-facet buffer offset f)]
      (recur buffer new-offset fs))
    offset))

(s/defn write-stl :- s/Int
  [stl-seq :- m/STL
   filename]
  (let [num_facets (count stl-seq)
        offset 0
        length (calculate-file-size num_facets)]
    (let [buffer (doto (nio/mmap filename
                                 offset
                                 length)
                   (.order ByteOrder/LITTLE_ENDIAN))
          post-header-offset (write-header buffer num_facets)]
      (process-facets buffer post-header-offset stl-seq))))




