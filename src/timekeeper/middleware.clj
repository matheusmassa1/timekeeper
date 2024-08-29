(ns timekeeper.middleware
  (:require [timekeeper.adapters.auth :refer [auth-backend]]
            [buddy.auth.middleware :refer [wrap-authorization wrap-authentication]]))

(defn wrap-jwt-authentication [handler]
  (wrap-authentication handler auth-backend))

(defn wrap-jwt-authorization [handler]
  (wrap-authorization handler auth-backend))
