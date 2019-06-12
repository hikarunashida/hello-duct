(ns hello-duct.graphql
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.walmartlabs.lacinia :as lacinia]
            [com.walmartlabs.lacinia.pedestal :as pedestal]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [integrant.core :as ig]))

(defmethod ig/init-key ::load-data
  [_ {:keys [data-path]}]
  (prn [data-path])
  (-> data-path
      io/resource
      slurp
      edn/read-string))

(defn- resolve-game-by-id
  [games-map context args value]
  (let [{:keys [id]} args]
    (get games-map id)))

(defn- resolve-board-game-designers
  [designers-map context args board-game]
  (->> board-game
       :designers
       (map designers-map)))

(defn- resolve-designer-games
  [games-map context args designer]
  (let [{:keys [id]} designer]
    (->> games-map
         vals
         (filter #(-> % :designers (contains? id))))))

(defn- entity-map
  [data k]
  (reduce #(assoc %1 (:id %2) %2)
          {}
          (get data k)))

(defmethod ig/init-key ::resolver-map
  [_ {:keys [cgg-data]}]
  (let [entity-map* (partial entity-map cgg-data)
        games-map (entity-map* :games)
        designers-map (entity-map* :designers)]
    {:query_game-by-id (partial resolve-game-by-id games-map)
     :BoardGame_designers (partial resolve-board-game-designers designers-map)
     :Designer_games (partial resolve-designer-games games-map)}))

(defmethod ig/init-key ::load-schema
  [_ {:keys [schema-path resolver-map]}]
  (-> schema-path
      io/resource
      slurp
      edn/read-string
      (util/attach-resolvers resolver-map)
      schema/compile))

;; execute

(defmethod ig/init-key ::execute
  [_ {:keys [schema]}]
  (fn [query]
    (lacinia/execute schema query nil nil)))

;; service

(defmethod ig/init-key ::service
  [_ {:keys [schema options]}]
  (pedestal/service-map schema options))
