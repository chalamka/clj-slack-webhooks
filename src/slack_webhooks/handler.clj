(ns slack-webhooks.handler
  (:require [slack-webhooks.parser :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.handler :refer [site]]
            [clojure.string :only [split] :as string]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [org.httpkit.server :refer [run-server]]
            [cheshire.core :as json]
            [clojure.java.io :refer [input-stream]]))

(def api-token (atom {}))

(defn load-conf [filepath]
  (let [f (slurp filepath)
        conf (json/parse-string f true)]
    (reset! api-token (:api-token conf))))

(defn process-request [params]
  (json/generate-string {:text params}))

(defn verify-token [token]
  (if (= @api-token token)
    true
    false))

(defroutes api-routes
  (GET "/" []
    "Hello World")
  (POST "/slack"
    [token team_id team_domain channel_id channel_name timestamp user_id user_name text trigger_word :as request]
    (if (verify-token token)
      (-> request :params :text tokenize-message resolve-tokens process-request)
      nil))
  (route/not-found "Not Found"))

(def api
  (wrap-defaults api-routes api-defaults))

(defn -main [& args]
  (do
    (println "loading config")
    (load-conf "config.json")
    (println "starting server")
    (run-server (site #'api) {:port 3000})))