(ns timekeeper.domain.user
  (:require [malli.core :as m]
            [malli.util :as mu]
            [timekeeper.utils :refer [validate-schema]]
            [buddy.hashers :refer [derive]])
  (:import [org.joda.time DateTime]
           [org.joda.time Minutes]
           [org.bson.types ObjectId]))

;; Models
(def UserSchema
  [:map
   {:closed true}
   [:email string?]
   [:username string?]
   [:password string?]])

(def UserLoginSchema
  (mu/dissoc UserSchema :email))

(defn validate-user-registration [data]
  (when (m/validate UserSchema data)
    data))

(defn validate-registration-schema [input]
  (validate-schema UserSchema input))

(defn validate-user-login [data]
  (when (m/validate UserLoginSchema data)
    data))

(defn validate-login-schema [input]
  (validate-schema UserLoginSchema input))

;; Services
;; TODO: move this to a logic ns
(defn extract-user-info [data]
  (let [{:keys [username email]} data]
    (cond-> []
      username (conj {:username username})
      email    (conj {:email email}))))

(defn extract-login-info [data]
  (let [{:keys [username password]} data]
    (cond-> []
      username (conj {:username username})
      password (conj {:password password}))))

(defn is-valid-user? [input auth-data]
  (let [{:keys [username password]} input]
    (some-> auth-data
           (:username)
           (= username)
           (and (= (:password auth-data) password)))))
