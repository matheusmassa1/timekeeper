(ns timekeeper.auth
  (:require
   [ring.util.response :as resp]
   [buddy.sign.jwt :as jwt]
   [buddy.auth.backends.token :refer [jws-backend]]
   [buddy.auth.accessrules :refer [error]]
   [buddy.auth :refer [authenticated?]]
   [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
   [timekeeper.adapters.database :as db]
   [malli.core :as m])
  (:import [org.joda.time DateTime]
           [org.joda.time Minutes]))


(defonce secret "mysecret")

(def auth-data {:username "admin" :password "admin" :roles ["admin"]})

(def auth-backend
  (jws-backend {:secret secret :token-name "Bearer"}))

(defn is-valid-user [username password]
  (some-> auth-data
         (:username)
         (= username)
         (and (= (:password auth-data) password))))

(defn addMinutesToNow []
  (let [now (DateTime/now)]
    (-> now
        (.plusMinutes 120))))

(defn create-token [payload secret]
  (let [{:keys [username role]} payload
        exp (addMinutesToNow)
        claims {:user username :exp exp :role role}
        token (jwt/sign claims secret)]
    token))

(defn auth-access [request]
  (if-not (authenticated? request)
    (error "Unauthorized")
    true))

(defn any-access [_]
  true)

(defn on-error [request _]
  {:status 401 :headers {} :body "Unauthorized"})
