(ns re-lastic.recipes.pfsense
  "Pfsense settings"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.git :refer (clone)]
   [re-conf.resources.file :refer (directory symlink)]
   [re-conf.resources.pkg :refer (package)]))

(defn pfsense-logstash
  "Setting up Pfsense Grok support"
  [c]
  (let [conf-d "/etc/logstash/conf.d/" pfsense "/etc/pfsense-kibana"]
    (->
     (package c "git")
     (directory conf-d :absent)
     (clone "git://github.com/narkisr/pfsense-kibana.git" pfsense)
     (symlink (<< "~{pfsense}/conf.d/") conf-d :present))))
