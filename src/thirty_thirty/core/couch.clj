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

(defn request*
  [response]
  (-> response
      read-json-response))

(defn request
  [opts]
  (request* (http-request opts)))

; (defn- normalize-url
;   "If not present, appends a / to the url-string."
;   [url]
;   (if-not (= (last url) \/)
;     (str url \/ )
;     url))

; (defn database-list
;   [db-url]
;   (:json (couch-request {:url    (str (normalize-url db-url))
;                     :method :get})))

; (defn database-info
;   [db-url db-name]
;   )

; (defn database-create
;   [db-url db-name]
;   )

; (defn database-delete
;   [db-url db-name]
;   )

; (defn database-compact
;   [db-url db-name]
;   )
