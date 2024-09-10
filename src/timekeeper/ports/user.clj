(ns timekeeper.ports.user
  (:require [timekeeper.domain.user :refer [extract-user-info validate-user-registration]]
            [buddy.hashers :refer [derive]])
  (:import [org.bson.types ObjectId]
           [org.joda.time DateTime]
           [org.joda.time DateTimeZone]))

(defn user-exists? [find-fn data]
  (let [user-info (extract-user-info data)
        found-user (find-fn user-info)]
    (when-not (empty? found-user)
      true)))

(defn register-user [find-fn register-fn data]
  (if (validate-user-registration data)
    (let [exists? (user-exists? find-fn data)]
      (when-not exists?
        (let [created-at (DateTime.)
              hashed-password (derive (:password data))
              user (-> data
                         (dissoc :password)
                         (assoc :_id (ObjectId.)
                                :password hashed-password
                                :created-at created-at))]
          (register-fn user))))
    nil))


(defn register-user! [dispatcher data]
  (let [find-user (:db/find-one dispatcher)
        register-user (:db/insert dispatcher)]
    (register-user (find-user "user" data) (register-user "users" data))))
