(ns stl-collector.core
  (:require [stl-collector.reader :as r]
            [stl-collector.writer :as w]
            [stl-collector.transforms :as t]))


(defn combine-files [output & filenames]
  (let [stls (map r/read-stl filenames)
        faces (map t/facify stls)
        distributed-faces (t/distribute-x faces 10)
        recombined-faces (t/combine distributed-faces)
        normalized-faces (t/normalize recombined-faces)]
    (w/write-stl normalized-faces output)))
