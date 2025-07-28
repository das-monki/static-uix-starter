(ns app.pages.home
  (:require [uix.core :refer [defui $]]
            [app.common.components :as c]))

(defui hero-section []
  ($ :section
     {:style {:text-align "center"
              :padding "80px 20px"
              :background "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"
              :color "white"
              :border-radius "10px"
              :margin-bottom "40px"}}
     ($ :h1
        {:style {:font-size "3rem"
                 :margin-bottom "20px"}}
        "Welcome to UIx Static Sites")
     ($ :p
        {:style {:font-size "1.3rem"
                 :max-width "600px"
                 :margin "0 auto 30px"}}
        "Build blazing fast static sites with Clojure, shadow-cljs, and UIx. Server-side rendering with client-side hydration.")
     ($ c/button
        {:variant :primary
         :on-click #?(:cljs #(js/alert "Ready to build something amazing!")
                      :clj nil)}
        "Get Started")))

(defui feature-card [{:keys [title description icon]}]
  ($ :div
     {:style {:padding "30px"
              :border "1px solid #e0e0e0"
              :border-radius "8px"
              :text-align "center"
              :transition "transform 0.3s, box-shadow 0.3s"
              :cursor "pointer"}
      :on-mouse-enter #?(:cljs #(set! (.-transform (.-style ^js (.-currentTarget %))) "translateY(-5px)")
                         :clj nil)
      :on-mouse-leave #?(:cljs #(set! (.-transform (.-style ^js (.-currentTarget %))) "translateY(0)")
                         :clj nil)}
     ($ :div
        {:style {:font-size "3rem"
                 :margin-bottom "20px"}}
        icon)
     ($ :h3
        {:style {:margin-bottom "15px"}}
        title)
     ($ :p
        {:style {:color "#666"
                 :line-height "1.6"}}
        description)))

(defui features-section []
  ($ :section
     {:style {:margin "60px 0"}}
     ($ :h2
        {:style {:text-align "center"
                 :margin-bottom "40px"
                 :font-size "2.5rem"}}
        "Features")
     ($ :div
        {:style {:display "grid"
                 :grid-template-columns "repeat(auto-fit, minmax(300px, 1fr))"
                 :gap "30px"}}
        ($ feature-card
           {:icon "⚡"
            :title "Lightning Fast"
            :description "Pre-rendered static pages with instant hydration for dynamic functionality."})
        ($ feature-card
           {:icon "🔧"
            :title "Developer Friendly"
            :description "Hot reloading in development, optimized builds for production."})
        ($ feature-card
           {:icon "🎯"
            :title "SEO Optimized"
            :description "Server-side rendering ensures your content is indexed by search engines."}))))

(defui home-page []
  ($ c/layout
     {:page-id :home}
     ($ hero-section)
     ($ features-section)
     ($ :section
        {:style {:margin "60px 0"
                 :text-align "center"}}
        ($ :h2
           {:style {:margin-bottom "30px"}}
           "Try the Interactive Counter")
        ($ c/counter {:initial-value 0}))))