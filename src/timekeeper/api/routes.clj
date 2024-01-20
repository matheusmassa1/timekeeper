(ns timekeeper.api.routes
  (:require [timekeeper.api.handlers :as handlers]
            [ring.util.response :as resp]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/healthcheck" [] (handlers/ping))
  (POST "/register" params (handlers/register params))
  (POST "/login" params (handlers/login params))
  (GET "/request-info" [] handle-dump)
  (GET "/gapi-auth" [] (handlers/get-oauth-code-handler))
  (GET "/oauth-callback" params (handlers/get-oauth-access-token-handler params)) 
  (GET "/calendar/list" [] (handlers/list-calendars-handler))
  (GET "/event/list/:calendar-id" [calendar-id] (handlers/list-events-handler calendar-id))
  (route/not-found (resp/response {:error "Route not found"})
    ,,,))



(comment
  (clojure.pprint/pprint auth-token)
  ,,,)

