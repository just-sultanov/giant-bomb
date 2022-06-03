(ns giant-bomb.pages.search
  (:require
    [giant-bomb.components :as components]
    [giant-bomb.pages.games :as games]
    [giant-bomb.pages.videos :as videos]
    [giant-bomb.search.core]
    [goog.functions :as gf]
    [heroicons.outline :as icons.outline]
    [re-frame.core :as rf]
    [reagent.core :as r :include-macros true]))


(defn search-input
  [query]
  (r/with-let [*query  (r/atom (or query ""))
               search! (gf/debounce #(rf/dispatch [:api/search {:query @*query}]) 500)]
    [:label.relative.block
     [:span.sr-only "Search"]
     [:span.absolute.inset-y-0.left-0.flex.items-center.pl-2
      [icons.outline/search-icon {:class "h-5 w-5 text-slate-300"}]]
     [:input.placeholder:text-slate-400.block.bg-white.w-full.border.border-slate-300.rounded-md.py-2.pl-9.pr-3.shadow-sm.focus:outline-none.focus:border-sky-500.focus:ring-sky-500.focus:ring-1.sm:text-sm
      {:type        "text"
       :name        "search"
       :value       @*query
       :placeholder "Search..."
       :on-change   (fn [event]
                      (let [value (.. event -target -value)]
                        (reset! *query value)
                        (rf/dispatch [:search/set-query value])
                        (search!)))}]]))


(defn search-results
  [results]
  (into [:div]
        (for [[group items] (->> results
                                 (group-by :resource_type)
                                 (sort-by key))]
          [:div
           [:div.flex.items-center.py-4.mt-10.gap-2
            [:div.rounded-sm.bg-gray-300.px-2
             [:span.font-bold.text-gray-800 (count items)]]
            [:h2.text-lg.font-medium.text-gray-800.capitalize (str group "s")]]
           (into ^{:key group} [:div.grid.grid-cols-1.gap-y-12.gap-x-6.sm:grid-cols-2.lg:grid-cols-4.xl:gap-x-8]
                 (for [{:as item :keys [guid]} items]
                   ^{:key [guid]}
                   (case group
                     "game" [games/game-card item]
                     "video" [videos/video-card item]
                     nil)))])))


;; TODO: [2022-06-04, ilshat@sultanov.team] Pagination for search API doesn't work

(defn search
  []
  (let [readiness @(rf/subscribe [:search/readiness])
        query     @(rf/subscribe [:search/query])
        results   @(rf/subscribe [:search/results])]
    ^{:key readiness}
    [:div
     [search-input query]
     [:div.mt-4
      [components/loader {:state      readiness
                          :on-loading [:div.flex.justify-center.justify-items-center.content-center.items-center.gap-2
                                       [components/loading-spinner "Loading..."]]
                          :on-failed  [:div.flex.justify-center.justify-items-center.content-center.items-center.gap-2
                                       [components/failed-spinner "Something went wrong..."]]
                          :on-idle    [search-results results]}]]]))


(defn page
  []
  [:div.max-w-7xl.mx-auto.mt-24.p-10
   [search]])
