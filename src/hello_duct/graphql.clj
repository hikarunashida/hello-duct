(ns hello-duct.graphql
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.walmartlabs.lacinia.pedestal :as pedestal]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [integrant.core :as ig]))

;; TODO: add resolvers
(def resolver-map
  {:BoardGame_designers (constantly [])
   :Designer_games (constantly [])
   :query_game-by-id (constantly nil)})

(defmethod ig/init-key ::schema
  [_ {:keys [schema-path]}]
  (-> schema-path
      io/resource
      slurp
      edn/read-string
      (util/attach-resolvers resolver-map)
      schema/compile))

(defmethod ig/init-key ::service
  [_ {:keys [schema options]}]
  (pedestal/service-map schema options))
