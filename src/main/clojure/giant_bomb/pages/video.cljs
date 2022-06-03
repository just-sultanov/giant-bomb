(ns giant-bomb.pages.video
  (:require
    [cljs.pprint :as pprint]
    [giant-bomb.components :as components]
    [giant-bomb.video.core]
    [re-frame.core :as rf]))


(comment
  (rf/dispatch [:api/fetch-video "2300-17788" {:field_list ["associations" "name"]}])
  )


(defn details
  [id]
  (let [readiness @(rf/subscribe [:video/readiness id])
        video     @(rf/subscribe [:video id])]
    ^{:keys readiness}
    [:div.max-w-7xl.mx-auto.mt-32.p-10
     [:h1 "Video page"]
     [components/loader {:state      readiness
                         :on-loading [components/loading-spinner]
                         :on-ready   [components/ready-spinner]
                         :on-failed  [components/failed-spinner]
                         :on-idle    [:pre (with-out-str (pprint/pprint video))]}]]))


(defn page
  []
  (let [{{:keys [id]} :path} @(rf/subscribe [:navigation/route-params])]
    (when id
      [details id])))
