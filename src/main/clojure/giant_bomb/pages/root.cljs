(ns giant-bomb.pages.root
  (:require
    [giant-bomb.components :as components]
    [giant-bomb.pages.checkout :as checkout]
    [giant-bomb.pages.game :as game]
    [giant-bomb.pages.games :as games]
    [giant-bomb.pages.home :as home]
    [giant-bomb.pages.not-found :as not-found]
    [giant-bomb.pages.search :as search]
    [giant-bomb.pages.video :as video]
    [giant-bomb.pages.videos :as videos]
    [heroicons.outline :as icons.outline]
    [re-frame.core :as rf]))


(defn- logotype
  []
  (let [theme @(rf/subscribe [:app/theme])
        src   (case theme
                "light" "assets/images/logotype.svg"
                "assets/images/logotype.svg")]
    [:div.flex.flex-1.justify-start.items-center.h-12.p-2.space-x-8
     [:a {:href @(rf/subscribe [:href :page/home])}
      [:span.sr-only "logotype"]
      [:img.h-12.w-36.min-w-36 {:src src :alt "logotype"}]]]))


(def navbar-items
  [{:label "Home" :href :page/home :icon icons.outline/home-icon}
   {:label "Games" :href :page/games :icon icons.outline/chip-icon}
   {:label "Videos" :href :page/videos :icon icons.outline/video-camera-icon}
   {:label "Search" :href :page/search :icon icons.outline/search-icon}
   {:label "Checkout" :href :page/checkout :icon icons.outline/shopping-bag-icon}])


(defn- navbar-links
  []
  (let [route-name @(rf/subscribe [:navigation/route-name])]
    (into [:div.flex.flex-1.items-center.h-12.space-x-8.p-2]
          (for [{:keys [href icon label]} navbar-items]
            (let [active? (= route-name href)]
              [:a.flex.items-center.gap-2.text-base.font-medium.hover:text-gray-900.dark:hover:text-gray-100.h-24
               {:href  @(rf/subscribe [:href href])
                :class (if active? "text-gray-700 dark:text-white" "text-gray-500 dark:text-gray-400")}
               [icon {:class "h-6"}]
               [:span label]])))))


(defn- toggle-theme
  []
  [:button {:type "button" :on-click #(rf/dispatch [:app/toggle-theme])}
   [:span.sr-only "Dark mode"]
   [icons.outline/sparkles-icon {:class "h-6 w-6 text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-white" :aria-hidden "true"}]])


(defn- navbar-controls
  []
  [:div.flex.flex-1.justify-end.items-center.space-x-5
   [:div.flex.space-x-5
    [toggle-theme]]])


(defn- navbar
  []
  [:header.w-screen.mx-auto.px-3.bg-white.dark:bg-gray-800.shadow-md.absolute.top-0.left-0.z-10
   [:div.flex.justify-between.items-center.p-6.space-x-10
    [logotype]
    [navbar-links]
    [navbar-controls]]])


(defn- main
  []
  (let [route-name @(rf/subscribe [:navigation/route-name])
        page       (condp re-matches (str route-name)
                     #":page/home.*" home/page
                     #":page/search.*" search/page
                     #":page/checkout.*" checkout/page
                     #":page/games.*" games/page
                     #":page/game.*" game/page
                     #":page/videos.*" videos/page
                     #":page/video.*" video/page
                     not-found/page)]
    [:main.relative
     [page route-name]]))


(defn page
  "Root page."
  []
  (if-not @(rf/subscribe [:app/initialized?])
    [:div.w-screen.h-screen.flex.justify-center.justify-items-center.content-center.items-center.gap-2
     [components/loading-spinner "Loading..."]]
    [:div.static.h-screen
     [navbar]
     [main]]))
