(ns app.pages.about
  (:require [uix.core :refer [defui $]]
            [app.common.components :as c]))

(defui timeline-item [{:keys [year title description]}]
  ($ :div
     {:class "flex mb-8 pl-8 border-l-4 border-blue-500 relative"}
     ($ :div
        {:class "absolute -left-3 w-5 h-5 bg-blue-500 rounded-full border-4 border-white"}
        "")
     ($ :div
        {:class "ml-5"}
        ($ :h4
           {:class "text-blue-500 mb-1 font-semibold"}
           year)
        ($ :h3
           {:class "mb-2 text-lg font-bold"}
           title)
        ($ :p
           {:class "text-gray-600 leading-relaxed"}
           description))))

(defui tech-stack []
  ($ :div
     {:class "bg-gray-50 p-10 rounded-lg my-10"}
     ($ :h3
        {:class "mb-5 text-xl font-bold"}
        "Technology Stack")
     ($ :div
        {:class "grid grid-cols-[repeat(auto-fit,minmax(200px,1fr))] gap-5"}
        (for [tech ["Clojure" "ClojureScript" "UIx (React)" "shadow-cljs" "Ring" "Reitit"]]
          ($ :div
             {:key tech
              :class "bg-white p-4 rounded text-center shadow-sm"}
             tech)))))

(defui about-page []
  ($ c/layout
     {:page-id :about}
     ($ :section
        {:class "py-10"}
        ($ :h1
           {:class "text-center mb-10 text-4xl font-bold"}
           "About This Project")
        
        ($ :div
           {:class "max-w-4xl mx-auto"}
           ($ :p
              {:class "text-xl leading-relaxed mb-8"}
              "This template demonstrates how to build static sites with Clojure and ClojureScript, 
               combining the power of server-side rendering with client-side interactivity.")
           
           ($ tech-stack)
           
           ($ :h2
              {:class "my-10 mb-8 text-3xl font-bold"}
              "Project Timeline")
           
           ($ :div
              ($ timeline-item
                 {:year "2024"
                  :title "Project Inception"
                  :description "Started as a template to showcase UIx and shadow-cljs capabilities for static site generation."})
              ($ timeline-item
                 {:year "Present"
                  :title "Active Development"
                  :description "Continuously improving the developer experience and adding new features."})
              ($ timeline-item
                 {:year "Future"
                  :title "Community Driven"
                  :description "Open for contributions and improvements from the Clojure community."}))
           
           ($ :div
              {:class "text-center mt-15"}
              ($ :h3
                 {:class "text-2xl font-bold mb-4"}
                 "Interactive Demo")
              ($ :p
                 {:class "mb-5"}
                 "This counter component is hydrated on the client side:")
              ($ c/counter {:initial-value 42}))))))
