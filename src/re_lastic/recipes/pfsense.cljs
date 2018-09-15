(ns re-lastic.recipes.pfsense
  "Pfsense settings"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.firewall :refer (rule)]
   [re-conf.resources.git :refer (clone)]
   [re-conf.resources.file :refer (directory symlink)]
   [re-conf.resources.pkg :refer (package)]))

(defn pfsense-rules
  "Ports required for Pfsense logstash"
  [c]
  (->
   (rule {:port 5044}); filebeat snort
   (rule {:port 5000}); syslog blocks
))

(defn pfsense-logstash
  "Setting up Pfsense support"
  [c]
  (let [logstash "/etc/logstash/" pfsense "/etc/pfsense-kibana"]
    (->
     (pfsense-rules c)
     (package "git")
     (directory conf-d :absent)
     (clone "git://github.com/narkisr/pfsense-kibana.git" pfsense)
     (symlink (<< "~{pfsense}/conf.d") (<< "~{logstash}/conf.d") :present)
     (symlink (<< "~{pfsense}/patterns") (<< "~{logstash}/patterns") :present))))

