(ns stl-collector.core
  (:require [stl-collector.reader :as r]
            [stl-collector.writer :as w]
            [stl-collector.flattener :as f]
            [clojure.java.io :as io]))

;; Machine Definitions in mm
(def MICRO-3D [75.0 75.0 75.0])
(def MAKEBOT [252.0 199.0 150.0])


(defn get-stl-files [dir]
  (map #(.getAbsolutePath %)
       (filter #(.endsWith (.getName %) ".stl")
               (file-seq (io/file dir)))))

(defn combine-files [machine buffer output dir]
  "Reads every binary stl file in dir, combines them into a single stl file 'output'"
  (let [stls (map r/read-stl (get-stl-files dir))
        stl (f/collect machine buffer stls)]
    (w/write-stl stl output)))
