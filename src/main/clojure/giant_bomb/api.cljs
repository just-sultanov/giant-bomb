(ns giant-bomb.api
  (:require
    [clojure.set :as set]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [giant-bomb.env :as env]
    [giant-bomb.helpers.bean :as b]
    [giant-bomb.helpers.string :as str]
    [giant-bomb.logger :as log :include-macros true]
    [kitchen-async.promise :as p]
    [lambdaisland.fetch :as http]
    [re-frame.core :as rf]
    [tenet.response :as r]))


;;
;; API
;; Documentation URL: https://www.giantbomb.com/api/documentation
;;

(def errors
  #{::api-key-invalid
    ::object-not-found
    ::url-format-error
    ::json-callback-required
    ::filter-error
    ::subscriber-only-video
    ::unknown-status-code})


(defn init!
  []
  (swap! r/*registry into errors)
  (log/info :msg "Giant Bomb API errors are registered" :errors errors))


(defn- as-not-found
  [{:keys [resource id on-failure]} {:keys [body]}]
  (let [res (cond-> {:resource resource
                     :response body}
              id (assoc :id id))]
    (on-failure (r/as-not-found res))))


(defn- as-success
  [{:keys [on-success on-failure]} {:keys [body]}]
  (let [{:as res :keys [status_code]} (b/bean body)]
    (case status_code
      1 (on-success (r/as ::success res)) ; ok
      100 (on-failure (r/as ::api-key-invalid res)) ; invalid API key
      101 (on-failure (r/as ::object-not-found res)) ; object not found
      102 (on-failure (r/as ::url-format-error res)) ; error in URL format
      103 (on-failure (r/as ::json-callback-required res)) ; 'jsonp' format requires a 'json_callback' argument
      104 (on-failure (r/as ::filter-error res)) ; filter error
      105 (on-failure (r/as ::subscriber-only-video res)) ; subscriber only video is for subscribers only
      (on-failure (r/as ::unknown-status-code res)) ; unknown status code
      )))


(defn- as-error
  [{:keys [resource id on-failure]} {:keys [body]}]
  (let [res (cond-> {:resource resource
                     :response body}
              id (assoc :id id))]
    (on-failure (r/as-error res))))


(defn fetch
  {:arglists '([{:as request :keys [resource id params on-success on-failure]}])}
  [{:as request :keys [resource id params on-failure]}]
  (let [url  (if id
               (str/format "/api/%s/%s/" (name resource) (name id))
               (str/format "/api/%s/" (name resource)))
        opts {:query-params (merge {:api_key env/api-key :format "json"} params)
              :accept       :json
              :content-type :json}]
    (log/trace :msg "Sending a request" :resource resource :id id :params params)
    (-> (http/get url opts)
        (p/then (fn [response]
                  (log/trace :msg "Response received" :resource resource :id id :params params :response response)
                  (case (:status response)
                    404 (as-not-found request response)
                    200 (as-success request response)
                    (as-error request response))))
        (p/catch* (fn [error]
                    (log/error :msg "Request failed with an error" :resource resource :id id :params params :error error)
                    (on-failure (r/as-error error)))))))


(defn- wrap-event
  [event]
  (fn [res]
    (rf/dispatch (conj event res))))


(rf/reg-fx
  ::fetch
  (fn [{:as params :keys [on-success on-failure]}]
    (cond-> params
      (sequential? on-success) (update :on-success wrap-event)
      (sequential? on-failure) (update :on-failure wrap-event)
      :always fetch)))


(rf/reg-event-fx
  ::fetch
  (fn-traced [_ [_ params]]
    {::fetch params}))


(defn process-response
  [res]
  (let [res'   @res
        data   (:results res')
        params (-> res'
                   (select-keys [:number_of_total_results :number_of_page_results :limit :offset])
                   (set/rename-keys {:number_of_total_results :total, :number_of_page_results :page}))]
    {:data data, :params params}))
