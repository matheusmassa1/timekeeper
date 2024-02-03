(ns timekeeper.app
  (:require [timekeeper.api.routes :refer [app-routes]]
            [timekeeper.config :refer [memcached-config]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as rmp]
            [ring.middleware.keyword-params :as rmkp]
            [ring.middleware.json :as rmj]
            [ring.middleware.session :as rms]
            [ring.middleware.session.memcached :refer [mem-store]]))

(defn create-app [app-routes]
  (-> app-routes
      (rmkp/wrap-keyword-params)
      (rmp/wrap-params)
      (rmj/wrap-json-body {:keywords? true})
      (rmj/wrap-json-response)
      (rms/wrap-session {:store (mem-store (memcached-config))})))

(comment
  (def server (run-jetty (#'create-app #'app-routes) {:join? false :port 3000})) ;; pass app-routes as function not value https://clojure.org/guides/weird_characters#_var_quote
  (.stop server)
  ,,,)
