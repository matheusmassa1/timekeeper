(ns timekeeper.config
  (:require [aero.core :as aero]))

(defn load-config []
  (aero/read-config "resources/config.edn"))

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

(comment 
  ,,,)
