(ns timekeeper.ports.auth
  (:require [timekeeper.domain.user :refer [extract-login-info validate-user-login is-valid-user? generate-token-payload]]
            [timekeeper.adapters.auth :refer [sign-token-adapter]]))

(defn sign-token [sign-fn claims]
  (sign-fn claims))
