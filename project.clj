(defproject slack-webhooks "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [cheshire "5.5.0"]
                 [http-kit "2.0.0"]
                 [ring/ring-defaults "0.1.5"]
                 [enlive "1.1.6"]
                 [clj-http "2.0.1"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler slack-webhooks.handler/app}
  :main slack-webhooks.handler
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]
                        [cheshire "5.5.0"]
                        [ring/ring-json "0.4.0"]]}})
