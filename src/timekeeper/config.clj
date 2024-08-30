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

;; action dispatchers
;; (def prod-actions
;;   {:db/insert (fn [coll doc] (mg/insert coll doc))
;;    :db/find (fn [coll query] (mg/find coll query))
;;    :db/update (fn [coll query update] (mg/update coll query update))
;;    :db/remove (fn [coll query] (mg/remove coll query))})

(defn db-prod-actions [conn]
  {:db/insert (fn [coll doc] (db/insert-one conn coll doc))
   :db/find-one (fn [coll query] (db/find-one conn coll query))})

(comment
 (create-mongo-uri)
 (db-prod-actions {:connection {:obj "mock"}})
  )
