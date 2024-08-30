(ns timekeeper.system
  (:require [com.stuartsierra.component :as component]
            [timekeeper.components :refer [new-database new-http-server new-action-dispatcher]]
            [timekeeper.config :refer [db-prod-actions]]
            [timekeeper.app :refer [app]]))


(defn system []
   (component/system-map
     :db (new-database "mongodb://user:12345@localhost:27017/timekeeper")
     :dispatcher (component/using (new-action-dispatcher db-prod-actions) {:db-component :db})
     :context (component/using {} {:db :db :dispatcher :dispatcher})
     :http-server (component/using (new-http-server 3001 app) {:context :context})))
