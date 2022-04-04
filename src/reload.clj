(ns reload 
  (:require [hawk.core :as hawk]))

(def watcher (atom nil))

(defn- clojure-file? [_ {:keys [file]}]
  (re-matches #"[^.].*(\.clj|\.edn)$" (.getName file)))

(defn- auto-reset-handler [ctx {:keys [kind file]}]
  (try
    (condp = kind
      :modify (do 
                (printf "loading %s\n" file)
                (println "loading " (load-file (.getPath file))))
      (printf "skip kind %s (%s)\n" kind file))
    (catch Throwable t
      (printf "problem reloading %s - %s\n" file t)))
  ctx)

(defn auto-reset
  "Automatically reset the system when a Clojure or edn file is changed in
  `src` or `resources`."
  []
  (let [watcher-ref
        (hawk/watch! [{:paths ["src/"]
                       :filter clojure-file?
                       :handler auto-reset-handler}])]
    (swap! watcher (constantly watcher-ref))))

(auto-reset)
