(ns slack-webhooks.bot_functions)

(defn commands [args]
  (str (keys (ns-interns 'slack-webhooks.bot_functions))))


