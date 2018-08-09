(ns re-lastic.recipes.elastic
  "Setting up Elasticsearch instance"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.file :refer (line)]
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.output :refer (summary)]))

(defn elastic
  "Setting up Elasticsearch"
  [{:keys [cluster node data] :or {data "/var/lib/elasticsearch"}}]
  (->
   (package "openjdk-8-jre" "elasticsearch")
   (line "/etc/elasticsearch/elasticsearch.yml" "network.host: 0.0.0.0\n" :present)
   (line "/etc/elasticsearch/elasticsearch.yml" (<< "cluster.name: ~{cluster}\n") :present)
   (line "/etc/elasticsearch/elasticsearch.yml" (<< "node.name: ~{node}") :present)
   (line "/etc/elasticsearch/elasticsearch.yml" "path.data:" data " " :set)
   (service "elasticsearch" :restart)
   (summary "elastic setup done")))
