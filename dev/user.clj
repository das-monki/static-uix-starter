(ns user
  "Development utilities and REPL helpers"
  (:require [clojure.java.io :as io]
            [build :as build]
            [main.core :as server]
            [main.render :as render]))

(def system (atom nil))

(defn stop-server
  "Stop the development server"
  []
  (when-let [server @system]
    (.stop server)
    (reset! system nil)
    (println "Development server stopped")))

(defn start-server
  "Start the development server"
  ([] (start-server 3000))
  ([port]
   (when @system
     (println "Server already running, stopping first...")
     (stop-server))
   (reset! system (server/start-server port))
   (println (str "Development server started on http://localhost:" port))))

(defn restart-server
  "Restart the development server"
  []
  (stop-server)
  (start-server))

(defn generate-static
  "Generate static site files"
  []
  (build/generate-static-site))

(defn render-home
  "Render home page (useful for REPL testing)"
  []
  (render/render-home))

(defn render-about
  "Render about page (useful for REPL testing)"
  []
  (render/render-about))

(defn clean
  "Clean generated files"
  []
  (println "Cleaning generated files...")
  (let [public-dir (io/file "resources/public")]
    (doseq [file [".html" ".js"]]
      (doseq [f (file-seq public-dir)]
        (when (and (.isFile f) (.endsWith (.getName f) file))
          (.delete f)
          (println "Deleted:" (.getPath f))))))
  (println "Clean complete"))

(comment
  ;; REPL workflow examples:

  ;; Start the server
  (start-server)

  ;; Generate static files
  (generate-static)

  ;; Test rendering individual pages
  (render-home)
  (render-about)

  ;; Clean up
  (clean)

  ;; Stop server
  (stop-server))
