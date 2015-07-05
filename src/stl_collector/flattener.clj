(ns stl-collector.flattener
  (:require [stl-collector.transforms :as t]
            [stl-collector.model :as m]
            [schema.core :as s]
            [packager.container :as c]
            [packager.box :as b]
            [packager.distributor :as d]))


(s/defn create-empty-container :- c/Container
  [[mx _ mz] :- m/Vertex]
  {:boxes []
   :dimensions [mx mz]})

(s/defn build-cubes :- [m/Vertex]
  [stls :- [m/STL]]
  (map (comp t/bounding-cube t/facify) stls))

(s/defn build-buffered-boxes :- [b/Box]
  [cubes :- [m/Vertex]
   buffer :- Double]
  (map (fn [[w _ h]]
         [(+ buffer w) (+ buffer h)])
       cubes))


(defn merge-to-list
  [ks vs]
  (let [key-pairs (map (fn [k v] {k [v]})
                       ks vs)]
    (if (seq key-pairs)
      (apply merge-with
             into
             key-pairs)
      {})))

(s/defn fill-container :- c/Container
  [container :- c/Container
   boxes :- [b/Box]]
  (let [sorted-boxes (sort-by (fn [[x y]] (- (* x y))) boxes)]
    (reduce (fn [cnt box] (c/add cnt box))
            container
            sorted-boxes)))

(s/defn find-and-extract
  [m k]
  (let [[v & vs] (get m k)]
    (if (seq vs)
      [v (assoc m k vs)]
      [v (dissoc m k)])))

(s/defn collect :- m/STL
  [machine :- m/Vertex
   buffer :- Double
   stls :- [m/STL]]

  (let [cubes (build-cubes stls)
        boxes (build-buffered-boxes cubes buffer)
        boxes->stl (merge-to-list boxes stls)
        filled-container (fill-container (create-empty-container machine) boxes)
        distribution (d/distribute filled-container)]
    (loop [[b & bs] boxes
           boxes->stl boxes->stl
           distribution distribution]
      #_(let [[stl boxes->stl]
            (find-and-extract boxes->stl) box]))))
