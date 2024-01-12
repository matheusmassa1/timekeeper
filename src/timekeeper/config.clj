(ns timekeeper.config
  (:require [aero.core :as aero]))

(defn load-config []
  (aero/read-config "resources/config.edn"))

(defn get-config [param]
  (get-in (load-config) [param]))

(defn db-config []
  (get-config :db))

(defn oauth-config []
  (get-config :oauth))

(defn gapi-scopes []
  (get-in (oauth-config) [:scopes]))


(comment
 (load-config)
 (get-gapi-scopes)
 (get-config :db)
 (with-oauth-config)
 ,) 
