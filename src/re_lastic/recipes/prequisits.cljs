(ns re-lastic.recipes.prequisits
  "Common prequisits"
  (:require
   [re-conf.resources.pkg :refer (package key-file update repository)]
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.download :refer (download)]))

(defn repo
  "Setting up Elasticsearch repo"
  []
  (let [file "/tmp/GPG-KEY-elasticsearch"
        repo "deb https://artifacts.elastic.co/packages/6.x/apt stable main"]
    (->
      (package "apt-transport-https")
      (download "https://artifacts.elastic.co/GPG-KEY-elasticsearch" file)
      (key-file file)
      (repository repo :present)
      (update)
      (summary "elastic repository done")
      )))
