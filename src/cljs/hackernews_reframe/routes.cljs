(ns hackernews-reframe.routes
  (:require-macros [secretary.core :refer [defroute]])
  (:import [goog History]
           [goog.history EventType])
  (:require
    [secretary.core :as secretary]
    [goog.events :as gevents]
    [re-frame.core :as re-frame]
    [hackernews-reframe.events :as events]
    ))

(defn hook-browser-navigation! []
  (doto (History.)
    (gevents/listen
      EventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  ;; --------------------
  ;; define routes here
  (defroute "/" []
            (re-frame/dispatch [::events/set-active-panel :news-panel])
            (re-frame/dispatch [::events/get-news])
            )

  (defroute "/login" []
            (re-frame/dispatch [::events/set-active-panel :login-panel])
            )

  (defroute "/sign" []
            (re-frame/dispatch [::events/set-active-panel :sign-panel])
            )

  (defroute "/submit" []
            (re-frame/dispatch [::events/set-active-panel :post-panel])
            )

  (defroute "/past" []
            (re-frame/dispatch [::events/set-active-panel :past-panel]))

  (defroute hn-comment "/comment/:father" [main-father]
            (re-frame/dispatch-sync [::events/update-comment-main-father main-father])
            (re-frame/dispatch [::events/get-father-comments main-father])
            (re-frame/dispatch [::events/set-active-panel :comment-panel]))

  (defroute "/user" []
            (re-frame/dispatch [::events/set-active-panel :user-panel]))

  (defroute hn-user "/hn-user/:name" [name]
            (re-frame/dispatch-sync [::events/clean-user-info])
            (re-frame/dispatch [::events/get-user-info-by-name name])
            (re-frame/dispatch [::events/set-active-panel :generic-user-panel]))

  ;; --------------------
  (hook-browser-navigation!))
