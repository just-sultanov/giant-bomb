(ns giant-bomb.cart.subs
  (:require
    [re-frame.core :as rf]))


(rf/reg-sub
  :cart
  (fn [db _]
    (or (:cart db) #{})))
