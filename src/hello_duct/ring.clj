(ns hello-duct.ring
  (:require [integrant.core :as ig]
            [reitit.ring :as r-ring]
            [reitit.ring.coercion :as r-ring-corercion]
            [ring.adapter.jetty :as jetty]))

(defmethod ig/init-key ::router
  [_ {:keys [routes opts]}]
  (r-ring/router routes opts))

(defmethod ig/init-key ::middlewares
  [_ _]
  [r-ring-corercion/coerce-exceptions-middleware
   r-ring-corercion/coerce-request-middleware
   r-ring-corercion/coerce-response-middleware])

(defn- error-response
  [status-code msg]
  {:status status-code
   :headers {"Content-Type" "text/plain"}
   :body msg})

(defmethod ig/init-key ::ring-handler
  [_ {:keys [router middlewares]}]
  (r-ring/ring-handler router
                       (constantly (error-response 404 "not found"))
                       {:middleware middlewares}))

(defmethod ig/init-key ::jetty
  [_ {:keys [handler opts]}]
  (jetty/run-jetty handler (merge {:join? false :port 8888} opts)))

(defmethod ig/halt-key! ::jetty
  [_ jetty]
  (.stop jetty))
