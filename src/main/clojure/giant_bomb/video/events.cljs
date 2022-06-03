(ns giant-bomb.video.events
  (:require
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [giant-bomb.api :as api]
    [giant-bomb.logger :as log :include-macros true]
    [re-frame.core :as rf]))


(rf/reg-event-fx
  :api/fetch-video->success
  (fn-traced [{db :db} [_ id res]]
    {:db       (->> @res :results (assoc-in db [:video id]))
     :dispatch [:set-readiness [:video id] :ready]}))


(rf/reg-event-fx
  :api/fetch-video->failure
  (fn-traced [{db :db} [_ id error]]
    ;; TODO: [2022-06-01, ilshat@sultanov.team] Show error notification
    (log/error :api/fetch-video->failure @error)
    {:db       (update db :video dissoc id)
     :dispatch [:set-readiness [:video id] :failed]}))



(rf/reg-event-fx
  :api/fetch-video
  (fn-traced [_ [_ id params]]
    {:dispatch   [:set-readiness [:video id] :loading]
     ::api/fetch {:id         id
                  :resource   :video
                  :params     params
                  :on-success [:api/fetch-video->success id]
                  :on-failure [:api/fetch-video->failure id]}}))
