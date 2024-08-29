(ns timekeeper.utils
  (:require [java-time.api :as jt]))

(defn transform-map-keys [kw transform-fn]
  (-> kw
      name
      (transform-fn)
      keyword))

(defn underscore-to-hyphen [kw]
  (transform-map-keys kw #(clojure.string/replace % "_" "-")))

(defn hyphen-to-underscore [kw]
  (transform-map-keys kw #(clojure.string/replace % "-" "_")))

(defn convert-keys [m transform-fn]
  (into {} (for [[k v] m]
             [(transform-fn k) v])))

(defn map-keys-to-hyphen [m]
  (convert-keys m underscore-to-hyphen))

(defn map-keys-to-underscore [m]
  (convert-keys m hyphen-to-underscore))

(defn timestamp->string [ts]
  (jt/format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" (jt/zoned-date-time ts (jt/zone-id "UTC"))))

(defn ok [d] {:status 200 :body d})

(defn created [d] {:status 201 :body d})

(defn bad-request [d] {:status 400 :body d})

(defn unauthorized [d] {:status 401 :body d})
