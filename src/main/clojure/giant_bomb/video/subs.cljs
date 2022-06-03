(ns giant-bomb.video.subs
  (:require
    [re-frame.core :as rf]))


(rf/reg-sub
  :video/readiness
  :<- [:app/readiness]
  (fn [readiness [_ id]]
    (get readiness [:video id] :idle)))


(rf/reg-sub
  :video
  (fn [db [_ id]]
    (get-in db [:video id])))
