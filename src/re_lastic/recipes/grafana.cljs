(ns re-lastic.recipes.grafana
  "Setting up Elasticsearch instance"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.pkg :refer (package)]
   [re-conf.resources.download :refer (download)]))

(defn grafana
  "Setting up Grafana repo and package"
  []
  (let [file "/tmp/grafana.gpg"
        repo "deb https://packagecloud.io/grafana/stable/debian/ stretch main"]
    (->
     (package "apt-transport-https")
     (download "https://packagecloud.io/gpg.key" file)
     (key-file file)
     (repository repo :present)
     (update)
     (package "grafana")
     (summary "grafana done"))))
