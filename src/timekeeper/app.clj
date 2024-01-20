(ns timekeeper.app
  (:require [timekeeper.api.routes :refer [app-routes]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.nested-params :as rmnp]
            [ring.middleware.params :as rmp]
            [ring.middleware.keyword-params :as rmkp]
            [ring.middleware.json :as rmj]
            [ring.middleware.session :as rms]))
   

(defn create-app [app-routes]
  (-> app-routes
      (rms/wrap-session)
      (rmkp/wrap-keyword-params)
      (rmp/wrap-params)
      (rmj/wrap-json-body {:keywords? true})
      (rmj/wrap-json-response {:keywords? true})))

(comment
  (def server (run-jetty (#'create-app #'app-routes) {:join? false :port 3001})) ;; pass app-routes as function not value https://clojure.org/guides/weird_characters#_var_quote
  (.stop server)
  ,,,)
