(ns hello-duct.handler.example
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response] 
            [integrant.core :as ig]))

(defn- json-response
  [data]
  {:status 200 :body data})

(defmethod ig/init-key ::example
  [_ _]
  (fn [_] (json-response {:example "data"})))

(defmethod ig/init-key ::hello
  [_ _]
  (fn [_] "<h1>Hello World</h1>"))

(defmethod ig/init-key ::plus
  [_ _]
  (fn [{{:keys [x y]} :query-params}]
    (json-response (+ x y))))
