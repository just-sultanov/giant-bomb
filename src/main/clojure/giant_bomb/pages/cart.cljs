(ns giant-bomb.pages.cart
  (:require
    [giant-bomb.cart.core]
    [giant-bomb.helpers.string :as str]
    [re-frame.core :as rf]))


;; FIXME: [2022-06-04, ilshat@sultanov.team] Known issue: You cannot add games from the different pages
;; The list of games/videos is always overwritten when new data is received
;; We need to add caching and normal pagination to improve performance and fix this issue

(defn cart-item
  [guid]
  (let [{:keys [name image deck]} @(rf/subscribe [:cart/game guid])]
    [:li.flex.py-6
     [:div.h-24.w-24.flex-shrink-0.overflow-hidden.rounded-md.border.border-gray-200
      [:div.aspect-w-3.aspect-h-4.rounded-lg.overflow-hidden.lg:block
       [:img.w-full.h-full.object-center.object-cover {:src (:super_url image) :alt "Game image"}]]]
     [:div.ml-4.flex.flex-1.flex-col
      [:div
       [:div.flex.justify-between.text-base.font-medium.text-gray-900
        [:h3 [:a {:href @(rf/subscribe [:href :page/game {:id guid}])} name]]]
       [:p.mt-1.text-sm.text-wrap.text-gray-500 deck]]
      [:div.flex.flex-1.items-end.justify-between.text-sm
       [:div.flex
        [:button.font-medium.text-indigo-600.hover:text-indigo-500
         {:type     "button"
          :on-click #(when (js/confirm "Are you sure?")
                       (rf/dispatch [:cart/remove-item guid]))}
         "Remove"]]]]]))


(defn cart-items
  [cart]
  (let [n (count cart)]
    [:div.relative.z-10
     [:div.pointer-events-auto
      [:div.flex.h-full.flex-col.overflow-y-scroll.bg-white.rounded-md.shadow-md
       [:div.flex-1.overflow-y-auto.py-6.px-4.sm:px-6
        [:div.flex.items-start.justify-between
         [:h2.text-lg.font-medium.text-gray-900 "Shopping cart"]]
        [:div.mt-6
         [:div.flow-root
          (if-not (seq cart)
            [:p.text-sm.text-wrap.text-gray-500 "Your shopping cart is empty"]
            (into [:ul.-my-6.divide-y.divide-gray-200 {:role "list"}
                   (for [guid cart]
                     ^{:key guid}
                     [cart-item guid])]))]]]
       [:div.border-t.border-gray-200.py-6.px-4.sm:px-6
        (if-not (seq cart)
          [:div.flex.justify-center.text-center.text-md.text-gray-500.gap-2
           [:button.font-medium.text-indigo-600.hover:text-indigo-500
            {:type     "button"
             :on-click #(rf/dispatch [:navigation/redirect {:route-name :page/games}])}
            "Explore games"]]
          [:div
           [:div.mt-6
            [:button.w-full.flex.items-center.justify-center.rounded-md.border.border-transparent.bg-indigo-600.px-6.py-3.text-base.font-medium.text-white.shadow-sm.hover:bg-indigo-700
             {:type     "button"
              :on-click #(when (js/confirm "Are you sure?")
                           (rf/dispatch [:cart/clear])
                           (js/alert (str/format "You bought %s game%s. Thank you very much!" n (if (< n 2) "" "s"))))}
             "Confirm order"]]
           [:div.mt-6.flex.justify-center.text-center.text-sm.text-gray-500.gap-2
            [:span "or"]
            [:button.font-medium.text-indigo-600.hover:text-indigo-500
             {:type     "button"
              :on-click #(rf/dispatch [:navigation/redirect {:route-name :page/games}])}
             "Continue Shopping"]]])]]]]))


(defn page
  []
  (let [cart @(rf/subscribe [:cart])]
    [:div.max-w-7xl.mx-auto.mt-24.p-10
     [cart-items cart]]))
