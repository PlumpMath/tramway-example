(ns user
  (:require (eu.cassiel [tramway :as app])
            (eu.cassiel.tramway [auto :as a])
            (eu.cassiel.tramway-example.forms [simple-square :as simple-square])
            [quil.core :as q]
            [clojure.core.async :as async]
            (clojure.tools.namespace [repl :as rr])))

(def system nil)

(defn init
  "Constructs the current development system."
  []
  (alter-var-root #'system
                  (constantly (app/create-app [simple-square/simple-square]
                                              :frame-rate 30))))

(defn start
  "Starts the current development system."
  []
  (alter-var-root #'system (fn [s] (app/start s
                                             :size [480 480]
                                             :renderer :p3d
                                             :init #(do (q/color-mode :rgb 255)
                                                        (q/ortho))))))

(defn stop
  "Shuts down and destroys the current development system."
  []
  (alter-var-root #'system
    (fn [s] (when s (app/stop s)))))

(defn go
  "Initializes the current development system and starts it running."
  []
  (init)
  (start))

(defn reset []
  (stop)
  (rr/refresh :after 'user/go))
