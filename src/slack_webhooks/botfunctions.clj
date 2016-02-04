(ns slack-webhooks.botfunctions)

(defn foo [& args]
  "message post successful!")

(defn stats [args]
  (let [name (first args)]
    (str "Stats for " name)))
