(ns slack-webhooks.parser
  (:require [clojure.string :refer [split lower-case]]
            [slack-webhooks.bot_functions]))

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn valid-fn?
  "test if token is a fn in botfunctions namespace"
  [token]
  (in? (keys (ns-interns 'slack-webhooks.bot_functions)) (symbol token)))

(defn tokenize-message
  "Tokenize a line from slack"
  [message]
  (split (lower-case message) #" "))

(defn at-message?
  "test if token is an @message"
  [token]
  (re-matches #"@\w+" token))

(defn get-recipients
  "return all @messages from a list of strongs"
  [tokens]
  (filter at-message? tokens))

(defn resolve-tokens
  "Resolve a token -> function and call it with the remaining tokens"
  [tokens]
  (if (valid-fn? (first tokens))
    (let [bot-fn (ns-resolve 'slack-webhooks.bot_functions (symbol (first tokens)))]
      (bot-fn (rest tokens)))
    (str "Could not resolve " (first tokens) " as a function.")))
