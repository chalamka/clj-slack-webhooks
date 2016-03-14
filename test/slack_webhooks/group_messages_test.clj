(ns slack-webhooks.group-messages-test
  (:require [clojure.test :refer :all]
            [slack-webhooks.group-messages :refer :all]))

(def test-all-groups-atom (atom {:test1 #{"testmember1" "2" "3" "4"}
                                 :test2 #{"testmember1" "2"}
                                 :test3 #{"ledif"}}))

(def test-privileged-groups-atom (atom {:admins #{"ledif"}}))

(deftest test-group-exists?
  (testing "works as expected"
    (is (= (group-exists? "test1" test-all-groups-atom test-privileged-groups-atom) true))))

(deftest test-privileged-sender?
  (testing "works as expected"
    (is (true? (privileged-sender? "ledif" test-privileged-groups-atom)))))

(deftest test-user-in-group?
  (testing "returns true if user is in group"
    (is (true? (user-in-group? "ledif" "test3" test-all-groups-atom test-privileged-groups-atom))))
  (testing "returns false if user is not in group"
    (is (nil? (user-in-group? "notLedif" "test3" test-all-groups-atom test-privileged-groups-atom)))))

(deftest test-add-user
  (let [groups (atom @test-all-groups-atom)
        priv (atom @test-privileged-groups-atom)
        new-dict {:test1 #{"testmember1" "2" "3" "4"}
                  :test2 #{"testmember1" "2"}
                  :test3 #{"ledif" "new-user"}}
        new-priv-dict {:admins #{"ledif" "not-ledif"}}]
    (testing "adds a user to a group if it exists"
      (is (= new-dict (add-user "ledif" "test3" groups priv "new-user"))))
    (testing "adds a user to privileged groups"
      (is (= new-priv-dict (add-user "ledif" "admins" groups priv "not-ledif"))))))

(deftest test-add-group
  (testing "adds a group to atom with sender as member"
    (let [groups (atom @test-all-groups-atom)
          new-dict {:test1 #{"testmember1" "2" "3" "4"}
                    :test2 #{"testmember1" "2"}
                    :test3 #{"ledif"}
                    :new-group #{"ledif"}}]
      (is (= new-dict (add-group "ledif" "new-group" groups))))))

(deftest test-raise-group-permission
    (let [groups (atom @test-all-groups-atom)
          priv (atom @test-privileged-groups-atom)
          new-groups (atom {:test1 #{"testmember1" "2" "3" "4"}
                            :test2 #{"testmember1" "2"}})
          new-priv (atom {:admins #{"ledif"}
                          :test3 #{"ledif"}})]
      (raise-group-permission "ledif" "test3" groups priv)
      (testing "raises a group to privileged"
        (is (true? (and (= @groups @new-groups) (= @priv @new-priv)))))
      (testing "fails if sender lacks privilege"
        (is (= "No such group fail or fail-user not privileged") (raise-group-permission "fail-user" "fail" groups priv)))))

(deftest test-lower-group-permission
  (let [groups (atom @test-all-groups-atom)
        priv (atom {:admins #{"ledif"}
                    :other #{"ledif"}})
        new-groups (atom {:test1 #{"testmember1" "2" "3" "4"}
                          :test2 #{"testmember1" "2"}
                          :test3 #{"ledif"}
                          :other #{"ledif"}})
        new-priv (atom @test-privileged-groups-atom)]
    (testing "lowers a group to unprivileged"
      (lower-group-permission "ledif" "other" groups priv)
      (is (= @new-priv @priv))
      (is (= @new-groups @groups)))))

(deftest test-get-group-members
  (testing "returns group members"
    (is (= "ledif" (get-group-members "ledif" "test3" test-all-groups-atom test-privileged-groups-atom))))
  (testing "returns members of privileged groups"
    (is (= "ledif" (get-group-members "ledif" "admins" test-all-groups-atom test-privileged-groups-atom))))
  (testing "fails if sender is not privileged"
    (is (= "testmember1 is not privileged" (get-group-members "testmember1" "test1" test-all-groups-atom test-privileged-groups-atom))))
  (testing "fails if group does not exist"
    (is (= "No such group: fail" (get-group-members "ledif" "fail" test-all-groups-atom test-privileged-groups-atom)))))
