(ns re-lastic.recipes.logstash
  "Setting up Logstash instance"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.git :refer (clone)]
   [re-lastic.recipes.pfsense :refer (pfsense-logstash)]
   [re-conf.resources.file :refer (directory symlink)]
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.pkg :refer (package)]))

(defn logstash
  "Setting up Logstash"
  []
  (->
   (package "openjdk-8-jre")
   (package "logstash" :present)
   (pfsense-logstash)
   (service "logstash" :start)
   (summary "logstash setup done")))

