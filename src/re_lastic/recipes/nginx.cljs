(ns re-lastic.recipes.nginx
  "Nginx https+auth reverse proxy for Elasticsearch and Kibana"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.file :refer (template)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.facts :refer (os)]
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.pkg :refer (package)]))

(defn reverse-proxy
  "Nginx revese proxy for Elasticsearch"
  [{:keys [domain] :as input}]
  (let [fqdn {:fqdn (<< "~(os :hostname).~{domain }")}]
    (->
     (package "nginx")
     (template input "resources/nginx/elastic.mustache" "/etc/nginx/sites-available/elastic.conf")
     (template input "resources/nginx/kibana.mustache" "/etc/nginx/sites-available/kibana.conf")
     (service "nginx" :restart)
     (summary "reverse proxy"))))

(defn ssl
  "SSL setup for nginx"
  [{:keys [domain]}]
  (let [fqdn (<< "~(os :hostname).~{domain}")
        subj (<< "/C=pp/ST=pp/L=pp/O=pp Inc/OU=DevOps/CN=~{fqdn}/emailAddress=dev@~{fqdn}")
        dest "/etc/nginx/ssl"
        openssl "/usr/bin/openssl"]
    (->
     (package "apache3-utils" "openssl")
     (exec openssl "req" "-nodes" "-newkey" "rsa:2048" "-keyout" (<< "~{dest}/~{fqdn}.key")  "-out" (<< "~{dest}/~{fqdn}.csr") "-subj" subj)
     (exec openssl "dhparam" "-dsaparam" "-out" (<< "~{dest}/dhparam.pem") "4096")
     (summary "ssl"))))
