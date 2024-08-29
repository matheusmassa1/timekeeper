(ns timekeeper.handlers
  (:require [ring.util.response :as resp]
            [timekeeper.adapters.database :as db]
            [happy.oauth2 :as oauth]
            [timekeeper.config :as config]
            [happygapi.calendar.calendarList :as calendar]
            [happygapi.calendar.events :as event]
            [timekeeper.utils :refer [ok bad-request created timestamp->string]]
            [timekeeper.auth :refer [is-valid-user create-token secret]]
            [monger.collection :as mc]
            [malli.core :as m]
            [timekeeper.ports.user :refer [register-user]]
            [timekeeper.ports.auth :as p]
            [buddy.auth.backends.token :as token]))

(defn ping [_]
  (resp/response {:status 200
                  :body "pong"}))

(defn login [req]
  (let [data (:body req)
        u (get-in data [:username])
        p (get-in data [:password])
        valid? (is-valid-user u p)]
    (println data)
    (if valid?
      (let [token (create-token data secret)]
        (ok {:token token}))
      (bad-request {:error "Invalid credentials"}))))

(defn new-login [req context]
  (let [db (get-in context [:db :db])
        data (:body req)
        find-fn (partial db/find-user db)
        token (p/login find-fn data)]
    (if token
      (ok {:token token})
      (bad-request {:error "Invalid credentials"}))))

(defn register-user-handler [req context]
  (let [db (get-in context [:db :db])
        data (:body req)
        find-fn (partial db/find-user db)
        register-fn (partial db/register-user-adapter db)
        user (register-user find-fn register-fn data)]
    (if user
      (let [sanitized-user (dissoc user :password)]
        (created sanitized-user))
      (bad-request {:error "User already exists"}))))

;; (defn register [req]
;;   (let [{:keys [db body]} req]
;;     (if (validate-user-registration body)
;;       (if-let [user (db/create-user db body)]
;;         (created user)
;;         (bad-request {:error "User already exists"}))
;;       (bad-request {:error "Invalid data"}))))



(comment ,,,)

;; (defn register [req]
;;   (let [db (get-in req [:db])
;;         data (:body req)
;;         valid? (m/validate User data)]
;;     (if-not valid?
;;       (bad-request {:error "Invalid data"})
;;       (let [exists? (db/get-user-by-username db (:username data))]
;;         (if exists?
;;           (bad-request {:error "User already exists"})
;;           (-> (db/create-user db data)
;;               (created)))))))

;; (defn get-oauth-access-token-handler [request]
;;   (let [code (:code (:params request))
;;         user-id (get-in (:identity (:session request)) [:id])
;;         token-data (oauth/exchange-code (config/oauth-config) code)
;;         saved-token (-> token-data
;;                          (dissoc :token_type)
;;                          (assoc :user-id user-id)
;;                          (utils/map-keys-to-hyphen)
;;                          (db/save-oauth-credentials))]
;;     (resp/response {:status 200
;;                     :message "OK"})))

;; (defn get-oauth-code-handler []
;;   (let [uri (oauth/set-authorization-parameters (config/oauth-config) (config/gapi-scopes))]
;;     (resp/redirect uri)))

;; (defn list-calendars-handler [request]
;;   (let [user (:identity (:session request))
;;         user-id (:id user)
;;         last-token (db/get-last-oauth-token-by-user-id user-id)]
;;     (resp/response (ok {:user last-token}))))


; (defn list-calendars-handler []
;   (let [body (calendar/list$ (auth/auth!) {})]
;     resp/response {:status 200
;                    :body body})
  ; ,,,)
 
; (defn list-events-handler [calendar-id]
;   (let [body (event/list$ (auth/auth!) {:calendarId calendar-id})]
;     resp/response {:status 200
;                    :body body}))

(comment ,,,)
