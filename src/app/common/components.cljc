(ns app.common.components
  (:require [uix.core :as uix :refer [defui $]]))

(defui button [{:keys [on-click children variant]
                :or {variant :primary}}]
  ($ :button
     {:class (str "btn btn-" (name variant))
      :on-click on-click
      :style {:padding "10px 20px"
              :margin "5px"
              :border "none"
              :border-radius "5px"
              :cursor "pointer"
              :font-size "16px"
              :background-color (case variant
                                  :primary "#007bff"
                                  :secondary "#6c757d"
                                  :danger "#dc3545"
                                  "#007bff")
              :color "white"
              :transition "background-color 0.3s"}}
     children))

(defui nav-bar [{:keys [current-page]}]
  ($ :nav
     {:style {:background-color "#333"
              :padding "1rem"
              :margin-bottom "2rem"}}
     ($ :div
        {:style {:max-width "1200px"
                 :margin "0 auto"
                 :display "flex"
                 :justify-content "space-between"
                 :align-items "center"}}
        ($ :h1
           {:style {:color "white"
                    :margin 0
                    :font-size "1.5rem"}}
           "UIx Static Site")
        ($ :div
           {:style {:display "flex"
                    :gap "1rem"}}
           ($ :a
              {:href "/"
               :style {:color (if (= current-page :home) "#fff" "#ccc")
                       :text-decoration "none"
                       :font-weight (if (= current-page :home) "bold" "normal")}}
              "Home")
           ($ :a
              {:href "/about.html"
               :style {:color (if (= current-page :about) "#fff" "#ccc")
                       :text-decoration "none"
                       :font-weight (if (= current-page :about) "bold" "normal")}}
              "About")))))

(defui counter [{:keys [initial-value]
                 :or {initial-value 0}}]
  (let [[count set-count] (uix/use-state initial-value)]
    ($ :div
       {:style {:padding "20px"
                :border "1px solid #ddd"
                :border-radius "8px"
                :text-align "center"
                :max-width "300px"
                :margin "20px auto"}}
       ($ :h3 "Interactive Counter")
       ($ :p
          {:style {:font-size "2rem"
                   :margin "20px 0"}}
          count)
       ($ :div
          ($ button
             {:on-click #(set-count dec)
              :variant :danger}
             "−")
          ($ button
             {:on-click #(set-count inc)
              :variant :primary}
             "+")
          ($ button
             {:on-click #(set-count initial-value)
              :variant :secondary}
             "Reset")))))

(defui layout [{:keys [children page-id]}]
  ($ :div
     {:id "root"
      :style {:min-height "100vh"
              :font-family "-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif"}}
     ($ nav-bar {:current-page page-id})
     ($ :main
        {:style {:max-width "1200px"
                 :margin "0 auto"
                 :padding "0 20px"}}
        children)))