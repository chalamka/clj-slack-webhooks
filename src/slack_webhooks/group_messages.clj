(ns slack-webhooks.group-messages)

(def all-groups (atom {:test #{"testmember"}}))

(def privileged-groups (atom {:admins #{"ledif"}}))

(defn group-exists?
  "return whether or not a group exists"
  [group]
  (if (get @all-groups (keyword group))
    true
    false))

(defn privileged-sender?
  "return whether or not a sender is a member of a privileged group"
  [sender privileged-groups]
  (some true? (map #(contains? % sender) privileged-groups)))

(defn add-user "add a user or users to a group" [sender group & user])

(defn add-group "add a group with sender as member" [sender group])

(defn raise-group-permission "raise a group to privileged status" [sender group])

(defn lower-group-permission "lower a group to standard status" [sender group])

(defn remove-user "remove a user or users from a group" [sender group & user])

(defn remove-group "remove a group" [sender group])

(defn message-group "send a message to all members of a group" [sender group])

(defn get-group-members "get all the members of a group" [sender group]
  (let [exists (group-exists? group)
        privileged (privileged-sender? sender @privileged-groups)]
    (cond (and exists privileged)
            (-> @all-groups (keyword group) str)
          (true? exists)
            "Not privileged"
          (true? privileged)
            "No such group"
          :default-cond
            "Not privileged and no such group")))

(defn get-member-groups "get groups for sender, user, or users" [sender & users]
  (map key (filter #(-> % val (contains? sender)) @all-groups)))
