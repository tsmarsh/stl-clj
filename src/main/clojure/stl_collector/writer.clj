(ns stl-collector.writer
  (:require
   [stl-collector.model :as stl]
   [stl-collector.file :as stl-file]
   [clojure.java.io :as io]
   [nio.core :as nio])

  (:import (java.nio ByteOrder)))

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
  (pad buffer stl-file/MESSAGE_LENGTH)
  (.putInt buffer count))

(defn write-stl
  [stl-seq ^String filename]
  (let [num_facets (count stl-seq)
        offset 0
        length (+ stl-file/MESSAGE_LENGTH 4 (* num_facets stl-file/BYTES_PER_FACET))
        buffer (doto (nio/mmap filename offset length)
                  (.order ByteOrder/LITTLE_ENDIAN)
                  (.asFloatBuffer))]
    (write-header buffer "" num_facets)
    (doseq [facet stl-seq]
        (do 
          (write-vector buffer (:normal stl-seq))
          (map (partial write-vector buffer) (:vertices stl-seq))))))




