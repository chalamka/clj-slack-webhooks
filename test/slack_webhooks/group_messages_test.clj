(ns slack-webhooks.group-messages-test
  (:require [clojure.test :refer :all]
            [slack-webhooks.group-messages :refer :all]))

(def test-all-groups-atom (atom {:test1 #{"testmember1" "2" "3" "4"}
                                 :test2 #{"testmember1" "2"}
                                 :test3 #{"ledif"}}))

(def test-privileged-groups-atom (atom {:admins #{"ledif"}}))

(deftest test-group-exists?
  (testing "works as expected"
    (is (= (group-exists? "test1" test-all-groups-atom) true))))

(deftest test-privileged-sender?
  (testing "works as expected"
    (is (true? (privileged-sender? "ledif" test-privileged-groups-atom)))))

(deftest test-user-in-group?
  (testing "returns true if user is in group"
    (is (true? (user-in-group? "ledif" "test3" test-all-groups-atom))))
  (testing "returns false if user is not in group"
    (is (nil? (user-in-group? "notLedif" "test3" test-all-groups-atom)))))

(deftest test-validation
  (testing "work as expected"
    (let [pred "test"
          coll '(#{"test" "stuff"} #{"more" "things"})]
      (is (some true? (map #(contains? % pred) coll))))))

(deftest test-add-group
  (testing "adds a group to atom with sender as member"
    (let [new-dict {:test1 #{"testmember1" "2" "3" "4"}
                    :test2 #{"testmember1" "2"}
                    :test3 #{"ledif"}
                    :new-group #{"ledif"}}]
      (is (= new-dict (add-group "ledif" "new-group" test-all-groups-atom))))))

(deftest test-get-group-members
  (testing "returns group members"
    (is (= "ledif" (get-group-members "ledif" "test3" test-all-groups-atom test-privileged-groups-atom))))
  (testing "fails if sender is not privileged"
    (is (= "Not privileged" (get-group-members "testmember1" "test1" test-all-groups-atom test-privileged-groups-atom))))
  (testing "fails if group does not exist"
    (is (= "No such group" (get-group-members "ledif" "fail" test-all-groups-atom test-privileged-groups-atom)))))
