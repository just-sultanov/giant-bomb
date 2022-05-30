(ns giant-bomb.logger
  (:require
    [giant-bomb.env :as env]
    [lambdaisland.glogi :as log]
    [lambdaisland.glogi.console :as console]))


(defn init!
  "Logger initializer."
  ([]
   (init! env/logger-level))

  ([logger-level]
   (let [level (or logger-level :off)]
     (console/install!)
     (log/set-levels
       {:glogi/root level
        'giant-bomb level})
     (log/info :msg "Logger successfully initialized" :level level))))
