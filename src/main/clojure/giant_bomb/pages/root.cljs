(ns giant-bomb.pages.root
  (:require
    [giant-bomb.components :as components]
    [giant-bomb.pages.checkout :as checkout]
    [giant-bomb.pages.games :as games]
    [giant-bomb.pages.home :as home]
    [giant-bomb.pages.not-found :as not-found]
    [giant-bomb.pages.search :as search]
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
  [:div.max-w-8xl.mx-auto.px-3.bg-white.dark:bg-gray-800.shadow-md
   [:div.flex.justify-between.items-center.p-6.space-x-10
    [logotype]
    [navbar-links]
    [navbar-controls]]])


(defn- main
  []
  (let [route-name @(rf/subscribe [:navigation/route-name])
        page       (condp re-matches (str route-name)
                     #":page/home.*" home/page
                     #":page/games.*" games/page
                     #":page/checkout.*" checkout/page
                     #":page/search.*" search/page
                     not-found/page)]
    [:main.max-w-7xl.mx-auto.p-10
     [page route-name]]))


(defn- build-info
  []
  (when-some [info @(rf/subscribe [:app/build-info])]
    [:div.absolute.bottom-4.right-4
     [:a.text-violet-600.hover:text-violet-800.dark:text-violet-300.dark:hover:text-violet-400 {:href (:homepage info), :target "_blank"}
      [:span.text-sm (:version info)]]]))


(defn page
  "Root page."
  []
  (if-not @(rf/subscribe [:app/initialized?])
    [:div.w-screen.h-screen.flex.justify-center.justify-items-center.content-center.items-center.gap-2
     [components/loading-spinner "Loading..."]]
    [:div.relative.h-screen
     [navbar]
     [main]
     [build-info]]))
