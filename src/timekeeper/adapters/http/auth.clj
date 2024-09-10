(ns timekeeper.adapters.http.auth
  (:require [timekeeper.ports.auth :refer [register-user!]]
            [timekeeper.utils :refer [bad-request created ok keyword->table-name sanitize]]))


(defn get-dispatcher
  [context]
  (get-in context [:dispatcher :dispatcher]))

(defmulti ->action
  (fn [input _]
    (let [action (:action input)]
      (cond
        (nil? action) :no-action
        (keyword? action) :simple-action
        (vector? action) :action-with-args))))

(defmethod ->action :no-action
  [_ _]
  nil)

(defmethod ->action :simple-action
  [input context]
  (let [dispatcher (get-dispatcher context)
        action-fn (get-in dispatcher [(:action input)])]
    action-fn))

(defmethod ->action :action-with-args
  [input context]
  (let [dispatcher (get-dispatcher context)
        [action action-args] (:action input)
        action-fn (get-in dispatcher [action])]
    (partial action-fn action-args)))

(defn process-output
  [biz-logic-output action-fn]
  (let [status (:status biz-logic-output)]
    (condp = status
      :ok (-> (:data biz-logic-output)
              action-fn
              sanitize
              (ok))
      :created (-> (:data biz-logic-output)
                   action-fn
                   sanitize
                   (created))
      :error (bad-request {:error (:message biz-logic-output)}))))

(defn ->handler [biz-logic-fn]
  (fn handler [req context]
    (let [input (:body req)
          dispatcher (get-dispatcher context)
          output (biz-logic-fn input dispatcher)
          action-fn (->action output context)]
      (process-output output action-fn))))

(def register-user-handler (->handler register-user!))
