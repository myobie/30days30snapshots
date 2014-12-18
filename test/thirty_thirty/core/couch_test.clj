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
       (fact "it will raise"
             (do-missing-request) => (throws)))

(def db-name "test-database-create")

(try (couch/database-delete db-name)
  (catch Exception e))

(facts "about `databases`"
       (defn do-request []
         (couch/databases db-url))
       (fact "it doesn't raise"
             (do-request) =not=> (throws))
       (let [response (do-request)
             length   (count response)]
         (fact "it will return some databases"
               (compare length 1) => 1)))

(facts "about `database-create`"
       (defn do-request []
         (couch/database-create db-url db-name))
       (let [response (do-request)]
         (fact "it was successful"
               (:ok response) => truthy))
       (couch/database-delete db-url db-name))

(facts "about `database-delete`"
       (couch/database-create db-url db-name)
       (defn do-request []
         (couch/database-delete db-url db-name))
       (let [response (do-request)]
         (fact "it was successful"
               (:ok response) => truthy)))
