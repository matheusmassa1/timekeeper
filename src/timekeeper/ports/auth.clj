(ns timekeeper.ports.auth
  (:require [timekeeper.domain.user :refer [extract-login-info validate-user-login is-valid-user?]]
            [timekeeper.adapters.auth :refer [sign-token-adapter]]
            [timekeeper.logic.auth :refer [register-user login]]
            [buddy.hashers :as h]))

(defn sign-token [sign-fn claims]
  (sign-fn claims))

(defn register-user! [data dispatcher]
  (let [find-fn (partial (:db/find-one dispatcher) "users")]
    (register-user find-fn data)))

(defn login! [dispatcher data]
  (let [find-fn (:db/find-one dispatcher)]
    (login find-fn data)))
