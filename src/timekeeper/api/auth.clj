(ns timekeeper.api.auth
  (:require [timekeeper.config :refer [jwt-secret]]
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.backends :as backends]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [buddy.sign.jwt :as jwt]
            [ring.util.response :as resp]))

(defn backend []
  (let [secret (jwt-secret)]
    (backends/jws {:secret secret})))

(defn wrap-jwt-authentication [handler]
  (wrap-authentication handler backend))

(defn auth-middleware [handler]
  (fn [request]
    (if (authenticated? request)
      (handler request)
      (resp/response {:status 401 :body {:error "Unauthorized"}}))))

(defn create-token [payload]
  (jwt/sign payload (jwt-secret)))

(comment 
  (backend)
  ,,,)

