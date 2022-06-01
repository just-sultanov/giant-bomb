(ns giant-bomb.router
  (:require
    [giant-bomb.logger :as log :include-macros true]
    [giant-bomb.router.events]
    [giant-bomb.router.subs]
    [re-frame.core :as rf]
    [reitit.coercion.malli :as rcm]
    [reitit.frontend :as rfr]
    [reitit.frontend.easy :as rfe]))


(def routes
  [""
   ["/" {:name :page/home, :private false}]
   ["/search" {:name :page/search, :private false}]
   ["/checkout" {:name :page/checkout, :private false}]
   ["/games"
    {:name        :page/games
     :private     false
     :controllers [{:start #(rf/dispatch [:api/fetch-games])}]}]
   ["/game/:id"
    {:name        :page/game
     :private     false
     :controllers [{:parameters {:path  [:id]}
                    :start #(rf/dispatch [:api/fetch-game (get-in % [:path :id])])}]}]])


(def router
  (rfr/router
    routes
    {:data {:coercion rcm/coercion, :private true}}))


(defn on-navigate
  "Router `on-navigate` entry point. This function to be called when route changes."
  [matched-route]
  (rf/dispatch [:navigation/set-route matched-route]))


(defn init!
  "Router initializer."
  []
  (rfe/start!
    router
    on-navigate
    {:use-fragment true})
  (rf/dispatch-sync [:navigation/set-router router])
  (log/info :msg "Router successfully initialized"))
