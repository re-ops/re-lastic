(ns re-lastic.recipes.logstash
  "Setting up Logstash instance"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.pkg :refer (package)]))

(defn logstash
  "Setting up Logstash"
  []
  (->
   (package "logstash" :present)
   (service "kibana" :start)
   (summary "logstash setup done")))
