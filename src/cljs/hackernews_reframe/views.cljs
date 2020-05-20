(ns hackernews-reframe.views
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [hackernews-reframe.subs :as subs]
    [hackernews-reframe.events :as events]
    [clojure.string :as str]
    ))

;; news
(defn post-row [post-id title posted-by points comments-count]
  [:article.media
   [:figure.media-left
    [:a.like-dislike [:i.fas.fa-arrow-up]]
    [:a.like-dislike [:i.fas.fa-arrow-down]]]
   [:div.media-content
    [:div#small-content.content
     [:p
      [:small " " points " points by "] [:a posted-by]
      [:br] [:strong title]]]
    [:nav.level.is-mobile
     [:div.level-left
      [:a.level-item
       [:span [:small "share"]]]
      [:a.level-item
       [:span [:small "hide"]]]
      [:a.level-item
       [:span [:small " " comments-count " comments"]]]]]]])

(defn news-panel []
  [:div.container-fluid
   [post-row "1" "title of the article whatever or yolo" "giovani" 10 20]
   [post-row "2" "title of the article whatever or yolo" "giovani" 10 20]
   [post-row "3" "title of the article whatever or yolo" "giovani" 10 20]
   [post-row "4" "title of the article whatever or yolo" "giovani" 10 20]])

(defn login-panel []
  [:div.container
   [:div.columns.is-centered
    [:div.field.column.is-4
     [:label.label "Username"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type        "text"
                      :placeholder "Username"
                      :value       @(re-frame/subscribe [::subs/email])
                      :on-change   #(re-frame/dispatch [::events/change-email (-> % .-target .-value)])}]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-user]]]]
    [:div.field.column.is-4
     [:label.label "Password"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type        "password"
                      :placeholder "Password"
                      :value       @(re-frame/subscribe [::subs/pwd])
                      :on-change   #(re-frame/dispatch [::events/change-pwd (-> % .-target .-value)])}]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-lock]]]]]
   [:div.columns.is-centered
    [:div.field.is-grouped
     [:div.control
      [:button.button.is-success
       {:on-click #(re-frame/dispatch [::events/login])} "Login"]]
     [:div.control
      [:button.button.is-light "Forgot Password"]]]]
   (let [login-error @(re-frame/subscribe [::subs/login-error])]
     (if-not (nil? login-error)
       [:div.columns.is-centered.space-left [:span [:strong login-error]]]))])

(defn post-panel []
  [:div.container
   [:div.field
    [:label.label "Title"]
    [:div.control
     [:input.input {:type        "text"
                    :placeholder "Post Title"
                    :value       @(re-frame/subscribe [::subs/new-title])
                    :on-change   #(re-frame/dispatch [::events/change-new-title (-> % .-target .-value)])}]]]
   [:div.field
    [:label.label "URL"]
    [:div.control
     [:input.input {:type        "text"
                    :placeholder "Article HTTPS URL"
                    :value       @(re-frame/subscribe [::subs/new-url])
                    :on-change   #(re-frame/dispatch [::events/change-new-url (-> % .-target .-value)])}]]]
   [:div.field
    [:div.control
     (let [non-nil @(re-frame/subscribe [::subs/non-nil-url-subs])
           usr (nil? @(re-frame/subscribe [::subs/username]))]
       (if (or non-nil usr)
         [:button.button.is-danger "Submit"]
         [:button.button.is-success
          {:on-click #(re-frame/dispatch [::events/submit-post])}
          "Submit"]))]]])

;go to date or search by keyword, show only 30 itens, but allow it to go further
(defn past-panel []
  [:div.container-fluid
   [:div.columns.is-centered.space-left
    [:div.field.column.is-3
     [:label.label "Date"]
     [:div.field.has-addons
      [:p.control.has-icons-left
       [:div.control
        [:input.input {:type "date"}]]
       [:span.icon.is-small.is-left
        [:i.fas.fa-calendar]]]
      [:div.control
       [:a.button.is-info "Search"]]]]
    [:div.field.column.is-3
     [:label.label "Keyword"]
     [:div.field.has-addons
      [:p.control.has-icons-left
       [:div.control
        [:input.input {:type "text" :placeholder "Search Keyword"}]]
       [:span.icon.is-small.is-left
        [:i.fas.fa-search]]]
      [:div.control
       [:a.button.is-info "Search"]]]]]])

(defn sign-panel []
  [:div.container
   [:div.columns.is-centered
    [:div.field.column.is-4
     [:label.label "Username"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type        "text"
                      :placeholder "Username"
                      :value       @(re-frame/subscribe [::subs/new-usr])
                      :on-change   #(re-frame/dispatch [::events/change-new-usr (-> % .-target .-value)])
                      }]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-user]]]]
    [:div.field.column.is-4
     [:label.label "Email"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type        "text"
                      :placeholder "Email"
                      :value       @(re-frame/subscribe [::subs/new-email])
                      :on-change   #(re-frame/dispatch [::events/change-new-email (-> % .-target .-value)])
                      }]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-envelope]]]]]
   [:div.columns.is-centered
    [:div.field.column.is-4
     [:label.label "Password"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type        "password"
                      :placeholder "Password"
                      :value       @(re-frame/subscribe [::subs/new-pwd])
                      :on-change   #(re-frame/dispatch [::events/change-new-pwd (-> % .-target .-value)])
                      }]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-lock]]]]
    [:div.field.column.is-4
     [:label.label "Confirm Password"]
     [:p.control.has-icons-left
      [:div.control
       [:input.input {:type        "password"
                      :placeholder "Password"
                      :value       @(re-frame/subscribe [::subs/pwd-new-conf])
                      :on-change   #(re-frame/dispatch [::events/change-new-pwd-conf (-> % .-target .-value)])}]]
      [:span.icon.is-small.is-left
       [:i.fas.fa-lock]]]]]
   [:div.columns.is-centered
    [:div.field.column.is-1
     [:div.control
      (let [equal @(re-frame/subscribe [::subs/confirm-pwd])
            not-nil (nil? @(re-frame/subscribe [::subs/new-pwd]))]
        (if (and equal (not not-nil))
          [:button.button.is-success.login-btn
           {:on-click #(re-frame/dispatch [::events/sign])}
           "Create User"]
          [:button.button.is-danger.login-btn "Create User"]
          ))]]]

   (let [login-error @(re-frame/subscribe [::subs/signup-error])]
     (if-not (nil? login-error)
       [:div.columns.is-centered.space-left [:span [:strong login-error]]]))])

(defn comment-panel [])

(defn user-panel []
  [:div.container-fluid

   [:div.columns.space-left
    [:div.column.is-2.column-text
     [:label.label "Username"]]
    [:div.column.is-3.column-text
     [:input.input {:type "text" :readOnly true}]]]

   [:div.columns.space-left
    [:div.column.is-2.column-text
     [:label.label "E-Mail"]]
    [:div.column.is-3.column-text
     [:input.input {:type "text" :readOnly true}]]]

   [:div.columns.space-left
    [:div.column.is-2.column-text
     [:label.label "Created At"]]
    [:div.column.is-2.column-text
     [:input.input {:type "text" :readOnly true}]]]

   [:div.columns.space-left
    [:div.column.is-2.column-text
     [:label.label "Karma"]]
    [:div.column.is-2.column-text
     [:input.input {:type "text" :readOnly true}]]]

   [:div.columns.is-centered.space-left [:div.column.is-2 [:button.button [:small "Posts"]]] [:div.column.is-2 [:button.button [:small "Comments"]]]]]
  )

;; main
[:div.columns.space-left
 [:div.column.is-2.column-text
  [:label.label "About You"]]
 [:div.column.is-6
  [:textarea.textarea]]]

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
       (let [is-logged-in @(re-frame/subscribe [::subs/username])]
         (if (nil? is-logged-in)
           [:div.buttons
            [:a.button {:href "#/sign"} "Sign up"]
            [:a.button {:href "#/login"} "Log in"]]
           [:div.buttons
            [:a.navbar-item {:href "#/user"} is-logged-in]
            [:a.navbar-item {:href     "#/"
                             :on-click #(re-frame/dispatch [::events/logout])} "logout"]]))]]]]
   (let [active-panel (re-frame/subscribe [::subs/active-panel])]
     [show-panel @active-panel])
   [:footer.footer.is-fixed-bottom
    [:div.content.has-text-centered
     [:p "This is a Hacker News homage with Lacinia Pedestal and Re-Frame."]
     [:p "For info check the Github Project, " [:a {:target "_blank" :href "https://github.com/giovanialtelino/hackernews-reframe"} "front-end"] " and " [:a {:target "_blank" :href "https://github.com/giovanialtelino/hackernews-lacinia-datomic"} "back-end"] "."]]]])