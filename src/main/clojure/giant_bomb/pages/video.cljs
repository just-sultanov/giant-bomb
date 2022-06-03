(ns giant-bomb.pages.video
  (:require
    [giant-bomb.components :as components]
    [giant-bomb.video.core]
    [heroicons.outline :as icons.outline]
    [re-frame.core :as rf]))


(defn breadcrumb
  [{:keys [guid name]}]
  [:nav {:aria-label "Breadcrumb"}
   [:ol.max-w-2xl.mx-auto.px-4.flex.items-center.space-x-2.sm:px-6.lg:max-w-7xl.lg:px-8
    {:role "list"}
    [:li
     [:div.flex.items-center
      [:a.mr-2.text-sm.font-medium.text-gray-900 {:href @(rf/subscribe [:href :page/home])}
       "Home"]
      [icons.outline/chevron-right-icon {:class "w-4 h-4 text-gray-400"}]]]
    [:li
     [:div.flex.items-center
      [:a.mr-2.text-sm.font-medium.text-gray-900 {:href @(rf/subscribe [:href :page/videos])} "Videos"]
      [icons.outline/chevron-right-icon {:class "w-4 h-4 text-gray-400"}]]]
    [:li.text-sm
     [:a.font-medium.text-gray-500.hover:text-gray-600 {:href @(rf/subscribe [:href :page/video {:id guid}]) :aria-current "page"} name]]]])


(defn video-card
  [{:as video :keys [name embed_player deck video_categories associations]}]
  [:div.bg-white.dark:bg-gray-500.rounded-md.shadow-md
   [:div.pt-6
    [breadcrumb video]
    [:div.max-w-2xl.mx-auto.pt-10.pb-16.px-4.sm:px-6.lg:max-w-7xl.lg:pt-16.lg:pb-24.lg:px-8.lg:grid.lg:grid-cols-3.lg:gap-x-8 {:class "lg:grid-rows-[auto,auto,1fr]"}
     [:div.mt-4.lg:mt-0.lg:row-span-3
      [:h2.sr-only "Video details"]
      [:div.aspect-w-3.aspect-h-4.rounded-lg.overflow-hidden.lg:block
       [:iframe {:src             embed_player
                 :frameborder     0
                 :allow           "fullscreen; accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                 :allowfullscreen true}]]]
     [:div.lg:col-span-2.lg:border-l.lg:border-gray-200.lg:pl-8.sm:mt-4
      [:div.flex.justify-between.content-center.items-center
       [:h1.text-2xl.font-extrabold.tracking-tight.text-gray-900.sm:text-3xl name]
       [:button.bg-green-500.hover:bg-green-400.rounded-md.shadow-md.px-4.py-2.inline-flex.items-center.gap-2.text-white
        {:type "button"}
        [icons.outline/eye-icon {:class "w-6 h-6"}]
        [:span.text-md "Subscribe"]]]
      [:div.py-10.lg:pt-6.lg:pb-16.lg:col-start-1.lg:col-span-2
       [:div
        [:h3.sr-only "Description"]
        [:div.space-y-6
         [:p.text-base.text-gray-900 deck]]]
       [:div.mt-6
        [:h3.text-sm.font-medium.text-gray-900 "Categories"]
        [:div.mt-4.gap-2.px-4.pb-4
         (into [:ul.flex.flex-wrap.gap-2]
               (for [{:keys [name]} video_categories]
                 [:li.rounded-sm.bg-gray-200.px-2
                  [:p.text-gray-700 {:class "text-[12px]"} name]]))]]
       [:div.mt-6
        [:h3.text-sm.font-medium.text-gray-900 "Associations"]
        [:div.mt-4.gap-2.px-4.pb-4
         (into [:ul.flex.flex-wrap.gap-2]
               (for [{:keys [name]} associations]
                 [:li.rounded-sm.bg-gray-200.px-2
                  [:p.text-gray-700 {:class "text-[12px]"} name]]))]]]]]]])


(defn video-details
  [id]
  (let [readiness @(rf/subscribe [:video/readiness id])
        video     @(rf/subscribe [:video id])]
    ^{:keys readiness}
    [:div.mt-4
     [components/loader {:state      readiness
                         :on-loading [components/loading-spinner "Loading..."]
                         :on-failed  [components/failed-spinner "Something went wrong..."]
                         :on-idle    [video-card video]}]]))


(defn page
  []
  (let [{{:keys [id]} :path} @(rf/subscribe [:navigation/route-params])]
    [:div.max-w-7xl.mx-auto.mt-24.p-10
     [video-details id]]))
