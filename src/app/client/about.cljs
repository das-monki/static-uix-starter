(ns app.client.about
  (:require [uix.core :refer [$]]
            [uix.dom :as dom]
            [app.pages.about :as about]))

(defn ^:export init []
  (let [root (.getElementById js/document "root")]
    (dom/hydrate-root root ($ about/about-page))))