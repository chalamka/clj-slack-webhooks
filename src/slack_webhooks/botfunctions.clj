(ns slack-webhooks.botfunctions)

(defn foo [& args]
  "test")

(defn stats [args]
  (let [name (first args)]
    (str "Stats for " name)))
