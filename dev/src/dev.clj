(ns dev
  (:refer-clojure :exclude [test])
  (:require [clojure.repl :refer :all]
            [fipp.edn :refer [pprint]]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.java.io :as io]
            [duct.core :as duct]
            [duct.core.repl :as duct-repl]
            [duct.repl.figwheel :refer [cljs-repl]]
            [eftest.runner :as eftest]
            [integrant.core :as ig]
            [integrant.repl :refer [clear halt go init prep reset]]
            [integrant.repl.state :refer [config system]]
            [clojure.walk :as walk]
            [com.walmartlabs.lacinia :as lacinia])
  (:import (clojure.lang IPersistentMap)))

(duct/load-hierarchy)

(defn read-config []
  (duct/read-config (io/resource "hello_duct/config.edn")))

(defn test []
  (eftest/run-tests (eftest/find-tests "test")))

(def profiles
  [:duct.profile/dev :duct.profile/local])

(clojure.tools.namespace.repl/set-refresh-dirs "dev/src" "src" "test")

(when (io/resource "local.clj")
  (load "local"))

(integrant.repl/set-prep! #(duct/prep-config (read-config) profiles))

;;graphql

(defn sanitize-node
  [node]
  (cond
    (instance? IPersistentMap node)
    (into {} node)

    (seq? node)
    (vec node)

    :else
    node))

(defn simplify
  [m]
  (walk/postwalk sanitize-node m))

(defn q
  [query-string]
  (-> system
      :hello-duct.graphql/schema
      (lacinia/execute query-string nil nil)
      simplify))
