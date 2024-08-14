(ns timekeeper.api.routes
  (:require [timekeeper.api.handlers :as handlers]
            [timekeeper.api.auth :refer [login auth-access-rules]]
            [buddy.auth.accessrules :refer [restrict success error]]
            [ring.util.response :as resp]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/healthCheck" req (handlers/ping req))
  ;; (POST "/register" params (handlers/register params))
  (POST "/login" params (login params))
  (GET "/requestInfo" [] handle-dump)
  ;; (GET "/gapiAuth" [] (handlers/get-oauth-code-handler))
  ;; (GET "/oauthCallback" params (handlers/get-oauth-access-token-handler params))
  ;; (GET "/calendar/list" params (handlers/list-calendars-handler params))
  ; (GET "/event/list/:calendar-id" [calendar-id] (handlers/list-events-handler calendar-id))
  (route/not-found (resp/response {:error "Route not found"})
    ,,,))



(comment
  ,,,)

