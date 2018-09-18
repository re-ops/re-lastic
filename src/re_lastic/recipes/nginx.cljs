(ns re-lastic.recipes.nginx
  "Nginx https+auth reverse proxy for Elasticsearch and Kibana"
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-conf.core :refer (apply*)]
   [re-conf.resources.file :refer (template directory symlink)]
   [re-conf.resources.shell :refer (exec)]
   [re-conf.resources.facts :refer (hostname)]
   [re-conf.resources.output :refer (summary)]
   [re-conf.resources.service :refer (service)]
   [re-conf.resources.firewall :refer (rule firewall)]
   [re-conf.resources.pkg :refer (package)]))

(defn reverse-proxy
  "Nginx revese proxy for Elasticsearch"
  [{:keys [domain nginx] :as env}]
  (let [fqdn {:fqdn (<< "~(hostname).~{domain }")}
        available "/etc/nginx/sites-available/"
        enabled "/etc/nginx/sites-enabled"
        instances ["kibana" "elasticsearch" "grafana"]
        input (fn [k] (merge (assoc env :hostname (hostname) :product k) (nginx (keyword k))))
        temp-args (fn [k] ["resources/nginx/elk.mustache" (<< "~{available}/~{k}.conf") (input k)])
        link-args (fn [k] [(<< "~{available}/~{k}.conf") (<< "~{enabled}/~{k}.conf") :present])]
    (->
     (package "nginx")
     (apply* template temp-args instances)
     (apply* symlink link-args instances)
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

(defn firewalling
  "network hardening"
  []
  (->
   (rule {:port 22})
   (rule {:port 9201})
   (rule {:port 5602})
   (rule {:port 3001})
   (firewall :present)
   (summary "nginx firewalling")))
