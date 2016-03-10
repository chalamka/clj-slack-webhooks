(ns slack-webhooks.group-messages
  (:require [slack-webhooks.helpers :refer [in? printable-seq]]))

(def all-groups-atom (atom {:test #{"testmember"}}))

(def privileged-groups-atom (atom {:admins #{"ledif"}}))

(defn group-exists?
  "return whether or not a group exists"
  [group all-groups]
  (if (get @all-groups (keyword group))
    true
    false))

(defn privileged-sender?
  "return whether or not a sender is a member of a privileged group"
  [sender privileged-groups]
  (some true? (map #(contains? % sender) (vals @privileged-groups))))

(defn user-in-group?
  "return whether or not a user is a member of a group"
  [user group all-groups]
  (let [group-key (keyword group)]
    (in? (-> @all-groups group-key) user)))

(defn add-user "add a user or users to a group" [sender group all-groups privileged-groups & user]
  (let [exists (group-exists? group all-groups)
        privileged (privileged-sender? sender privileged-groups)
        group-key (keyword group)]
    (if exists
      (cond
        (and privileged (in? (keys @privileged-groups) group-key))
          (swap! privileged-groups assoc group-key (conj (group-key @privileged-groups) user))
        :default-cond
          (swap! all-groups assoc group-key (conj (group-key @all-groups) user)))
      (str "No such group: " group))))

(defn add-group "add a group with sender as member" [sender group all-groups]
  (swap! all-groups assoc (keyword group) #{sender}))

(defn raise-group-permission "raise a group to privileged status" [sender group])

(defn lower-group-permission "lower a group to standard status" [sender group])

(defn remove-user "remove a user or users from a group" [sender group & user])

(defn remove-group "remove a group" [sender group])

(defn message-group "send a message to all members of a group" [sender group])

(defn get-group-members
  "get all the members of a group"
  [sender group all-groups privileged-groups]
  (let [exists (group-exists? group all-groups)
        privileged (privileged-sender? sender privileged-groups)
        group-key (keyword group)]
    (cond (and exists privileged)
            (-> @all-groups group-key printable-seq)
          (true? exists)
            "Not privileged"
          (true? privileged)
            "No such group"
          :default-cond
            "Not privileged and no such group")))

(defn get-member-groups "get groups for sender, user, or users" [sender all-groups & users]
  (map key (filter #(-> % val (contains? sender)) @all-groups)))