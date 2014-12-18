(ns thirty-thirty.core.couch
  (:require [clj-http.core     :as    http-core]
            [clj-http.client   :as    http-client]
            [clojure.data.json :as    json]))

(def http-request
  (-> #'http-core/request
      http-client/wrap-query-params
      http-client/wrap-basic-auth
      http-client/wrap-url
      http-client/wrap-decompression
      http-client/wrap-input-coercion
      http-client/wrap-output-coercion
      http-client/wrap-accept
      http-client/wrap-accept-encoding
      http-client/wrap-content-type
      http-client/wrap-method))

(defn- read-json-response
  [response]
  (try (assoc response
              :json (json/read-str (:body response) :key-fn keyword))
       (catch Exception e
         response)))

(defn- raise
  [msg error response]
  (throw (ex-info msg {:error error :code (:status response) :response response})))

(defn- raise-client-error
  [error response]
  (raise "client error" error response))

(defn- validate-response
  [response]
  (let [status (:status response)]
    (if (>= status 400)

      (case status
        404 (case (:reason response)
              "no_db_file" (raise-client-error "database-not-found" response)
              "Document is missing attachment" (raise-client-error "attachment-not-found" response)
              (raise-client-error "document-not-found" response))
        409 (raise-client-error "resource-conflict" response)
        412 (raise-client-error "precondition-failed" response)
        (raise "internal server error" "server-error" response))

      response)))

(defn request*
  [response]
  (-> response
      read-json-response
      validate-response))

(defn request
  [opts]
  (request* (http-request opts)))

(defn couch-request
  [opts]
  (:json (request opts)))

(defn- normalize-url
  "If not present, appends a / to the url-string."
  [url]
  (if-not (= (last url) \/)
    (str url \/ )
    url))

(defn- database-request
  [db-url db-name method]
  (couch-request {:url    (str (normalize-url db-url) db-name)
                  :method method}))

(defn databases
  [db-url]
  (database-request db-url "_all_dbs" :get))

(defn database
  [db-url db-name]
  (database-request db-url db-name :get))

(defn database-create
  [db-url db-name]
  (database-request db-url db-name :put))

(defn database-delete
  [db-url db-name]
  (database-request db-url db-name :delete))

; (defn database-compact [db-url db-name])
; (defn database-replicate [source-db-url source-db-name target-db-url target-db-name])
