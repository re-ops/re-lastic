(ns re-lastic.recipes.nginx
  "Nginx https+auth reverse proxy for Elasticsearch and Kibana"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.file :refer (template)]
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.pkg :refer (package)]))

(defn reverse-proxy
  "Nginx revese proxy for Elasticsearch"
  [env]
  (->
   (package "nginx")
   (template env "resources/nginx/elastic.mustache" "/etc/nginx/sites-available/elastic.conf")
   (template env "resources/nginx/kibana.mustache" "/etc/nginx/sites-available/kibana.conf")
   (service "nginx" :restart)
   (summary "reverse proxy")))


