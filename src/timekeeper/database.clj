(ns timekeeper.database
  (:require [timekeeper.config :as config]
            [timekeeper.utils :as utils]
            [monger.core :as mg]
            [monger.collection :as mc]
            [buddy.hashers :refer [encrypt check]]))

(defn create-mongo-uri
  [{:keys [dbname host user password port]}]
  (format "mongodb://%s:%s@%s:%s/%s"
          user
          password
          (or host "127.0.0.1")
          port
          dbname))

(defn connect-to-mongo []
  (let [uri (create-mongo-uri (config/db-config))
        {:keys [conn db]} (mg/connect-via-uri uri)]
  {:conn conn :db db}))

(defn insert-document [db]
  (let [coll "events"
        document {:name "John Dxe" :age 30 :city "New York"}]
    (mc/insert db coll document)))

(let [{:keys [conn db]} (connect-to-mongo)]
  (insert-document db)
  (mg/disconnect conn))

;; (def db (jdbc/get-datasource (config/db-config)))

;; (defn db-query [sql]
;;   (jdbc/execute! db sql
;;                  {:return-keys true
;;                   :builder-fn rs/as-unqualified-maps}))

;; (defn db-query-one [sql]
;;   (jdbc/execute-one! db sql
;;                  {:return-keys true
;;                   :builder-fn rs/as-unqualified-maps}))

;; (defn create-user
;;   [{:keys [email username password]}]
;;   (let [hashed-password (encrypt password)
;;         created-user (->
;;                        (hh/insert-into :users)
;;                        (hh/columns :email :username :password)
;;                        (hh/values [[email username hashed-password]])
;;                        sql/format
;;                        db-query-one
;;                       (utils/reset-metadata))
;;         sanitized-user (dissoc created-user :password)]
;;     sanitized-user))

;; (defn get-user [{:keys [username password]}]
;;    (let [user (-> (hh/select :*)
;;                   (hh/from :users)
;;                   (hh/where := :username username)
;;                   sql/format
;;                   db-query-one
;;                   (utils/reset-metadata))
;;          sanitized-user (dissoc user :password)]
;;     (if (and user (check password (:password user)))
;;       sanitized-user
;;       nil)))


;; (defn save-oauth-credentials
;;   [{:keys [user-id access-token expires-in scope expires-at]}]
;;   (let [saved-token (->
;;                         (hh/insert-into :oauth-credentials)
;;                         (hh/columns :user-id :access-token :expires-in :scope :expires-at)
;;                         (hh/values [[user-id access-token expires-in scope expires-at]])
;;                         sql/format
;;                         db-query-one)]
;;     saved-token))

;; (defn get-last-oauth-token-by-user-id [user-id]
;;   (let [data (-> (hh/select :*)
;;               (hh/from :oauth-credentials)
;;               (hh/where := :user-id user-id)
;;               (hh/order-by [:created-at :desc])
;;               (sql/format)
;;               (db-query-one))]
;;     data))


(comment 
  ,,,)
