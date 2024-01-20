(ns timekeeper.database
  (:require [timekeeper.config :as config]
            [timekeeper.utils :refer [map-keys-to-underscore map-keys-to-hyphen]]
            [honey.sql :as sql]
            [honey.sql.helpers :as hh]
            [next.jdbc :as jdbc]
            [next.jdbc.date-time]
            [next.jdbc.result-set :as rs]
            [buddy.hashers :refer [encrypt check]]))

(def db (jdbc/get-datasource (config/db-config)))

(defn db-query [sql]
  (jdbc/execute! db sql
                 {:return-keys true
                  :builder-fn rs/as-unqualified-maps}))

(defn db-query-one [sql]
  (jdbc/execute-one! db sql
                 {:return-keys true
                  :builder-fn rs/as-unqualified-maps}))

(defn create-user 
  [{:keys [email username password]}]
  (let [hashed-password (encrypt password)
        created-user (->
                       (hh/insert-into :users)
                       (hh/columns :email :username :password)
                       (hh/values [[email username hashed-password]])
                       sql/format
                       db-query-one)
        sanitized-user (dissoc created-user :password)]
    sanitized-user))
   
(defn get-user [{:keys [username password]}]
   (let [user (-> (hh/select :*)
                  (hh/from :users)
                  (hh/where := :username username)
                  sql/format
                  db-query-one)
         sanitized-user (dissoc user :password)]
    (if (and user (check password (:password user)))
      sanitized-user
      nil))) 
     
   
(defn save-oauth-credentials
  [{:keys [user-id access-token scope expires-in expires-at]}]
  (let [saved-token (->
                      ()
                      (hh/insert-into :oauth-credentials)
                      (hh/columns :user-id :access-token :scope :expires-in :expires-at) 
                      (hh/values [[user-id access-token scope expires-in expires-at]])
                      sql/format
                      db-query-one)]
    saved-token))

