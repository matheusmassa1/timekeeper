(ns timekeeper.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [timekeeper.adapters.database :as db]))

(defn load-config []
  (aero/read-config (io/resource "config.edn")))

(defn get-config [param]
  (get-in (load-config) [param]))

(defn db-config []
  (get-config :db))

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

(defn db-prod-actions [conn]
  {:db/insert (fn insert [coll doc] (db/insert-one conn coll doc))
   :db/find-one (fn find-one [coll query] (db/find-one conn coll query))})

(comment
 (create-mongo-uri)
 (db-prod-actions {:connection {:obj "mock"}})
  )
