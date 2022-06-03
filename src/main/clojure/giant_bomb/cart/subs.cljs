(ns giant-bomb.cart.subs
  (:require
    [re-frame.core :as rf]))


(rf/reg-sub
  :cart
  (fn [db _]
    (or (:cart db) #{})))


(rf/reg-sub
  :cart/has-item?
  :<- [:cart]
  (fn [cart [_ item]]
    (contains? cart item)))


(rf/reg-sub
  :cart/game
  (fn [db [_ id]]
    (let [cart (or (:cart db) #{})]
      (->> (:games db)
           (filter
             (fn [{:keys [guid]}]
               (and (contains? cart guid)
                    (= guid id))))
           (first)))))
