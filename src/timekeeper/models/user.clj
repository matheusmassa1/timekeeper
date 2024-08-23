(ns timekeeper.models.user
  (:require [malli.core :as m]))

(def UserRegistrationSchema
  [:map
   [:email string?]
   [:username string?]
   [:password string?]])

(def UserLoginSchema
  [:map
   [:username string?]
   [:password string?]])

(defn validate-user-registration [data]
  (m/validate UserRegistrationSchema data))

(defn validate-user-login [data]
  (m/validate UserLoginSchema data))

;; (defn get-user-by-username [find-fn data])

;; (defn create-user [insert-fn data])
