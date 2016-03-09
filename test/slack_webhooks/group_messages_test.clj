(ns slack-webhooks.group-messages-test
  (:require [clojure.test :refer :all]
            [slack-webhooks.group-messages :refer :all]))

(deftest test-group-exists?
  (testing "works as expected"
    (is (= (group-exists? "test") true))))

(deftest test-valid-sender?
  (testing "works as expected"
    (is (true? (privileged-sender? "ledif" '(#{"ledif" "not_ledif"}))))))

(deftest test-validation
  (testing "work as expected"
    (let [pred "test"
          coll '(#{"test" "stuff"} #{"more" "things"})]
      (is (some true? (map #(contains? % pred) coll))))))

(deftest test-get-group-members
  (testing "returns group members"
    ))
