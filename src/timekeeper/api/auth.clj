(ns timekeeper.api.auth
  (:require
   [ring.util.response :as resp]
   [buddy.sign.jwt :as jwt]
   [buddy.auth.backends.token :refer [jws-backend]]
   [buddy.auth.accessrules :refer (success error)]
   [buddy.auth :refer [authenticated?]]
   [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
   [timekeeper.api.utils :refer [ok bad-request]]
   [java-time.api :as jt]))


(def secret "mysecret")

(def auth-data {:username "admin" :password "admin"})

(def auth-backend
  (jws-backend {:secret secret
                :options {:options :hs512}}))

(defn register []
  true)

(defn is-valid-user [username password]
  (some-> auth-data
         (:username)
         (= username)
         (and (= (:password auth-data) password))))

(defn create-token [username secret]
  (let [claims {:user username}
        token (jwt/sign claims secret)]
    token))

(defn login [request]
  (let [u (get-in request [:body :username])
        p (get-in request [:body :password])
        valid? (is-valid-user u p)]
    (if valid?
      (let [token (create-token u secret)]
        (ok {:token token}))
      (bad-request {:error "Invalid credentials"}))))

(defn authorization-handler [handler]
  (fn [request]
    (let [_ (println "Authorization: " (:identity request))]
      (handler request))))

(defn authentication-handler [handler]
  (fn [request]
    (let [_ (println "Authentication: " :identity (:identity request))]
      (handler request))))

(defn wrap-jwt-authentication [handler]
  (wrap-authentication handler auth-backend))

(defn authenticated-access [request]
  (if (authenticated? request)
    (success)
    (error)))


;; (defn backend []
;;   (let [secret (jwt-secret)]
;;     (backends/jws {:secret secret})))

;; (defn wrap-jwt-authentication [handler]
;;   (wrap-authentication handler backend))

;; (defn auth-middleware [handler]
;;   (fn [request]
;;     (if (authenticated? request)
;;       (handler request)
;;       (resp/response {:status 401 :body {:error "Unauthorized"}}))))

;; (defn create-token [payload]
;;   (jwt/si
   ;; gn payload (jwt-secret)))

(comment 
  (backend)
  ,,,)

