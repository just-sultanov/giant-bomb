(ns giant-bomb.pages.games
  (:require
    [cljs.pprint :as pprint]
    [giant-bomb.components :as components]
    [giant-bomb.games.events]
    [giant-bomb.games.subs]
    [re-frame.core :as rf]))


(defn page
  []
  (let [readiness @(rf/subscribe [:games/readiness])
        games     @(rf/subscribe [:games])]
    ^{:keys readiness}
    [:div.max-w-7xl.mx-auto.mt-32.p-10
     [:h1 "Games page"]
     [:a.inline-flex.justify-center.items-center.gap-1.text-violet-600.hover:text-violet-800.dark:text-violet-300.dark:hover:text-violet-400
      {:href @(rf/subscribe [:href :page/game {:id "3030-4725"}])}
      [:span "Game: 3030-4725"]]
     [components/loader {:state      readiness
                         :on-loading [components/loading-spinner]
                         :on-ready   [components/ready-spinner]
                         :on-failed  [components/failed-spinner]
                         :on-idle    [:pre (with-out-str (pprint/pprint games))]}]]))
