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

  (defroute "/comment" []
            (re-frame/dispatch [::events/set-active-panel :comment-panel]))

  (defroute "/user" []
            (re-frame/dispatch [::events/set-active-panel :user-panel]))

  ;; --------------------
  (hook-browser-navigation!))
