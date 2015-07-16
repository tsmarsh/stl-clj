(ns stl-collector.core
  (:require [stl-collector.reader :as r]
            [stl-collector.writer :as w]
            [stl-collector.flattener :as f]
            [clojure.java.io :as io])
  (:import (java.io File)))

(defn get-stl-files [^File dir]
  (map #(.getAbsolutePath %)
       (filter (fn [^File f]
                 (.endsWith (.getName f) ".stl"))
               (file-seq (io/file dir)))))

(defn combine-files [machine buffer output dir]
  "Reads every binary stl file in dir, combines them into a single stl file 'output'"
  (let [stls (map r/read-stl (get-stl-files dir))
        {stl :stl r :rejections} (f/collect-with-rejections machine buffer stls)]
    (w/write-stl stl output)
    r))

(defn combine-all-files [machine buffer output-dir input-dir]
  (loop [file-number 0
         stls (map r/read-stl (get-stl-files input-dir))]
    (when (seq stls)
      (let [{stl :stl r :rejections} (f/collect-with-rejections machine buffer stls)]
        (when stl
          (do
            (w/write-stl stl (str output-dir "/" file-number ".stl"))
            (when (seq r)
              (recur (inc file-number) r))))))))
