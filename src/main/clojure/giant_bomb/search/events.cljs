(ns giant-bomb.search.events
  (:require
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [giant-bomb.api :as api]
    [giant-bomb.logger :as log :include-macros true]
    [re-frame.core :as rf]))


(rf/reg-event-db
  :search/set-query
  (fn [db [_ query]]
    (assoc db :search/query query)))


(rf/reg-event-fx
  :api/search->success
  (fn-traced [{db :db} [_ res]]
    {:db       (->> res (api/process-response) (assoc db :search/results))
     :dispatch [:set-readiness :search :ready]}))


(rf/reg-event-fx
  :api/search->failure
  (fn-traced [{db :db} [_ error]]
    ;; TODO: [2022-06-01, ilshat@sultanov.team] Show error notification
    (log/error :api/search->failure @error)
    {:db       (dissoc db :search/results)
     :dispatch [:set-readiness :search :failed]}))


(rf/reg-event-fx
  :api/search
  (fn-traced [{db :db} [_ {:as params :keys [query]}]]
    (let [query (or query (:search/query db))
          params (merge {:resources "game,video", :query query, :limit 100} params)]
      (if-not (seq query)
        {}
        {:dispatch   [:set-readiness :search :loading]
         ::api/fetch {:resource   :search
                      :params     params
                      :on-success [:api/search->success]
                      :on-failure [:api/search->failure]}}))))

