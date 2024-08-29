(ns timekeeper.adapters.auth
  (:require
   [buddy.sign.jwt :as jwt]
   [buddy.auth.backends.token :refer [jws-backend]]))

(defonce secret "myscret")

(def auth-backend
  (jws-backend {:secret secret
                :token-name "Bearer"
                :options {:algorithm :HS256}}))

(defn sign-token-adapter [claims]
  (jwt/sign claims secret))
