(ns slack-webhooks.handler
  (:require [slack-webhooks.parser :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [clojure.string :only [split] :as string]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [org.httpkit.server :refer [run-server]]
            [cheshire.core :as json]))

(def process-request)

(defroutes api-routes
  (GET "/" []
    "Hello World")
  (POST "/slack"
    [token team_id team_domain channel_id channel_name timestamp user_id user_name text trigger_word :as request]
    (-> request :params :text tokenize-message parse-tokens process-request))
  (route/not-found "Not Found"))

(def api
  (wrap-defaults api-routes api-defaults))

(defn process-request [params]
  (json/generate-string {:response params}))

(defn -main [& args]
  (run-server (site #'api) {:port 3000}))
