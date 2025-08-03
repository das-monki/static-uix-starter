(ns main.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.file :refer [wrap-file]]
            [hawk.core :as hawk]
            [clojure.java.io :as io]
            [main.render :as render]))

(defn ensure-directory [path]
  (let [dir (io/file path)]
    (when-not (.exists dir)
      (.mkdirs dir))))

(defn write-file [path content]
  (ensure-directory (.getParent (io/file path)))
  (spit path content))

(defn regenerate-files []
  (println "Regenerating HTML files...")
  (require '[main.render :as render] :reload-all)
  (require '[app.pages.home :as home] :reload-all)
  (require '[app.pages.about :as about] :reload-all)
  (require '[app.common.components :as c] :reload-all)

  ;; Generate home page
  (println "  Generating index.html...")
  (write-file "resources/public/index.html" (render/render-home))

  ;; Generate about page
  (println "  Generating about.html...")
  (write-file "resources/public/about.html" (render/render-about))

  (println "Static files regenerated!"))

(defn start-server
  ([] (start-server 3000))
  ([port]
   (println (str "Starting static file server on http://localhost:" port))

   ;; Generate initial files
   (regenerate-files)

   ;; Watch for changes and regenerate
   (println "Watching for file changes in src/app...")
   (hawk/watch! [{:paths ["src/app"]
                  :filter hawk/file?
                  :handler (fn [ctx e]
                             (println "File changed:" (:file e))
                             (regenerate-files)
                             ctx)}])

   ;; Simple static file server
   (jetty/run-jetty
    (-> (fn [_] {:status 404 :body "Not found"})  ; Base handler for non-file requests
        (wrap-file "resources/public")
        wrap-content-type)
    {:port port
     :join? false})))

(defn -main [& args]
  (let [port (if (seq args)
               (Integer/parseInt (first args))
               3000)]
    (start-server port)))
