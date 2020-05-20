(ns hackernews-reframe.graphql)

(def login
  "login($email:String!, $password:String!) {
    login(
      email: $email,
      password: $password
    ) {token
       error
       refresh
       user {name}}}")

(def sign
  "signup($email:String!, $name:String!, $password:String!) {
    signup(email:$email, name:$name, password:$password) {
     token
     refresh
     error
     user {name}}}")

