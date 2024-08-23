(ns timekeeper.middleware)

(defn wrap-db [handler db]
  (fn [req]
    (handler (assoc req :db db))))
