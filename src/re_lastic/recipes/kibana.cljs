(ns re-lastic.recipes.kibana
  "Setting up Elasticsearch instance"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.download :refer (download)]))

(defn kibana
  "Setting up Kibana"
  []
  (->
   (package "kibana-oss" :present)
   (service "kibana" :start)
   (summary "kibana setup done")))
