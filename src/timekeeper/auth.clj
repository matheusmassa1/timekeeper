(ns timekeeper.auth
  (:require
   [ring.util.response :as resp]
   [buddy.sign.jwt :as jwt]
   [buddy.auth.backends.token :refer [jws-backend]]
   [buddy.auth.accessrules :refer [error]]
   [buddy.auth :refer [authenticated?]]
   [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
   [timekeeper.database :as db]
   [malli.core :as m]
   [java-time.api :as jt]))


(defonce secret "mysecret")

(def auth-data {:username "admin" :password "admin" :roles ["admin"]})

(def auth-backend
  (jws-backend {:secret secret :token-name "Bearer"}))

;; {:username "admin", :password "admin", :roles ["admin" "student"]}

(defn is-valid-user [username password]
  (some-> auth-data
         (:username)
         (= username)
         (and (= (:password auth-data) password))))

(defn create-token [payload secret]
  (let [{:keys [username role]} payload
        exp (jt/plus (jt/instant) (jt/minutes 120))
        claims {:user username :exp exp :role role}
        token (jwt/sign claims secret)]
    token))

(defn wrap-jwt-authentication [handler]
  (wrap-authentication handler auth-backend))

(defn wrap-jwt-authorization [handler]
  (wrap-authorization handler auth-backend))

(defn auth-access [request]
  (if-not (authenticated? request)
    (error "Unauthorized")
    true))

(defn any-access [_]
  true)

(defn on-error [request _]
  {:status 401 :headers {} :body "Unauthorized"})
