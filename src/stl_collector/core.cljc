(ns stl-collector.core
  (:require [stl-collector.reader :as r]
            [stl-collector.writer :as w]
            [stl-collector.flattener :as f]
            [clojure.java.io :as io]))

(def micro3D [75.0 75.0 75.0])
(def maker [252.0 199.0 150.0])

(def buffer 5.0)

(defn get-stl-files [dir]
  (map #(.getAbsolutePath %)
       (filter #(.endsWith (.getName %) ".stl")
               (file-seq (io/file dir)))))

(defn combine-files [output dir]
  (let [stls (map r/read-stl (get-stl-files dir))
        stl (f/collect maker buffer stls)]
    (w/write-stl stl output)))
