(ns slack-webhooks.parser
  (:require [clojure.string :refer [split lower-case]]
            [slack-webhooks.botfunctions]))

(defn tokenize-message
  "Tokenize a line from slack, removing the first token (trigger word)"
  [message]
  (rest (split (lower-case message) #"\W+")))

(defn parse-tokens
  "Resolve a token -> function and call it with the remaining tokens"
  [tokens]
  (if tokens
    (if-let [bot-fn (ns-resolve 'slack-webhooks.botfunctions (symbol (first tokens)))]
      (if (fn? @bot-fn)
        (bot-fn (rest tokens))
        (str (first tokens) " isn't a valid function."))
      (str "Could not resolve " (first tokens) " as a function."))))
