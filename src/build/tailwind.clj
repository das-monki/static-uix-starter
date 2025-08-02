(ns build.tailwind
  "Simplified Tailwind CSS integration for shadow-cljs"
  (:require [clojure.java.io :as io]
            [clojure.java.shell :as shell]))

(defn- tailwind-installed? []
  "Check if Tailwind CSS is installed in node_modules"
  (.exists (io/file "node_modules/.bin/tailwindcss")))

(defn- ensure-output-dir []
  "Ensure the CSS output directory exists"
  (let [css-dir (io/file "resources/public/assets/css")]
    (when-not (.exists css-dir)
      (.mkdirs css-dir))))

(defn build-css
  "Build Tailwind CSS for production (minified)"
  []
  (when-not (tailwind-installed?)
    (throw (ex-info "Tailwind CSS not found. Run 'npm install' first." {})))

  (ensure-output-dir)

  (println "Building Tailwind CSS for production...")
  (let [result (shell/sh "npx" "tailwindcss"
                         "-i" "resources/css/input.css"
                         "-o" "resources/public/assets/css/main.css"
                         "--minify")]
    (when-not (zero? (:exit result))
      (throw (ex-info "Tailwind CSS build failed" result)))
    (println "Tailwind CSS build complete")))

(defn watch-css
  "Build Tailwind CSS for development (watch mode)"
  []
  (when-not (tailwind-installed?)
    (throw (ex-info "Tailwind CSS not found. Run 'npm install' first." {})))

  (ensure-output-dir)

  (println "Starting Tailwind CSS in watch mode...")
  (let [result (shell/sh "npx" "tailwindcss"
                         "-i" "resources/css/input.css"
                         "-o" "resources/public/assets/css/main.css"
                         "--watch")]
    (when-not (zero? (:exit result))
      (throw (ex-info "Tailwind CSS watch failed" result)))
    (println "Tailwind CSS watch started")))

;; Shadow-cljs build hooks following teknql pattern

(def ^:private tailwind-watch-process (atom nil))

(defn- start-tailwind-process []
  "Start Tailwind CSS process and return the Process object"
  (let [pb (ProcessBuilder. ["npx" "tailwindcss"
                             "-i" "resources/css/input.css"
                             "-o" "resources/public/assets/css/main.css"
                             "--watch"])
        _ (.inheritIO pb) ; This makes the process inherit IO, showing output
        process (.start pb)]
    process))

(defn start-watch!
  "Start Tailwind CSS in watch mode for development builds"
  {:shadow.build/stage :configure}
  [build-state & args]
  (when-not @tailwind-watch-process
    (try
      (when-not (tailwind-installed?)
        (throw (ex-info "Tailwind CSS not found. Run 'npm install' first." {})))

      (ensure-output-dir)

      (println "Starting Tailwind CSS watch mode...")
      (let [process (start-tailwind-process)]
        (reset! tailwind-watch-process process)
        (println "Tailwind CSS watch mode started - output will appear below"))
      (catch Exception e
        (println "Warning: Failed to start Tailwind CSS watch:" (.getMessage e)))))
  build-state)

(defn compile-release!
  "Compile Tailwind CSS for production builds"
  {:shadow.build/stage :flush}
  [build-state & args]
  (try
    (when-not (tailwind-installed?)
      (throw (ex-info "Tailwind CSS not found. Run 'npm install' first." {})))

    (ensure-output-dir)

    (println "Building Tailwind CSS for production...")
    (let [result (shell/sh "npx" "tailwindcss"
                           "-i" "resources/css/input.css"
                           "-o" "resources/public/assets/css/main.css"
                           "--minify"
                           :env (merge (into {} (System/getenv))
                                      {"NODE_ENV" "production"}))]
      (when-not (zero? (:exit result))
        (println "Warning: Tailwind CSS build failed:" (:err result)))
      (println "Tailwind CSS production build complete"))
    (catch Exception e
      (println "Warning: Tailwind CSS build failed:" (.getMessage e))))
  build-state)
