(ns main.render
  (:require [uix.core :refer [$]]
            [uix.dom.server :as dom.server]
            [app.pages.home :as home]
            [app.pages.about :as about]))

(defn html-template
  [{:keys [title content script-src meta-description]
    :or {title "UIx Static Site"
         meta-description "A static site built with Clojure, shadow-cljs and UIx"}}]
  (str "<!DOCTYPE html>\n"
       "<html lang=\"en\">\n"
       "<head>\n"
       "  <meta charset=\"UTF-8\">\n"
       "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
       "  <meta name=\"description\" content=\"" meta-description "\">\n"
       "  <title>" title "</title>\n"
       "  <link rel=\"stylesheet\" href=\"/assets/css/main.css\">\n"
       "</head>\n"
       "<body>\n"
       "  <div id=\"root\">" content "</div>\n"
       (when script-src
         (str "\n  <script src=\"/assets/js/shared.js\"></script>\n"
              "  <script src=\"" script-src "\"></script>\n"))
       "</body>\n"
       "</html>"))

(defn render-page [page-component {:keys [title script-src meta-description]}]
  (let [content (dom.server/render-to-string ($ page-component))]
    (html-template {:title title
                    :content content
                    :script-src script-src
                    :meta-description meta-description})))

(defn render-home []
  (render-page home/home-page
               {:title "Home - UIx Static Site"
                :script-src "/assets/js/home.js"
                :meta-description "Welcome to UIx Static Sites - Build blazing fast static sites with Clojure"}))

(defn render-about []
  (render-page about/about-page
               {:title "About - UIx Static Site"
                :script-src "/assets/js/about.js"
                :meta-description "Learn about UIx Static Sites and the technology stack"}))
