(ns timekeeper.adapters.database
  (:require [timekeeper.config :as config]
            [buddy.hashers :refer [encrypt]]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer [$or]])
  (:import org.bson.types.ObjectId))

(defn insert-one [db coll doc]
  (-> (mc/insert-and-return db coll doc)
      (update :_id str)))

(defn find-one [db coll query]
  (when-let [r (mc/find-one-as-map db coll query)]
    (update r :_id str)))

(defn find-user [db query-vect]
  (mc/find-one db "users" {$or query-vect}))

(defn register-user-adapter [db data]
  (insert-one db "users" data))
