(ns app.client.home
  (:require [uix.core :refer [$]]
            [uix.dom :as dom]
            [app.pages.home :as home]))

(defn ^:export init []
  (let [root (.getElementById js/document "root")]
    (dom/hydrate-root root ($ home/home-page))))