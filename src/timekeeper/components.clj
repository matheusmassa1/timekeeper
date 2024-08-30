(ns timekeeper.components
  (:require [com.stuartsierra.component :as component]
            [monger.core :as mg]
            [ring.adapter.jetty :as jetty]))

(defrecord Database [uri connection db]
  component/Lifecycle

  (start [this]
    (println ";; Starting database connection")
    (let [{:keys [uri]} this
          {:keys [conn db]} (mg/connect-via-uri uri)]
      (assoc this
             :connection conn
             :db db)))

  (stop [this]
    (println ";; Stopping database connection")
    (when connection
      (mg/disconnect connection))
    (assoc this
           :connection nil
           :db nil)))

(defn new-database [uri]
  (map->Database {:uri uri}))

(defrecord HTTPServer [port app context server]
  component/Lifecycle

  (start [this]
    (println ";; Starting web server")
    (let [server-instance (jetty/run-jetty (app context) {:port port :join? false})]
      (assoc this :server server-instance)))

  (stop [this]
    (println ";; Stopping web server")
    (when server
      (.stop server))
    (assoc this :server nil)))

(defn new-http-server [port app]
  (map->HTTPServer {:port port :app app}))

(defrecord ActionDispacher [actions-fn db-component dispatcher]
  component/Lifecycle

  (start [this]
    (println ";; Starting action dispatcher")
    (let [db (:db db-component)
          {:keys [actions-fn]} this]
      (assoc this :dispatcher (actions-fn db))))

  (stop [this]
    (println ";; Stopping action dispatcher")
    (assoc this :dispatcher nil)))

(defn new-action-dispatcher [actions-fn]
  (map->ActionDispacher {:actions-fn actions-fn}))
