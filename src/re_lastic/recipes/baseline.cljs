(ns re-lastic.recipes.elastic
  "Setting up Elasticsearch instance"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.download :refer (download)]))

(defn elastic
  "Setting up Elasticsearch repo and package"
  []
  (package "gt5")
  (package "nmap"))
