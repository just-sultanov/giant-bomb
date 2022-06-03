(ns giant-bomb.games.events
  (:require
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [giant-bomb.api :as api]
    [giant-bomb.logger :as log :include-macros true]
    [re-frame.core :as rf]))


(rf/reg-event-fx
  :api/fetch-games->success
  (fn-traced [{db :db} [_ res]]
    {:db       (->> res (api/process-response) (assoc db :games))
     :dispatch [:set-readiness :games :ready]}))


(rf/reg-event-fx
  :api/fetch-games->failure
  (fn-traced [{db :db} [_ error]]
    ;; TODO: [2022-06-01, ilshat@sultanov.team] Show error notification
    (log/error :api/fetch-games->failure @error)
    {:db       (dissoc db :games)
     :dispatch [:set-readiness :games :failed]}))


(rf/reg-event-fx
  :api/fetch-games
  (fn-traced [_ [_ params]]
    {:dispatch   [:set-readiness :games :loading]
     ::api/fetch {:resource   :games
                  :params     params
                  :on-success [:api/fetch-games->success]
                  :on-failure [:api/fetch-games->failure]}}))
