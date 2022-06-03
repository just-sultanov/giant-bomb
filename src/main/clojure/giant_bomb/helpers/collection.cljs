(ns giant-bomb.helpers.collection)


(defn index-by
  [f coll]
  (->> coll
       (reduce
         (fn [acc v]
           (assoc! acc (f v) v))
         (transient {}))
       (persistent!)))
