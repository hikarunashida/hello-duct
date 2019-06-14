(ns hello-duct.ring
  (:require [integrant.core :as ig]
            [reitit.ring :as r-ring]))

(defmethod ig/init-key ::router
  [_ {:keys [routes opts]}]
  (r-ring/router routes opts))

(defn- error-response [status-code msg]
  {:status status-code
   :headers {"Content-Type" "text/plain"}
   :body msg})

(defmethod ig/init-key ::ring-handler
  [_ {:keys [router middlewares]}]
  (r-ring/ring-handler router
                       (constantly (error-response 404 "not found"))
                       {:middleware middlewares}))
