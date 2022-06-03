(ns giant-bomb.cart.events
  (:require
    [re-frame.core :as rf]))


(rf/reg-event-db
  :cart/add-item
  (fn [db [_ item]]
    (update db :cart (fnil conj #{}) item)))


(rf/reg-event-db
  :cart/remove-item
  (fn [db [_ item]]
    (update db :cart (fnil disj #{}) item)))


(rf/reg-event-db
  :cart/clear
  (fn [db _]
    (assoc db :cart #{})))
