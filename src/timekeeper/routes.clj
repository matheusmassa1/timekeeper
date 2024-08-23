(ns timekeeper.routes
  (:require [timekeeper.handlers :as handlers]
            [timekeeper.auth :refer [auth-access any-access]]
            [ring.util.response :as resp]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.core :refer [routes GET POST]]
            [compojure.route :as route]))

(def access-rules [{:pattern #"^/login" :handler any-access}
                   {:pattern #"/healthCheck" :handler any-access}
                   {:pattern #"/requestInfo" :handler any-access}])

(defn app-routes [components]
  (routes
    (GET "/healthCheck" [] handlers/ping)
    (POST "/login" req (handlers/login req))
    (POST "/register" req (handlers/register req))
    (GET "/requestInfo" [] handle-dump)
    (route/not-found (resp/response {:error "Route not found"}))))
