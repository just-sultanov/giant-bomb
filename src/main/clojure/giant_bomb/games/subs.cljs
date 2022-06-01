(ns giant-bomb.games.subs
  (:require
    [re-frame.core :as rf]))


(rf/reg-sub
  :games/readiness
  :<- [:app/readiness]
  (fn [readiness]
    (or (:games readiness) :idle)))


(rf/reg-sub
  :games
  (fn [db _]
    (:games db)))
