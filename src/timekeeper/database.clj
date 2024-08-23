(ns timekeeper.database
  (:require [timekeeper.config :as config]
            [buddy.hashers :refer [encrypt]]
            [monger.core :as mg]
            [monger.collection :as mc])
  (:import org.bson.types.ObjectId))

;; (defn create-mon;; go-uri
  ;; [{:keys [dbname host user password port]}]
  ;; (format "mongodb://%s:%s@%s:%s/%s"
  ;;         user
  ;;         password
  ;;         (or host "127.0.0.1")
  ;;         port
  ;;         dbname))

(defn insert-one [db coll doc]
  (let [oid (ObjectId.)
        doc (merge doc {:_id oid})]
    (-> (mc/insert-and-return db coll doc)
        (update :_id str))))

;; (defn find-one [db coll query]
;;   (let [r (mc/find-one-as-map db coll query)]
;;     (when-not (nil? r)
;;       (update r :_id str))))

(defn find-one [db coll query]
  (when-let [r (mc/find-one-as-map db coll query)]
    (update r :_id str)))

(defn get-user-by-username [db username]
  (find-one db "users" {:username username}))

(defn create-user [db {:keys [username email password] :as document}]
  (when-not (get-user-by-username db username)
    (let [user-data {:username username
                     :email email
                     :password (encrypt password)}
          created-user (insert-one db "users" user-data)]
      (dissoc created-user :password))))

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

