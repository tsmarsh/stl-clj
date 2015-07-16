(ns stl-collector.utils)

(defn find-and-extract
  [m k]
  (let [[v & vs] (get m k)]
    (if (seq vs)
      [v (assoc m k vs)]
      [v (dissoc m k)])))

(defn merge-to-list
  [ks vs]
  (let [key-pairs (map (fn [k v] {k [v]})
                       ks vs)]
    (if (seq key-pairs)
      (apply merge-with
             into
             key-pairs)
      {})))

(defn flatten-to-list
  [m]
  (flatten (vals m)))