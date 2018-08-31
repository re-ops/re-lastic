(ns re-lastic.recipes.nginx
  "Nginx https+auth reverse proxy for Elasticsearch and Kibana"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.resources.file :refer (template directory symlink)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.facts :refer (hostname)]
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.firewall :refer (rule firewall)]
   [re-conf.resources.pkg :refer (package)]))

(defn reverse-proxy
  "Nginx revese proxy for Elasticsearch"
  [{:keys [domain] :as env}]
  (let [fqdn {:fqdn (<< "~(hostname).~{domain }")}
        available "/etc/nginx/sites-available/"
        input (assoc env :hostname (hostname))
        enabled "/etc/nginx/sites-enabled"]
    (->
     (package "nginx")
     (template input "resources/nginx/elasticsearch.mustache" (<< "~{available}/elastic.conf"))
     (template input "resources/nginx/kibana.mustache" (<< "~{available}/kibana.conf"))
     (symlink  (<< "~{available}/elastic.conf") (<< "~{enabled}/elastic.conf") :present)
     (symlink  (<< "~{available}/kibana.conf") (<< "~{enabled}/kibana.conf") :present)
     (summary "reverse proxy"))))

(defn ssl
  "SSL setup for nginx"
  [{:keys [domain]}]
  (let [fqdn (<< "~(hostname).~{domain}")
        subj (<< "/C=pp/ST=pp/L=pp/O=pp Inc/OU=DevOps/CN=~{fqdn}/emailAddress=dev@~{fqdn}")
        dest "/etc/nginx/ssl"
        openssl "/usr/bin/openssl"]
    (->
     (package "apache2-utils" "openssl")
     (directory dest :present)
     (exec openssl "req" "-x509" "-nodes" "-days" "365" "-newkey" "rsa:2048" "-keyout" (<< "~{dest}/~{fqdn}.key")  "-out" (<< "~{dest}/~{fqdn}.crt") "-subj" subj)

     (exec openssl "dhparam" "-dsaparam" "-out" (<< "~{dest}/dhparam.pem") "4096")
     (summary "nginx ssl"))))

(defn network
  "network hardening"
  []
  (->
   (rule {:port 22})
   (rule {:port 443})
   (firewall :present)
   (summary "networking")))
