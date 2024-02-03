(ns timekeeper.api.handlers
  (:require [ring.util.response :as resp]
            [happy.oauth2 :as oauth]
            [timekeeper.config :as config]
            [timekeeper.database :as db]
            [timekeeper.api.auth :as api-auth]
            [happygapi.calendar.calendarList :as calendar]
            [happygapi.calendar.events :as event]
            [timekeeper.utils :as utils]
            [timekeeper.api.utils :refer [ok bad-request created]]))

(defn ping []
  (resp/response {:status 200
                  :message "It's fine"}))

(defn register [request]
  (let [data (:body request)
        user (db/create-user data)
        session (assoc {} :identity user)]
    (-> (resp/response (created {:user user
                                 :token (api-auth/create-token user)}))
        (assoc :session session))))

(defn login [request]
  (let [data (:body request)
        user (db/get-user data)
        identity-info (dissoc user :email)]
    (println user)
    (if (nil? user)
      (resp/response (bad-request {:error "Invalid credentials"}))
      (-> (resp/response (ok {:user user
                              :token (api-auth/create-token identity-info)}))
          (assoc :session (assoc {} :identity identity-info))))))

(defn get-oauth-access-token-handler [request]
  (let [code (:code (:params request))
        user-id (get-in (:identity (:session request)) [:id])
        token-data (oauth/exchange-code (config/oauth-config) code)
        saved-token (-> token-data
                         (dissoc :token_type)
                         (assoc :user-id user-id)
                         (utils/map-keys-to-hyphen)
                         (db/save-oauth-credentials))]
    (resp/response {:status 200
                    :message "OK"})))

(defn get-oauth-code-handler []
  (let [uri (oauth/set-authorization-parameters (config/oauth-config) (config/gapi-scopes))]
    (resp/redirect uri)))

(defn list-calendars-handler [request]
  (let [user (:identity (:session request))
        user-id (:id user)
        last-token (db/get-last-oauth-token-by-user-id user-id)]
    (resp/response (ok {:user last-token}))))
                    

; (defn list-calendars-handler []
;   (let [body (calendar/list$ (auth/auth!) {})]
;     resp/response {:status 200
;                    :body body})
  ; ,,,)
 
; (defn list-events-handler [calendar-id]
;   (let [body (event/list$ (auth/auth!) {:calendarId calendar-id})]
;     resp/response {:status 200
;                    :body body}))

(comment
  (require '[taoensso.carmine :as car])
  (def my-conn-spec {:pool {}
                     :spec {:host "localhost"
                            :port 6379}})
  (def redis-conn {:pool {}
                   :spec (config/redis-config)})
  (def conn (car/connection-pool my-conn-spec))

  (car/wcar redis-conn (car/ping))
  (println redis-conn)
  (db/get-user {:username "matheus" :password "12345"})

  ,,,)
