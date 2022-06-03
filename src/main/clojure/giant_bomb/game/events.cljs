(ns giant-bomb.game.events
  (:require
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [giant-bomb.api :as api]
    [giant-bomb.logger :as log :include-macros true]
    [re-frame.core :as rf]))


(rf/reg-event-fx
  :api/fetch-game->success
  (fn-traced [{db :db} [_ id res]]
    {:db       (->> @res :results (assoc-in db [:game id]))
     :dispatch [:set-readiness [:game id] :ready]}))


(rf/reg-event-fx
  :api/fetch-game->failure
  (fn-traced [{db :db} [_ id error]]
    ;; TODO: [2022-06-01, ilshat@sultanov.team] Show error notification
    (log/error :api/fetch-game->failure @error)
    {:db       (update db :game dissoc id)
     :dispatch [:set-readiness [:game id] :failed]}))



(rf/reg-event-fx
  :api/fetch-game
  (fn-traced [_ [_ id params]]
    {:dispatch   [:set-readiness [:game id] :loading]
     ::api/fetch {:id         id
                  :resource   :game
                  :params     params
                  :on-success [:api/fetch-game->success id]
                  :on-failure [:api/fetch-game->failure id]}}))
