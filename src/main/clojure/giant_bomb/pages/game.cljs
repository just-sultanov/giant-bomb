(ns giant-bomb.pages.game
  (:require
    [cljs.pprint :as pprint]
    [giant-bomb.components :as components]
    [giant-bomb.game.events]
    [giant-bomb.game.subs]
    [re-frame.core :as rf]))


(comment
  (rf/dispatch [:api/fetch-game "3030-4725" {:field_list ["genres" "name"]}])
  )


(defn details
  [id]
  (let [readiness @(rf/subscribe [:game/readiness id])
        game     @(rf/subscribe [:game id])]
    ^{:keys readiness}
    [:div.max-w-7xl.mx-auto.mt-32.p-10
     [:h1 "Game page"]
     [components/loader {:state      readiness
                         :on-loading [components/loading-spinner]
                         :on-ready   [components/ready-spinner]
                         :on-failed  [components/failed-spinner]
                         :on-idle    [:pre (with-out-str (pprint/pprint game))]}]]))


(defn page
  []
  (let [{{:keys [id]} :path} @(rf/subscribe [:navigation/route-params])]
    (when id
      [details id])))
