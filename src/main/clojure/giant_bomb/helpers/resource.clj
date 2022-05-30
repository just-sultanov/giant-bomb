(ns giant-bomb.helpers.resource
  (:refer-clojure :exclude [slurp])
  (:require
    [clojure.core :as c]
    [clojure.java.io :as io]))


(defmacro slurp
  [path]
  (some->> path
           (io/resource)
           (c/slurp)))
