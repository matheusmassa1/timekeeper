(ns timekeeper.system
  (:require [com.stuartsierra.component :as component]
            [timekeeper.components :refer [new-database new-http-server]]
            [timekeeper.app :refer [app]]))


(defn system []
   (component/system-map
     :db (new-database "mongodb://user:12345@localhost:27017/timekeeper")
     :context (component/using {} {:db :db})
     :http-server (component/using (new-http-server 3001 app) {:context :context})))
