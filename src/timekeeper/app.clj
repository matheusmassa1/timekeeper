(ns timekeeper.app
  (:require [timekeeper.api.routes :refer [app-routes]]
            [timekeeper.config :refer [memcached-config]]
            [timekeeper.api.auth :refer [auth-backend wrap-jwt-authentication]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as rmp]
            [ring.middleware.keyword-params :as rmkp]
            [ring.middleware.json :as rmj]
            [ring.middleware.session :as rms]
            [ring.middleware.session.memcached :refer [mem-store]]))

(defn create-app [app-routes]
  (-> app-routes
      (wrap-jwt-authentication)
      ;; (rmkp/wrap-keyword-params)
      ;; (rmp/wrap-params)
      (rmj/wrap-json-response)
      (rmj/wrap-json-body {:keywords? true})))

(comment
  (def server (run-jetty (#'create-app #'app-routes) {:join? false :port 3001}))
  (.stop server)
  ,,,)
