(ns stl-collector.reader
  (:require
   [clojure.core.matrix :as mx]
   [stl-collector.model :as m]
   [stl-collector.file :as stl-file]
   [nio.core :as nio]
   [schema.core :as s])

  (:import (java.nio ByteOrder DirectByteBuffer)))

(s/defn read-vector :- m/Vertex
  [buffer :- DirectByteBuffer
   offset :- s/Int]
  (for [n (range 3)]
    (double (.getFloat buffer (+ offset (* 4 n))))))

(s/defn read-header :- s/Int
  [buffer :- DirectByteBuffer]
  (.getInt buffer stl-file/MESSAGE_LENGTH))

(s/defn read-facet :- m/Facet
  [buffer :- DirectByteBuffer
   offset :- s/Int]
  (let [normal    (read-vector buffer offset)
        new_offset (+ offset 12)
        vertices (mx/matrix (for [n (range 3)] (read-vector buffer (+ new_offset (* 12 n)))))]
    {:normal normal :vertices vertices}))

(s/defn read-stl :- m/STL
  [filename]
  (let [buffer (doto (nio/mmap filename)
                 (.order ByteOrder/LITTLE_ENDIAN))]
    (let [num_facets (read-header buffer)]
      (for [n (range num_facets)]
        (try 
          (read-facet buffer
                      (+ stl-file/HEADER_LENGTH
                         (* n stl-file/BYTES_PER_FACET)))
          (catch Exception e
            (println "Trouble reading: " filename (.getMessage e))
            []))))))
