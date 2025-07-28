(ns app.pages.about
  (:require [uix.core :refer [defui $]]
            [app.common.components :as c]))

(defui timeline-item [{:keys [year title description]}]
  ($ :div
     {:style {:display "flex"
              :margin-bottom "30px"
              :padding-left "30px"
              :border-left "3px solid #007bff"
              :position "relative"}}
     ($ :div
        {:style {:position "absolute"
                 :left "-10px"
                 :width "20px"
                 :height "20px"
                 :background "#007bff"
                 :border-radius "50%"
                 :border "3px solid white"}}
        "")
     ($ :div
        {:style {:margin-left "20px"}}
        ($ :h4
           {:style {:color "#007bff"
                    :margin-bottom "5px"}}
           year)
        ($ :h3
           {:style {:margin-bottom "10px"}}
           title)
        ($ :p
           {:style {:color "#666"
                    :line-height "1.6"}}
           description))))

(defui tech-stack []
  ($ :div
     {:style {:background "#f8f9fa"
              :padding "40px"
              :border-radius "10px"
              :margin "40px 0"}}
     ($ :h3
        {:style {:margin-bottom "20px"}}
        "Technology Stack")
     ($ :div
        {:style {:display "grid"
                 :grid-template-columns "repeat(auto-fit, minmax(200px, 1fr))"
                 :gap "20px"}}
        (for [tech ["Clojure" "ClojureScript" "UIx (React)" "shadow-cljs" "Ring" "Reitit"]]
          ($ :div
             {:key tech
              :style {:background "white"
                      :padding "15px"
                      :border-radius "5px"
                      :text-align "center"
                      :box-shadow "0 2px 4px rgba(0,0,0,0.1)"}}
             tech)))))

(defui about-page []
  ($ c/layout
     {:page-id :about}
     ($ :section
        {:style {:padding "40px 0"}}
        ($ :h1
           {:style {:text-align "center"
                    :margin-bottom "40px"}}
           "About This Project")
        
        ($ :div
           {:style {:max-width "800px"
                    :margin "0 auto"}}
           ($ :p
              {:style {:font-size "1.2rem"
                       :line-height "1.8"
                       :margin-bottom "30px"}}
              "This template demonstrates how to build static sites with Clojure and ClojureScript, 
               combining the power of server-side rendering with client-side interactivity.")
           
           ($ tech-stack)
           
           ($ :h2
              {:style {:margin "40px 0 30px"}}
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
              {:style {:text-align "center"
                       :margin-top "60px"}}
              ($ :h3 "Interactive Demo")
              ($ :p
                 {:style {:margin-bottom "20px"}}
                 "This counter component is hydrated on the client side:")
              ($ c/counter {:initial-value 42}))))))