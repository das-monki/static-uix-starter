(ns app.pages.home
  (:require [uix.core :refer [defui $]]
            [app.common.components :as c]))

(defui hero-section []
  ($ :section
     {:class "text-center py-20 px-5 text-white rounded-lg mb-10"
      :style {:background "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"}}
     ($ :h1
        {:class "text-5xl mb-5"}
        "Welcome to UIx Static Sites")
     ($ :p
        {:class "text-xl max-w-2xl mx-auto mb-8"}
        "Build blazing fast static sites with Clojure, shadow-cljs, and UIx. Server-side rendering with client-side hydration.")
     ($ c/button
        {:variant :primary
         :on-click #?(:cljs #(js/alert "Ready to build something amazing!")
                      :clj nil)}
        "Get Started")))

(defui feature-card [{:keys [title description icon]}]
  ($ :div
     {:class "p-8 border border-gray-300 rounded-lg text-center transition-transform duration-300 cursor-pointer hover:-translate-y-1 hover:shadow-lg"}
     ($ :div
        {:class "text-5xl mb-5"}
        icon)
     ($ :h3
        {:class "mb-4 text-lg font-semibold"}
        title)
     ($ :p
        {:class "text-gray-600 leading-relaxed"}
        description)))

(defui features-section []
  ($ :section
     {:class "my-13"}
     ($ :h2
        {:class "text-center mb-10 text-4xl font-bold"}
        "Features")
     ($ :div
        {:class "grid grid-cols-[repeat(auto-fit,minmax(300px,1fr))] gap-8"}
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
        {:class "my-15 text-center"}
        ($ :h2
           {:class "mb-8 text-3xl font-bold"}
           "Try the Interactive Counter")
        ($ c/counter {:initial-value 0}))))
