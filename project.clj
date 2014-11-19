(defproject thirty-thirty "0.1.0-SNAPSHOT"
  :description "Take photos every day."
  :url "https://github.com/myobie/thirty-thirty"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [clj-http "1.0.0"]
                 [compojure "1.2.0"]
                 [ring/ring-defaults "0.1.2"]
                 [environ "1.0.0"]
                 [midje "1.6.3"]]
  :plugins [[lein-ring "0.8.13"]
            [lein-environ "1.0.0"]
            [lein-midje "3.0.0"]]
  :ring {:handler thirty-thirty.core.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
