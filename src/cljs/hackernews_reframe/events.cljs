(ns hackernews-reframe.events
  (:require
    [re-frame.core :as re-frame]
    [hackernews-reframe.db :as db]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [re-graph.core :as re-graph]
    [hackernews-reframe.subs :as subs]
    [hackernews-reframe.graphql :as graph]))

(defn- add-local-storage [key value]
  (.setItem js/localStorage key value))

(defn- remove-local-storage [key]
  (.removeItem js/localStorage key))

(defn- get-local-storage [key]
  (.getItem js/localStorage key))

(def re-graph-init {:ws-url          nil
                    :http-url        "http://localhost:8080/graphql"
                    :http-parameters {:with-credentials? false}})

(re-frame/dispatch [::re-graph/init re-graph-init])

(re-frame/reg-event-db
  ::update-re-graph
  (fn-traced [db [_ token]]
             (assoc-in db [:re-graph :re-graph.internals/default :http-parameters :headers] {"Authorization" token})))

(re-frame/reg-fx
  :set-local-store
  (fn [array]
    (let [keys (first array)]
      (add-local-storage "token" (:token keys))
      (add-local-storage "refresh-token" (:refresh keys)))))

(re-frame/reg-fx
  :dispatch-panel
  (fn [panel]
    (re-frame/dispatch [::set-active-panel panel])))

(re-frame/reg-fx
  :remove-local-store
  (fn []
    (remove-local-storage "token")
    (remove-local-storage "refresh-token")))

(re-frame/reg-event-db
  ::initialize-db
  (fn-traced [_ _]
             db/default-db))

(re-frame/reg-event-db
  ::set-active-panel
  (fn-traced [db [_ active-panel]]
             (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
  ::change-email
  (fn-traced [db [_ email]]
             (assoc db :email email)))

(re-frame/reg-event-db
  ::change-new-email
  (fn-traced [db [_ email]]
             (assoc db :new-email email)))

(re-frame/reg-event-db
  ::change-usr
  (fn-traced [db [_ email]]
             (assoc db :username email)))

(re-frame/reg-event-db
  ::change-new-usr
  (fn-traced [db [_ email]]
             (assoc db :new-usr email)))

(re-frame/reg-event-db
  ::change-pwd
  (fn-traced [db [_ pwd]]
             (assoc db :pwd pwd)))

(re-frame/reg-event-db
  ::change-new-pwd
  (fn-traced [db [_ pwd]]
             (assoc db :new-pwd pwd)))

(re-frame/reg-event-db
  ::change-new-pwd-conf
  (fn-traced [db [_ pwd]]
             (assoc db :pwd-new-conf pwd)))

(re-frame/reg-event-db
  ::change-new-title
  (fn-traced [db [_ title]]
             (assoc db :new-title title)))

(re-frame/reg-event-db
  ::change-new-url
  (fn-traced [db [_ url]]
             (assoc db :new-url url)))

(re-frame/reg-event-fx
  ::login-result
  (fn [{db :db} [_ response]]
    (let [login (get-in response [:data :login] nil)
          error (:error login)
          refresh (:refresh login)
          token (:token login)
          username (get-in login [:user :name] nil)
          rmap {:db              (-> db
                                     (assoc :email nil)
                                     (assoc :pwd nil)
                                     (assoc :loading? false)
                                     (assoc :login-error? error)
                                     (assoc :username username))
                :dispatch        [::update-re-graph token]
                :set-local-store [{:token token :refresh refresh}]}]
      (if (and (nil? error) (not (nil? token)))
        (merge rmap {:dispatch-panel :news-panel})
        rmap
        ))))

(re-frame/reg-event-fx
  ::signup-result
  (fn [{db :db} [_ response]]
    (let [login (get-in response [:data :signup] nil)
          error (:error login)
          refresh (:refresh login)
          token (:token login)
          username (get-in login [:user :name] nil)
          rmap {:db              (-> db
                                     (assoc :email nil)
                                     (assoc :pwd nil)
                                     (assoc :loading? false)
                                     (assoc :new-pwd nil)
                                     (assoc :new-email nil)
                                     (assoc :new-usr nil)
                                     (assoc :pwd-new-conf nil)
                                     (assoc :signup-error? error)
                                     (assoc :username username))
                :dispatch        [::update-re-graph token]
                :set-local-store [{:token token :refresh refresh}]}]
      (if (and (nil? error) (not (nil? token)))
        (merge rmap {:dispatch-panel :news-panel})
        rmap
        ))))

(re-frame/reg-event-fx
  ::login
  (fn [{db :db} _]
    (let [email @(re-frame/subscribe [::subs/email])
          pwd @(re-frame/subscribe [::subs/pwd])]
      {:dispatch [::re-graph/mutate
                  graph/login
                  {:email    email
                   :password pwd}
                  [::login-result]]
       :db       (-> db
                     (assoc :loading? true))})))

(re-frame/reg-event-fx
  ::sign
  (fn [{db :db} _]
    (let [email @(re-frame/subscribe [::subs/new-email])
          pwd @(re-frame/subscribe [::subs/new-pwd])
          name @(re-frame/subscribe [::subs/new-usr])]
      {:dispatch [::re-graph/mutate
                  graph/sign
                  {:email    email
                   :password pwd
                   :name     name}
                  [::signup-result]]
       :db       (-> db
                     (assoc :loading? true))})))

(re-frame/reg-event-fx
  ::submit-result
  (fn [{db :db} [_ response]]
    {:db (assoc db :response response)}))

(re-frame/reg-event-fx
  ::submit-post
  (fn [{db :db} _]
    (let [title @(re-frame/subscribe [::subs/new-title])
          url @(re-frame/subscribe [::subs/new-url])]
      {:dispatch [::re-graph/mutate
                  graph/post
                  {:description title
                   :url         url}
                  [::submit-result]]
       :db       (assoc db :loading? true)})))

(re-frame/reg-event-fx
  ::logout
  (fn [{db :db} _]
    {:remove-local-store []
     :dispatch           [::update-re-graph nil]
     :db                 (-> db
                             (assoc :username nil))}))

(re-frame/reg-event-fx
  ::get-news-result
  (fn [{db :db} [_ response]]
    (let [news (get-in response [:data :feed])]
      {:db (assoc db :news-list news)})))

(re-frame/reg-event-fx
  ::get-news
  (fn [{db :db} _]
    (let [current-page @(re-frame/subscribe [::subs/news-page])
          first 30
          skip (* first current-page)]
      {:db       (assoc db :loading-news? true)
       :dispatch [::re-graph/query
                  graph/feed
                  {:first   first
                   :orderby "ASC"
                   :skip    skip}
                  [::get-news-result]]})))

