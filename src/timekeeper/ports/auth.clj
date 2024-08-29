(ns timekeeper.ports.auth
  (:require [timekeeper.domain.user :refer [extract-login-info validate-user-login is-valid-user? generate-token-payload]]
            [timekeeper.adapters.auth :refer [sign-token-adapter]]
            [buddy.hashers :as h]))

(defn sign-token [sign-fn claims]
  (sign-fn claims))

(defn login [find-fn auth-data]
  (when (validate-user-login auth-data)
    (let [user-data (find-fn auth-data)]
      (when (seq user-data)
        (let [hashed-password (h/derive (:password auth-data))
              auth-data-with-hashed-password (assoc auth-data :password hashed-password)]
          (when (is-valid-user? user-data auth-data-with-hashed-password)
            (-> user-data
                generate-token-payload
                (sign-token sign-token-adapter))))))))
