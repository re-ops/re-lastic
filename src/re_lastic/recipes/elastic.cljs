(ns re-lastic.recipes.elastic
  "Setting up Elasticsearch instance"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.output :refer (summary)]
   ))

(defn elastic
  "Setting up Elasticsearch"
  []
   (->
     (package "elasticsearch" :present)
     (summary "elastic setup done")))
