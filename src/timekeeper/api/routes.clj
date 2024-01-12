(ns timekeeper.api.routes
  (:require [timekeeper.api.handlers :as handlers]
            [ring.util.response :as resp]
            [ring.handler.dump :refer [handle-dump]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (resp/response {:message "It's fine!"}))
  (GET "/request-info" [] handle-dump)
  (GET "/gapi-auth" [] (handlers/get-oauth-code-handler))
  (GET "/oauth-callback" params (handlers/get-oauth-access-token-handler params)) 
  (GET "/calendar/list" [] (handlers/list-calendars-handler))
  (route/not-found (resp/response {:error "Route not found"})
    ,,,))



(comment
  (clojure.pprint/pprint auth-token)
  ,,,)

