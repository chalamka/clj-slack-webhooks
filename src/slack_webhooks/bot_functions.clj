(ns slack-webhooks.bot_functions)

(defn foo [& args]
  "message post successful!")

(defn stats [args]
  (let [name (first args)]
    (str "Stats for " name)))

(defn commands [args]
  (keys (ns-interns 'slack-webhooks.bot_functions)))
