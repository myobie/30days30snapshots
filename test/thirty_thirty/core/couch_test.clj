(ns thirty-thirty.core.couch-test
  (:use midje.sweet)
  (:require [thirty-thirty.core.couch :as    couch]
            [environ.core             :refer [env]]))

(def db-url (env :couchdb-url))

(fact "db-url is here"
      (nil? db-url) => false)

(facts "about `http-request`"
       (fact "it will perform a request"
             (let [response (couch/http-request {:method :get :url "http://nathanherald.com/"})
                   body (:body response)]
               (nil? body) => false)))

(facts "about `request`"
       (defn do-successful-request []
         (couch/request {:method :get :url db-url}))
       (let [response (do-successful-request)]
         (fact "it will call to couch"
               (:status response) => 200)
         (fact "it will parse json automatically"
               (let [json (:json response)]
                 (:couchdb json) => "Welcome")))

       (defn do-missing-request []
         (couch/request {:method :get :url (str db-url "missing")}))
       (fact "it will not raise"
             (do-missing-request) => (throws)))

