(ns timekeeper.domain.user
  (:require [malli.core :as m])
  (:import org.bson.types.ObjectId))

;; Models
(def UserSchema
  [:map
   [:email string?]
   [:username string?]
   [:password string?]])

(def UserLoginSchema
  [:map
   [:username string?]
   [:password string?]])

(defn validate-user-registration [data]
  (when (m/validate UserSchema data)
    data))

(defn validate-user-login [data]
  (when (m/validate UserLoginSchema data)
    data))

;; Services
(defn extract-user-info [data]
  (let [{:keys [username email]} data]
    (cond-> []
      username (conj {:username username})
      email    (conj {:email email}))))

(defn user-exists? [find-fn data]
  (let [user-info (extract-user-info data)
        found-user (find-fn user-info)]
    (when-not (empty? found-user)
      true)))

(defn register-user [find-fn register-fn data]
  (if (validate-user-registration data)
    (let [exists? (user-exists? find-fn data)]
      (when-not exists?
        (let [user (merge data {:_id (ObjectId.)})]
          (register-fn user))))
    nil))
