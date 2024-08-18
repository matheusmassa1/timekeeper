(ns timekeeper.app
  (:require [timekeeper.api.routes :refer [app-routes access-rules]]
            ;; [timekeeper.config :refer [memcached-config]]
            [timekeeper.api.auth :refer [wrap-jwt-authentication wrap-jwt-authorization on-error]]
            [buddy.auth.accessrules :refer [wrap-access-rules]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]))
            ;; [ring.middleware.session :as rms]
            ;; [ring.middleware.session.memcached :refer [mem-store]]))

(defn create-app [app-routes]
  (-> app-routes
      (wrap-access-rules {:rules access-rules :on-error on-error})
      (wrap-jwt-authorization)
      (wrap-jwt-authentication)
      (wrap-keyword-params)
      (wrap-params)
      (wrap-json-response)
      (wrap-json-body {:keywords? true})))

(comment
  (def server (run-jetty (#'create-app #'app-routes) {:join? false :port 3001}))
  (.stop server)
  ,,,)
