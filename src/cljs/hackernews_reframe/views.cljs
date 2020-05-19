(ns hackernews-reframe.views
  (:require
    [re-frame.core :as re-frame]
    [hackernews-reframe.subs :as subs]
    ))

;; news
(defn post-row [title posted-by points comments-count]
  [:div
   [:p title]
   [:p posted-by]
   [:p points]
   [:p comments-count]])

(defn news-panel []
  [:div.container
   [post-row "title" "giovani" 10 20]
   ]
  )

(defn login-panel []
  [:div.container
   [:div.columns.is-centered
    [:div.field.column.is-4
     [:label.label "Username"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type "text" :placeholder "Username"}]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-user]]]]
    [:div.field.column.is-4
     [:label.label "Password"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type "password" :placeholder "Password"}]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-lock]]]]]
   [:div.columns.is-centered
    [:div.field.is-grouped
     [:div.control
      [:button.button.is-success "Login"]]
     [:div.control
      [:button.button.is-light "Forgot Password"]]]]])

(defn post-panel []
  [:div.container
   [:div.field
    [:label.label "Title"]
    [:div.control
     [:input.input {:type "text" :placeholder "Post Title"}]]]
   [:div.field
    [:label.label "URL"]
    [:div.control
     [:input.input {:type "text" :placeholder "Article HTTPS URL"}]]]
   [:div.columns.is-centered
    [:p "OR"]]
   [:div.field
    [:label.label "Text"]
    [:div.control
     [:textarea.textarea]]]
   [:div.field
    [:div.control
     [:button.button.is-success "Submit"]]]])

(defn past-panel []
  )

(defn sign-panel []
  [:div.container
   [:div.columns.is-centered
    [:div.field.column.is-4
     [:label.label "Username"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type "text" :placeholder "Username"}]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-user]]]]
    [:div.field.column.is-4
     [:label.label "Email"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type "text" :placeholder "Username"}]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-envelope]]]]]
   [:div.columns.is-centered
    [:div.field.column.is-4
     [:label.label "Password"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type "password" :placeholder "Password"}]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-lock]]]]
    [:div.field.column.is-4
     [:label.label "Confirm Password"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type "password" :placeholder "Password"}]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-lock]]]]]
   [:div.columns.is-centered
    [:div.field.column.is-1
     [:div.control
      [:button.button.is-success.login-btn "Create User"]]]]])

(defn comment-panel [])

(defn user-panel [])

;; main

(defn- panels [panel-name]
  (case panel-name
    :news-panel [news-panel]
    :login-panel [login-panel]
    :post-panel [post-panel]
    :past-panel [past-panel]
    :sign-panel [sign-panel]
    :comment-panel [comment-panel]
    :user-panel [user-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  [:div.container-fluid
   [:nav#main-nav.navbar.item-a {:role "navigation" :aria-label "main navigation"}
    [:div.navbar-brand
     [:a.navbar-item {:href "#/"}
      [:i#h-n.fab.fa-hacker-news]]
     [:a.navbar-burger.burger {:role "button" :aria-label "menu" :aria-expanded "false" :data-target "navbarBasicExample"}
      [:span {:aria-hidden "true"}]
      [:span {:aria-hidden "true"}]
      [:span {:aria-hidden "true"}]]]
    [:div#navbarBasicExample.navbar-menu
     [:div.navbar-start
      [:a.navbar-item {:href "#/"} "News"]
      [:a.navbar-item {:href "#/past"} "Past"]
      [:a.navbar-item {:href "#/submit"} "Submit"]]
     [:div.navbar-end
      [:div.navbar-item
       [:div.buttons
        [:a.button {:href "#/sign"} "Sign up"]
        [:a.button {:href "#/login"} "Log in"]]]]]]
   (let [active-panel (re-frame/subscribe [::subs/active-panel])]
     [show-panel @active-panel])
   [:footer.footer
    [:div.content.has-text-centered
     [:p "This is a Hacker News homage with Lacinia Pedestal and Re-Frame."]
     [:p "For info check the Github Project, front-end and back-end."]]]])

