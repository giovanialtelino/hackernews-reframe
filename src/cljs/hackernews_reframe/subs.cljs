(ns hackernews-reframe.subs
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  ::active-panel
  (fn [db _]
    (:active-panel db)))

(re-frame/reg-sub
  ::email
  (fn [db _]
    (:email db)))

(re-frame/reg-sub
  ::new-email
  (fn [db _]
    (:new-email db)))

(re-frame/reg-sub
  ::pwd
  (fn [db _]
    (:pwd db)))

(re-frame/reg-sub
  ::new-pwd
  (fn [db _]
    (:new-pwd db)))

(re-frame/reg-sub
  ::pwd-new-conf
  (fn [db _]
    (:pwd-new-conf db)))

(re-frame/reg-sub
  ::login-error
  (fn [db _]
    (:login-error? db)))

(re-frame/reg-sub
  ::signup-error
  (fn [db _]
    (:signup-error? db)))

(re-frame/reg-sub
  ::username
  (fn [db _]
    (:username db)))

(re-frame/reg-sub
  ::new-usr
  (fn [db _]
    (:new-usr db)))

(re-frame/reg-sub
  ::new-title
  (fn [db _]
    (:new-title db)))

(re-frame/reg-sub
  ::new-url
  (fn [db _]
    (:new-url db)))

(re-frame/reg-sub
  ::confirm-pwd
  (fn [db _]
    (= (:pwd-new-conf db) (:new-pwd db))))

(re-frame/reg-sub
  ::non-nil-url-subs
  (fn [db _]
    (or (empty? (:new-url db)) (empty? (:new-title db)))))

