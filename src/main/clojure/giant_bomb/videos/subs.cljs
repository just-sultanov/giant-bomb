(ns giant-bomb.videos.subs
  (:require
    [re-frame.core :as rf]))


(rf/reg-sub
  :videos/readiness
  :<- [:app/readiness]
  (fn [readiness]
    (or (:videos readiness) :idle)))


(rf/reg-sub
  :videos
  (fn [db _]
    (:videos db)))
