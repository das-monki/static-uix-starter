(ns main.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.util.response :as response]
            [reitit.ring :as ring]
            [main.render :as render]))

(def routes
  [["/" {:get {:handler (fn [_]
                          {:status 200
                           :headers {"Content-Type" "text/html"}
                           :body (render/render-home)})}}]
   ["/about" {:get {:handler (fn [_]
                               {:status 200
                                :headers {"Content-Type" "text/html"}
                                :body (render/render-about)})}}]
   ["/about.html" {:get {:handler (fn [_]
                                    {:status 200
                                     :headers {"Content-Type" "text/html"}
                                     :body (render/render-about)})}}]])

(def handler
  (ring/ring-handler
   (ring/router routes)
   (ring/routes
    (ring/create-resource-handler {:path "/" :root "public"})
    (ring/create-default-handler
     {:not-found (fn [_]
                   {:status 404
                    :headers {"Content-Type" "text/html"}
                    :body "<h1>404 - Page Not Found</h1>"})}))))

(defn start-server
  ([] (start-server 3000))
  ([port]
   (println (str "Starting server on http://localhost:" port))
   (jetty/run-jetty 
    (-> handler
        (wrap-resource "public")
        wrap-content-type)
    {:port port
     :join? false})))

(defn -main [& args]
  (let [port (if (seq args)
               (Integer/parseInt (first args))
               3000)]
    (start-server port)))