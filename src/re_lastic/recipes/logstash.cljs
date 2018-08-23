(ns re-lastic.recipes.logstash
  "Setting up Logstash instance"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.git :refer (clone)]
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
   (service "logstash" :start)
   (summary "logstash setup done")))

(defn pfsense
  "Setting up pfsense grok support"
  []
  (let [conf-d "/etc/logstash/conf.d/" pfsense "/etc/pfsense-kibana/"]
    (->
     (package "git")
     (directory conf-d :absent)
     (clone "git://github.com/narkisr/pfsense-kibana.git" pfsense)
     (symlink (<< "~{pfsense}/conf.d") conf-d :present)
     (summary "logstash pfsense"))))
