(ns giant-bomb.pages.home
  (:require
    [giant-bomb.helpers.string :as str]
    [re-frame.core :as rf]))


(defn- video
  [n]
  [:video.absolute.z-0.w-auto.min-w-full.min-h-full.max-w-none
   {:autoPlay true :loop true :muted true}
   [:source {:src (str/format "assets/videos/%s.mp4" n) :type "video/mp4"}]])


(defn- hero
  []
  [:div.relative.z-10.p-10.flex.flex-col.items-center.gap-2.text-white
   [:span.text-4xl "Welcome to the world of video games"]
   [:div.flex-1
    [:a.ml-8.whitespace-nowrap.inline-flex.items-center.justify-center.px-4.py-2.border.border-transparent.rounded-md.shadow-sm.text-2xl.bg-violet-600.hover:bg-violet-700
     {:href @(rf/subscribe [:href :page/games])}
     [:span "Explore"]]]])


(defn- build-info
  []
  (when-some [info @(rf/subscribe [:app/build-info])]
    [:footer.absolute.bottom-2.right-2.z-10
     [:a.inline-flex.justify-center.items-center.gap-1.text-violet-600.hover:text-violet-800.dark:text-violet-300.dark:hover:text-violet-400
      {:href (:homepage info), :target "_blank"}
      [:i.fa-brands.fa-github]
      [:span.text-sm (str/format "v%s@%s" (:version info) (get-in info [:metadata :git/sha]))]]]))


(defn page
  []
  [:div.relative.flex.items-center.justify-center.h-screen.mb-12.overflow-hidden
   [hero]
   [video (rand-nth [1 2 3])]
   [build-info]])
