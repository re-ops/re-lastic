(ns re-base.rcp.main
  (:require-macros
   [clojure.core.strint :refer (<<)])
  (:require
   [cljs.core.async :as async :refer [take!]]
   [cljs-node-io.core :as io]
   [re-conf.core :refer (invoke invoke-all report-n-exit assert-node-major-version)]
   [re-conf.resources.log :refer (info debug error)]))


(defn server
  "Setting up only an elasticserver instance"
  [env]
  (report-n-exit
   (invoke-all env re-base.recipes.elastic)))

(defn stack
  "Set up an entire elk stack"
  [env]
  (report-n-exit
   (invoke-all env
               re-base.recipes.elastic
               re-base.recipes.kibana
               re-base.recipes.grafana
               )))

(defn run-profile [env profile]
  (fn [_]
    (case (keyword profile)
      :server (server env)
      :stack (stack env))))

(defn -main [e profile & args]
  (assert-node-major-version)
  (let [env (if e (cljs.reader/read-string (io/slurp e)) {})]
    (take! (initialize)
           (fn [r]
             (info "Provisioning machine using re-elastic!" ::main)
             (run-profile env profile)))))

(set! *main-cli-fn* -main)

(comment
  (-main "resources/dev.edn"))
