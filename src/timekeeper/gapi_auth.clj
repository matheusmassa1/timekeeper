(ns timekeeper.gapi-auth
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [happy.oauth2 :as oauth2]
            [happy.oauth2-capture-redirect :as ocr]
            [clojure.edn :as edn]
            [cheshire.core :as json]
            [timekeeper.config :refer [oauth-config gapi-scopes]]))

(def gapi-access-token (atom {}))

(defn set-access-token! [token]
  (reset! gapi-access-token token))

(defn auth! []
  (let [credentials @gapi-access-token
        new-credentials (ocr/update-credentials (oauth-config) @gapi-access-token (gapi-scopes) nil)]
    (set-access-token! new-credentials)
    (oauth2/auth-header new-credentials)))
 

(comment
  (require '[happygapi.calendar.calendarList :as cal])
  (cal/list$ (auth!) {})
  ,)
