(ns timekeeper.database
  (:require [timekeeper.config :as config]
            [next.jdbc :as jdbc]))

(def db (jdbc/get-datasource (config/db-config)))

