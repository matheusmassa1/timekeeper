(ns timekeeper.utils
  (:require [malli.core :as m]
            [malli.error :as me]))

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

(defn keyword->table-name [kw]
  (if (namespace kw)
    (str (namespace kw) "_" (name kw))
    (name kw)))

(defn ok [d] {:status 200 :body d})

(defn created [d] {:status 201 :body d})

(defn bad-request [d] {:status 400 :body d})

(defn unauthorized [d] {:status 401 :body d})

(defn validate-schema [schema data]
  (if (m/validate schema data)
    {:valid? true}
    (let [errors (me/humanize (m/explain schema data))]
      {:valid? false :error errors})))

(defn sanitize [data]
  (apply dissoc data [:password]))
