(ns build
  (:require [clojure.java.io :as io]
            [main.render :as render]))

(defn ensure-directory [path]
  (let [dir (io/file path)]
    (when-not (.exists dir)
      (.mkdirs dir))))

(defn write-file [path content]
  (ensure-directory (.getParent (io/file path)))
  (spit path content))

(defn generate-static-site []
  (println "Generating static site...")

  ;; Ensure output directory exists
  (ensure-directory "resources/public")

  ;; Generate home page
  (println "  Generating index.html...")
  (write-file "resources/public/index.html" (render/render-home))

  ;; Generate about page
  (println "  Generating about.html...")
  (write-file "resources/public/about.html" (render/render-about))

  (println "Static site generation complete!")
  (println "Files written to resources/public/"))

(defn -main [& _args]
  (generate-static-site))
