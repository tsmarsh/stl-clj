(ns stl-collector.flattener
  (:require [stl-collector.transforms :as t]
            [stl-collector.model :as m]
            [stl-collector.utils :as u]
            [schema.core :as s]
            [packager.container :as c]
            [packager.box :as b]
            [packager.distributor :as d]))


(s/defn create-empty-container :- c/Container
  [[mx _ mz] :- m/Vertex]
  {:shelves []
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

(s/defn fill-container :- c/Container
  [container :- c/Container
   boxes :- [b/Box]]
  (let [sorted-boxes (sort-by (fn [[x y]] (- (* x y))) boxes)]
    (reduce (fn [cnt box] (c/add cnt box))
            container
            sorted-boxes)))

(s/defn collect :- m/STL
  [machine :- m/Vertex
   buffer :- Double
   stls :- [m/STL]]

  (let [cubes (build-cubes stls)
        boxes (build-buffered-boxes cubes buffer)
        boxes->stl (u/merge-to-list boxes stls)
        filled-container (fill-container (create-empty-container machine) boxes)
        distribution (d/distribute filled-container)]
    (t/normalize (t/combine (loop [[b & bs] boxes
                                   boxes->stl boxes->stl
                                   distribution distribution
                                   faces []]
                              (if b 
                                (let [[stl boxes->stl'] (u/find-and-extract boxes->stl b)
                                      [[dx dz :as t] distribution'] (u/find-and-extract distribution b)]
                                  (if t
                                    (let [face (t/translate (t/facify stl) [dx 0.0 dz])
                                          faces' (conj faces face)] 
                                      (if (seq distribution)
                                        (recur bs boxes->stl' distribution' faces')
                                         faces))
                                    (recur bs boxes->stl' distribution faces)))
                                faces))))))
