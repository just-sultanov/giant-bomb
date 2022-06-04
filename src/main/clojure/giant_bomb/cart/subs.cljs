(ns giant-bomb.cart.subs
  (:require
    [re-frame.core :as rf]))


(rf/reg-sub
  :cart
  (fn [db _]
    (or (:cart db) {})))


(rf/reg-sub
  :cart/count
  :<- [:cart]
  (fn [cart _]
    (count cart)))


(rf/reg-sub
  :cart/has-item?
  :<- [:cart]
  (fn [cart [_ {:keys [guid]}]]
    (boolean (get cart guid))))


(rf/reg-sub
  :cart/game
  :<- [:cart]
  (fn [cart [_ id]]
    (get cart id)))
