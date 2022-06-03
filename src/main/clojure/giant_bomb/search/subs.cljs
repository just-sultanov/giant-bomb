(ns giant-bomb.search.subs
  (:require
    [re-frame.core :as rf]))


(rf/reg-sub
  :search/readiness
  :<- [:app/readiness]
  (fn [readiness]
    (or (:search readiness) :idle)))


(rf/reg-sub
  :search/results
  (fn [db _]
    (:search/results db)))


(rf/reg-sub
  :search/query
  (fn [db _]
    (or (:search/query db) "")))
