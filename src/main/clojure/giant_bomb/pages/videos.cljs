(ns giant-bomb.pages.videos
  (:require
    [giant-bomb.components :as components]
    [giant-bomb.videos.core]
    [re-frame.core :as rf]))



(defn video-title
  [{:keys [guid name]}]
  [:a.inline-flex.justify-between.items-center.gap-2
   {:href @(rf/subscribe [:href :page/video {:id guid}])}
   [:span.absolute.inset-0 {:aria-hidden true}]
   [:span.text-sm.font-medium.text-gray-900 name]])



(defn video-image
  [image]
  [:img.w-full.h-full.object-center.object-cover
   {:src (:super_url image) :alt (:medium_url image)}])



(defn video-associations
  [associations]
  [:div.mt-4.gap-2.px-4.pb-4
   (into [:ul.flex.flex-wrap.gap-2]
         (for [{:keys [name guid]} associations]
           [:li.rounded-sm.bg-gray-200.px-2
            [:p.text-gray-600 {:class "text-[10px]"} name]]))])


(defn video-categories
  [categories]
  [:div.mt-4.gap-2.px-4.pb-4
   (into [:ul.flex.flex-wrap.gap-2.mt-2]
         (for [{:keys [name]} categories]
           [:li.rounded-sm.bg-gray-200.px-2
            [:p.text-gray-600 {:class "text-[10px]"} name]]))])



(defn video-card
  [{:as video :keys [image video_categories associations deck]}]
  (js/console.log :video video)
  [:div.group.relative.bg-white.rounded-md.shadow-md
   [:div.w-full.min-h-80.bg-gray-200.aspect-w-1.aspect-h-1.rounded-t-md.overflow-hidden.group-hover:opacity-90.lg:h-80.lg:aspect-none
    [video-image image]]
   [:div.mt-4.flex.justify-between.gap-2.px-4.pb-4
    [:div
     [:h3.text-sm.text-gray-700
      [video-title video]]
     [:p.mt-1.text-sm.text-gray-500.text-justify deck]]]
   [video-categories video_categories]
   [video-associations associations]])


;; FIXME: [2022-06-03, ilshat@sultanov.team] Add pagination and don't remove previous results from the db
(defn videos-cards
  [videos]
  [:div.max-w-2xl.mx-auto.lg:max-w-7xl
   (into [:div.mt-6.grid.grid-cols-1.gap-y-12.gap-x-6.sm:grid-cols-2.lg:grid-cols-4.xl:gap-x-8]
         (for [video videos]
           [video-card video]))])



(defn videos-list
  []
  (let [readiness @(rf/subscribe [:videos/readiness])
        videos     @(rf/subscribe [:videos])]
    ^{:keys readiness}
    [:div
     [components/loader {:state      readiness
                         :on-loading [components/loading-spinner "Loading..."]
                         :on-failed  [components/failed-spinner "Something went wrong..."]
                         :on-idle    [videos-cards videos]}]]))



(defn page
  []
  [:div.max-w-7xl.mx-auto.mt-32.p-10
   [videos-list]])

