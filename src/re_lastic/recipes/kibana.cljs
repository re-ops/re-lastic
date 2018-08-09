(ns re-lastic.recipes.kibana
  "Setting up Elasticsearch instance"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.pkg :refer (package)]))

(defn kibana
  "Setting up Kibana"
  []
  (->
   (package "kibana-oss" :present)
   (service "kibana" :start)
   (summary "kibana setup done")))
