(ns user
  (:require [clojure.pprint :refer [pprint]]
            [clojure.tools.namespace.repl :as r]
            [com.stuartsierra.component :as component]
            [timekeeper.system :as s]))

(r/set-refresh-dirs "src/timekeeper" "test/timekeeper" "dev")

(def system nil)

(defn init []
  (alter-var-root #'system
                  (constantly (s/system))))

(defn start []
  (alter-var-root #'system
                  (fn [_] (component/start system))))

(defn stop []
  (alter-var-root #'system
                  (fn [s] (when s (component/stop s)))))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (r/refresh-all))


(comment
  (go)
  (stop)
  (reset)
  ;; Refresh changed namespaces
  (r/set-refresh-dirs "src/timekeeper" "test/timekeeper" "dev")
  (r/refresh-all)
  )
