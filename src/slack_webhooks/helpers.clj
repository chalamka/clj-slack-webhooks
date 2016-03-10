(ns slack-webhooks.helpers)

(defn in?
  "true if seq contains elm"
  [seq elm]
  (some #(= elm %) seq))

(defn printable-seq
  "creates a printable string from a seq"
  [seq]
  (loop [to-print seq
         string ""]
    (if (empty? (rest to-print))
      (str string (first to-print))
      (recur (rest to-print) (str string (first to-print) " ")))))