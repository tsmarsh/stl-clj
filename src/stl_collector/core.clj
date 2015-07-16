(ns stl-collector.core
  (:require [stl-collector.reader :as r]
            [stl-collector.writer :as w]
            [stl-collector.flattener :as f]
            [clojure.java.io :as io])
  (:import (java.io File))
  (:gen-class :name stlcollector.Core
              :methods [#^{:static true} [combine [doubles double java.io.File java.io.File] void]]))

(set! *warn-on-reflection* true)

(defn get-stl-files [^File dir]
  (map (fn [^File f] (.getAbsolutePath f))
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

(defn -combine [machine ^Double buffer ^File output-dir ^File input-dir]
  (combine-all-files (seq machine) buffer output-dir input-dir))
