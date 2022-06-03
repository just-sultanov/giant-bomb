(ns giant-bomb.videos.events
  (:require
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [giant-bomb.api :as api]
    [giant-bomb.logger :as log :include-macros true]
    [re-frame.core :as rf]))


(rf/reg-event-fx
  :api/fetch-videos->success
  (fn-traced [{db :db} [_ res]]
    {:db       (->> res (api/process-response) (assoc db :videos))
     :dispatch [:set-readiness :videos :ready]}))


(rf/reg-event-fx
  :api/fetch-videos->failure
  (fn-traced [{db :db} [_ error]]
    ;; TODO: [2022-06-01, ilshat@sultanov.team] Show error notification
    (log/error :api/fetch-videos->failure @error)
    {:db       (dissoc db :videos)
     :dispatch [:set-readiness :videos :failed]}))


(rf/reg-event-fx
  :api/fetch-videos
  (fn-traced [{db :db} [_ params]]
    {:db         (dissoc db :videos)
     :dispatch   [:set-readiness :videos :loading]
     ::api/fetch {:resource   :videos
                  :params     params
                  :on-success [:api/fetch-videos->success]
                  :on-failure [:api/fetch-videos->failure]}}))
