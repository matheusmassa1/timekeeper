(ns timekeeper.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]))

(defn load-config []
  (aero/read-config (io/resource "config.edn")))

(defn get-config [param]
  (get-in (load-config) [param]))

(defn db-config []
  (get-config :db))

(defn memcached-config []
  (let [config (get-config :memcached)]
    (str (config :host) ":" (config :port))))

(defn oauth-config []
  (get-config :oauth))

(defn gapi-scopes []
  (get-in (oauth-config) [:scopes]))

(defn jwt-secret []
  (get-config :jwt-secret))

(defn create-mongo-uri []
  (let [{:keys [dbname host user password port]} (db-config)]
    (format "mongodb://%s:%s@%s:%s/%s"
            user
            password
            (or host "127.0.0.1")
            port
            dbname)))

(comment
 (create-mongo-uri)
  )
