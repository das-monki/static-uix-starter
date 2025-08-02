(ns app.common.components
  (:require [uix.core :as uix :refer [defui $]]))

(defui button [{:keys [on-click children variant]
                :or {variant :primary}}]
  ($ :button
     {:class (str "btn btn-" (name variant))
      :on-click on-click}
     children))

(defui nav-bar [{:keys [current-page]}]
  ($ :nav
     {:class "bg-gray-700 p-4 mb-8"}
     ($ :div
        {:class "max-w-6xl mx-auto flex justify-between items-center"}
        ($ :h1
           {:class "text-white text-2xl font-bold"}
           "UIx Static Site")
        ($ :div
           {:class "flex gap-4"}
           ($ :a
              {:href "/"
               :class (str "no-underline "
                          (if (= current-page :home) 
                            "text-white font-bold" 
                            "text-gray-300 hover:text-white"))}
              "Home")
           ($ :a
              {:href "/about.html"
               :class (str "no-underline "
                          (if (= current-page :about) 
                            "text-white font-bold" 
                            "text-gray-300 hover:text-white"))}
              "About")))))

(defui counter [{:keys [initial-value]
                 :or {initial-value 0}}]
  (let [[count set-count] (uix/use-state initial-value)]
    ($ :div
       {:class "p-5 border border-gray-300 rounded-lg text-center max-w-sm mx-auto my-5"}
       ($ :h3
          {:class "text-lg font-semibold mb-4"}
          "Interactive Counter")
       ($ :p
          {:class "text-4xl font-bold my-5"}
          count)
       ($ :div
          {:class "flex justify-center gap-2"}
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
     {:class "min-h-screen font-sans"}
     ($ nav-bar {:current-page page-id})
     ($ :main
        {:class "max-w-6xl mx-auto px-5"}
        children)))
