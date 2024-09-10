(ns timekeeper.logic.auth
  (:require [buddy.hashers :refer [derive]]
            [monger.operators :refer [$or]]
            [timekeeper.domain.user :refer [validate-registration-schema validate-login-schema]])
  (:import  [org.joda.time DateTime]
            [org.joda.time Minutes]))

(defn addMinutes [now]
  (-> now
      (.plusMinutes 120)))

(defn generate-jwt-token-payload [auth-data]
  (let [{:keys [username role]} auth-data
        exp (addMinutes (DateTime.))
        claims {:user username :exp exp :role role}]
    claims))

(defn create-registration-payload
  [input]
  (let [{:keys [username email password]} input
        hashed-password (derive password)
        created-at (DateTime.)]
    {:username username
     :password hashed-password
     :email email
     :created-at created-at}))

(defn is-valid-login? [db-data input]
  (let [{:keys [username password]} db-data]
    (some-> input
           (:username)
           (= username)
           (and (= (:password db-data) password)))))

(defn user-exists? [input]
  (when-not (empty? input)
    true))

(defn extract-login-keys [data]
  (for [k [:username :email]
        :let [v (get data k)]
        :when v]
    {k v}))

(defn register-user
  [find-fn data]
  (let [valid-schema (:valid? (validate-registration-schema data))]
    (if valid-schema
      (let [qry {$or (extract-login-keys data)}
            exists? (user-exists? (find-fn qry))]
        (if-not exists?
          (let [payload (create-registration-payload data)]
            {:data payload :status :created :action [:db/insert "users"]})
          {:status :error :message "User already exists"}))
      {:status :error :message "Schema validation failed"})))

;; TODO: Fix this
(defn login
  [find-fn data]
  (let [valid-schema (:valid? (validate-login-schema data))]
    (if valid-schema
      (if-let [db-data (find-fn data)]
        (if (is-valid-login? db-data data)
          (let [payload (generate-jwt-token-payload data)]
            {:data payload :status :ok :action :sign-token})
          {:status :error :message "Invalid credentials"})
        {:status :error :message "User not found"})
    {:status :error :message "Schema validation failed"})))

(comment
  (let [data {:username "matheus" :password (derive "123456")}
        find-fn (fn [_] data)]
    (login find-fn {:username "matheus" :password "12345"}))
  ,,,)
