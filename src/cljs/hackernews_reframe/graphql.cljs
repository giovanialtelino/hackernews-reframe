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

(def get-comment
  "comment($id:String!){
  comment(id:$id){
  child
  father_id
  father_name
  id
  postedBy
  postedSince
  text
  votes
  }}")

(def feed
  "feed($first:Int, $orderBy:String, $skip:Int){
  feed(first:$first, orderby:$orderBy, skip:$skip){
  comments
  createdAt
  description
  error
  id
  order
  postedBy
  url
  votes
  }}")

(def get-link
  "link($id:String!){
  link(id:$id){
   comments
   createdAt
   description
   error
   id
   order
   postedBy
   url
   votes
   commentList{
    child
    father_id
    father_name
    id
    postedBy
    postedSince
    text
    votes }}")

(def post
  "post($description:String!, $url:String!){
  post(description:$description, url:$url){
    comments
    createdAt
    description
    error
    id
    order
    postedBy
    url
    votes  }}"
  )

