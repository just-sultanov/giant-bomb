(ns giant-bomb.env
  (:require
    [cljs.reader :as reader]
    [clojure.string :as str])
  (:require-macros
    [giant-bomb.helpers.resource :as resource]))


(goog-define mode "production")
(goog-define logger-level "info")
(goog-define default-language "en")

(goog-define api-url "N/A")
(goog-define api-key "N/A")


;; TODO: [2022-06-01, ilshat@sultanov.team] Rewrite using the shadow-cljs inject hooks
(def build-info
  (->> "com/gravie/giant-bomb/build.edn"
       (resource/slurp)
       (reader/read-string)))


(def develop?
  (= "develop" (str/lower-case mode)))


(def production?
  (= "production" (str/lower-case mode)))
