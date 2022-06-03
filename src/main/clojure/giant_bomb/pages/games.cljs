(ns giant-bomb.pages.games
  (:require
    [giant-bomb.components :as components]
    [giant-bomb.games.core]
    [re-frame.core :as rf]))


(defn game-title
  [{:keys [guid name expected_release_year]}]
  [:a.inline-flex.justify-between.items-center.gap-2
   {:href @(rf/subscribe [:href :page/game {:id guid}])}
   (when (pos? expected_release_year)
     [:div.border.border-white.bg-gray-500.rounded-md.w-10.h-6.flex.justify-center.items-center
      [:span.text-xs.text-white expected_release_year]])
   [:span.absolute.inset-0 {:aria-hidden true}]
   [:span.text-sm.font-medium.text-gray-900 name]])


(defn game-image
  [image]
  [:img.w-full.h-full.object-center.object-cover
   {:src (:super_url image) :alt (:medium_url image)}])


(defn game-rating
  [n]
  (when (pos? n)
    [:div.absolute.top-0.right-0.p-2
     [:div.border-2.border-green-500.bg-gray-900.rounded-full.w-8.h-8.flex.justify-center.items-center
      [:span.text-xs.text-white n]]]))


(defn game-platforms
  [platforms]
  [:div.mt-4.gap-2.px-4.pb-4
   (into [:ul.flex.flex-wrap.gap-2]
         (for [{:keys [abbreviation]} platforms]
           [:li.rounded-sm.bg-gray-200.px-2
            [:p.text-gray-600 {:class "text-[10px]"} abbreviation]]))])


(defn game-card
  [{:as game :keys [image platforms deck number_of_user_reviews]}]
  [:div.group.relative.bg-white.rounded-md.shadow-md
   [:div.w-full.min-h-80.bg-gray-200.aspect-w-1.aspect-h-1.rounded-t-md.overflow-hidden.group-hover:opacity-90.lg:h-80.lg:aspect-none
    [game-image image]
    [game-rating number_of_user_reviews]]
   [:div.mt-4.flex.justify-between.gap-2.px-4.pb-4
    [:div
     [:h3.text-sm.text-gray-700
      [game-title game]]
     [:p.mt-1.text-sm.text-gray-500.text-justify deck]]]
   [game-platforms platforms]])


;; FIXME: [2022-06-03, ilshat@sultanov.team] Add pagination and don't remove previous results from the db
(defn games-cards
  [games]
  [:div.max-w-2xl.mx-auto.lg:max-w-7xl
   (into [:div.grid.grid-cols-1.gap-y-12.gap-x-6.sm:grid-cols-2.lg:grid-cols-4.xl:gap-x-8]
         (for [game games]
           [game-card game]))])


(defn games-list
  []
  (let [readiness @(rf/subscribe [:games/readiness])
        games     @(rf/subscribe [:games])]
    ^{:keys readiness}
    [:div
     [components/loader {:state      readiness
                         :on-loading [components/loading-spinner "Loading..."]
                         :on-failed  [components/failed-spinner "Something went wrong..."]
                         :on-idle    [games-cards games]}]]))


(defn page
  []
  [:div.max-w-7xl.mx-auto.mt-28.p-10
   [games-list]])
