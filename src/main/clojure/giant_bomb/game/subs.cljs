(ns giant-bomb.game.subs
  (:require
    [re-frame.core :as rf]))


(rf/reg-sub
  :game/readiness
  :<- [:app/readiness]
  (fn [readiness [_ id]]
    (get readiness [:game id] :idle)))


(rf/reg-sub
  :game
  (fn [db [_ id]]
    (get-in db [:game id])))
