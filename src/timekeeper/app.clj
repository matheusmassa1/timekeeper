(ns timekeeper.app
  (:require [timekeeper.api.routes :refer [app-routes]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :as rmp]
            [ring.middleware.keyword-params :as rmkp]
            [ring.middleware.json :as rmj]))
   

(defn create-app [app-routes]
  (-> app-routes
      (rmp/wrap-params {:encoding "UTF-8"})
      (rmj/wrap-json-body)
      (rmj/wrap-json-response)))

(comment
  (def server (run-jetty (#'create-app #'app-routes) {:join? false :port 3001})) ;; pass app-routes as function not value https://clojure.org/guides/weird_characters#_var_quote
  (.stop server)
  ,,,)
