(ns re-lastic.recipes.main
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [re-lastic.recipes.kibana]
   [re-lastic.recipes.grafana]
   [re-lastic.recipes.elastic]
   [re-lastic.recipes.prequisits]
   [cljs.core.async :as async :refer [take!]]
   [cljs-node-io.core :as io]
   [re-conf.core :refer (invoke invoke-all report-n-exit assert-node-major-version)]
   [re-conf.resources.pkg :refer (initialize)]
   [re-conf.resources.log :refer (info debug error)]))

(defn elk
  "Setting up only an elasticserver instance"
  [env]
  (report-n-exit
   (invoke-all env re-lastic.recipes.elastic  re-lastic.recipes.kibana re-lastic.recipes.grafana)))

(defn run-profile [env profile]
  (fn [_]
    (case (keyword profile)
      :elk (elk env))))

(defn -main [e profile & args]
  (assert-node-major-version)
  (let [env (if e (cljs.reader/read-string (io/slurp e)) {})]
    (take! (initialize)
           (fn [r]
             (info "Provisioning machine using re-elastic!" ::main)
             (take! (invoke-all env re-lastic.recipes.prequisits)
                    (run-profile env profile))))))

(set! *main-cli-fn* -main)

(comment
  (-main "resources/dev.edn" "elk"))
