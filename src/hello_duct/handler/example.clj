(ns hello-duct.handler.example
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response] 
            [integrant.core :as ig]))

(defmethod ig/init-key :hello-duct.handler/example
  [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok {:example "data"}]))

(defmethod ig/init-key :hello-duct.handler/hello
  [_ options]
  (fn [{[_] :ataraxy/result}]
    [::response/ok "<h1>Hello World</h1>"]))
