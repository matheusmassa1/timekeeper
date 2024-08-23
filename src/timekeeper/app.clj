(ns timekeeper.app
  (:require [timekeeper.routes :refer [app-routes access-rules]]
            [timekeeper.auth :refer :all]
            [buddy.auth.accessrules :refer [wrap-access-rules]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [timekeeper.middleware :refer [wrap-db]]))

(defn app [context]
  (-> (app-routes context)
      (wrap-access-rules {:rules access-rules :on-error on-error})
      (wrap-jwt-authorization)
      (wrap-jwt-authentication)
      (wrap-keyword-params)
      (wrap-params)
      (wrap-json-response)
      (wrap-json-body {:keywords? true})
      (wrap-defaults api-defaults)))
