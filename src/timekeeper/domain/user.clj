(ns timekeeper.domain.user
  (:require [malli.core :as m])
  (:import [org.joda.time DateTime]
           [org.joda.time Minutes]))

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

(defn addMinutes [now]
  (-> now
      (.plusMinutes 120)))

(defn generate-token-payload [auth-data]
  (let [{:keys [username role]} auth-data
        exp (addMinutes (DateTime.))
        claims {:user username :exp exp :role role}]
    claims))
