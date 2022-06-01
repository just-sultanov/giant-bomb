(ns giant-bomb.core
  (:require
    [giant-bomb.api :as api]
    [giant-bomb.db :as db]
    [giant-bomb.logger :as log]
    [giant-bomb.pages.root :as root]
    [giant-bomb.router :as router]
    [giant-bomb.storage]
    [goog.dom :as gdom]
    [re-frame.core :as rf]
    [reagent.dom :as dom]))


(defn square
  [x]
  (* x x))


(defn init!
  "Initialize services."
  []
  (log/init!)
  (api/init!))


(defn mount-root
  "Mount root component."
  {:dev/after-load true}
  []
  (when-some [root-elem (gdom/getElement "root")]
    (rf/clear-subscription-cache!)
    (router/init!)
    (dom/render [root/page] root-elem)))


(defn -main
  "Application entry point."
  {:export true}
  [& _args]
  (init!)
  (rf/dispatch-sync [::db/init])
  (mount-root))
