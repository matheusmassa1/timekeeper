(ns timekeeper.system
  (:require [com.stuartsierra.component :as component]
            [timekeeper.components :refer [new-database new-http-server]]
            [timekeeper.app :refer [app]]))


(defn system []
  (let [db (new-database "mongodb://user:12345@localhost:27017/timekeeper")
        context {:db db}
        http-server (new-http-server 3001 (app context))]
    (component/system-map
     :db db
     :http-server (component/using http-server [:db]))))
