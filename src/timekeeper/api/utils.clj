(ns timekeeper.api.utils)

(defn ok [d] {:status 200 :body d})

(defn created [d] {:status 201 :body d})

(defn bad-request [d] {:status 400 :body d})

(defn unauthorized [d] {:status 401 :body d})
