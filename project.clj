(defproject hello-duct "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [duct/core "0.7.0"]
                 [duct/module.ataraxy "0.3.0"]
                 [duct/module.cljs "0.4.1"]
                 [duct/module.logging "0.4.0"]
                 [duct/module.web "0.7.0"]
                 [duct.module.pedestal "2.0.1"]
                 [com.walmartlabs/lacinia "0.33.0-alpha-3"]
                 [com.walmartlabs/lacinia-pedestal "0.11.0"]
                 [io.pedestal/pedestal.jetty "0.5.5"]
                 [io.pedestal/pedestal.service "0.5.5"]
                 [metosin/reitit-ring "0.3.7"]]
  :plugins [[duct/lein-duct "0.12.0"]]
  :main ^:skip-aot hello-duct.main
  :resource-paths ["resources" "target/resources"]
  :prep-tasks     ["javac" "compile" ["run" ":duct/compiler"]]
  :middleware     [lein-duct.plugin/middleware]
  :profiles
  {:dev  [:project/dev :profiles/dev]
   :repl {:prep-tasks   ^:replace ["javac" "compile"]
          :dependencies [[cider/piggieback "0.4.0"]]
          :repl-options {:init-ns user
                         :nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}
   :uberjar {:aot :all}
   :profiles/dev {}
   :project/dev  {:source-paths   ["dev/src"]
                  :resource-paths ["dev/resources"]
                  :dependencies   [[integrant/repl "0.3.1"]
                                   [eftest "0.5.7"]
                                   [kerodon "0.9.0"]]}})
