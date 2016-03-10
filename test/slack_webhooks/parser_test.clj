(ns slack-webhooks.parser-test
  (:require [clojure.test :refer :all]
            [slack-webhooks.parser :refer :all]
            [slack-webhooks.helpers :refer :all]))

(deftest test-in?
  (testing "Returns if an element is or is not in a seq"
    (let [success-seq '("element" "etc")
          failure-seq '("nothing" "here")]
      (is (true? (in? success-seq "element")))
      (is (not (in? failure-seq "element")))))
  (testing "Works on empty seq"
    (is (not (in? '() "element")))))


(deftest test-tokenize-message
  (testing "Breaks a string into lowercase tokens"
    (let [test-string "hey @recruiters check out XYZ"]
      (is (= '("hey" "@recruiters" "check" "out" "xyz" (tokenize-message test-string)))))))

(deftest test-at-message
  (testing "Matches on @message"
    (let [success-string "@recruiters"
          failure-string "recruiters"]
      (is (at-message? success-string))
      (is (not (at-message? failure-string))))))

(deftest test-recipients
  (testing "Returns any @messages in list of tokens"
    (let [test-tokens '("hey" "@recruiters" "check" "out" "XYZ")]
      (is (= '("@recruiters") (get-recipients test-tokens)))
      (is (empty? (get-recipients '("no" "message" "here"))))))
  (testing "Works on empty list"
    (is (empty? (get-recipients '())))))