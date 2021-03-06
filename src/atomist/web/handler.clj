(ns atomist.web.handler
  (:require [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [compojure.route :as route]
            [reload])
  (:import [java.security KeyStore]
           [java.io ByteArrayOutputStream FileInputStream]
           [java.util Base64])
  (:gen-class))

(defn local-update [] "go26")

(defroutes app
  (GET "/health" _ (constantly {:status 200 :body "ok"}))
  (->
   (GET "/" _ (constantly {:status 200 :body {:app "gcr-clj-web" :version 56 :local-change (local-update)}}))
   (wrap-json-body {:keywords? true :bigdecimals? true})  
   (wrap-json-response))
  (route/not-found "<h1>not found</h1>"))

(defn -main
  [& args]
  (try
    (println "... run-jetty")
    (run-jetty #'app {:port 3000})
    (catch Throwable t
      (println "failed to start " t))))

