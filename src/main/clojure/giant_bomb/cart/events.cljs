(ns giant-bomb.cart.events
  (:require
    [re-frame.core :as rf]))


(rf/reg-event-db
  :cart/add-item
  (fn [db [_ {:as item :keys [guid]}]]
    (update db :cart assoc guid item)))


(rf/reg-event-db
  :cart/remove-item
  (fn [db [_ {:keys [guid]}]]
    (update db :cart dissoc guid)))


(rf/reg-event-db
  :cart/clear
  (fn [db _]
    (assoc db :cart {})))
