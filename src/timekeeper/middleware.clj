(ns timekeeper.middleware)

;; (defn wrap-db [handler db]
;;   (fn [req]
;;     (handler (assoc req :db db))))

(defn wrap-db [handler]
  (fn [req components]
    (let [db (:db components)]
      (handler (req db)))))
