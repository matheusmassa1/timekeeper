(ns timekeeper.api.handlers
  (:require [ring.util.response :as resp]
            [happy.oauth2 :as oauth]
            [timekeeper.config :as config]
            [timekeeper.gapi-auth :as auth]
            [happygapi.calendar.calendarList :as calendar]))
   


(defn extract-code [request]
  (get-in request [:params "code"]))

(defn get-oauth-access-token-handler [request]
  (->> request
      (extract-code)
      (oauth/exchange-code (config/oauth-config))
      (auth/set-access-token!))
  (resp/response {:status 200
                  :body "OK"}))

(defn get-oauth-code-handler []
  (let [uri (oauth/set-authorization-parameters 
              (config/oauth-config) (config/gapi-scopes))]
    (resp/redirect uri)))

(defn list-calendars-handler []
  (let [body (calendar/list$ (auth/auth!) {})]
    resp/response {:status 200
                   :body body})
  ,,,)
  

(comment 
  (clojure.pprint/pprint @auth/gapi-access-token)
  (auth/auth! @auth/gapi-access-token)
  ,,,)
