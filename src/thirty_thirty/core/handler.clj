(ns thirty-thirty.core.handler
  (:use [ring.util.anti-forgery]
        [hiccup.core]
        [hiccup.page])
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [environ.core :refer [env]]
            ; [cemerick.url :refer [url url-encode]]
            [ring.util.response :refer [redirect]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

; (def db-url (url (env :couchdb-url)))
; (def db-name "test1")

; (defn init-db
;   [db-url db-name]
;   (do
;     (couch/database-create db-url db-name)
;     (couch/document-bulk-update db-url db-name [{:photo 1 :tags ["a" "b" "c"]}
;                                                 {:photo 2 :tags ["b" "c" "d"]}
;                                                 {:photo 3 :tags ["e" "f"]}])
;     (let [design-document-name "tagger"
;           view-name            "by-tag"]
;       (couch/view-create
;         db-url db-name design-document-name view-name
;         {:map "function(doc) {
;                 if (doc.tags && doc.tags.length > 0) {
;                   doc.tags.forEach(function(tag) {
;                     emit(tag, 1)
;                   })
;                 }
;               }"
;          :reduce "_sum"})
;       )))
         ; (fn [doc]
         ;   (when-let [tags (:tags doc)]
         ;     (map #(js/emit % 1) tags)))}))))

(defn layout
  [& content]
  (html5
    [:head
     [:meta {:http-equiv "Content-type"
             :content "text/html; charset=utf-8"}]
     [:title "30days30snapshots"]]
    [:body content]))

(defn index-view
  [request]
  (layout [:h1 "Hello World"]))

(defn not-found-view []
  (layout [:h1 "404 Not Found"]))

(defroutes app-routes
  (GET "/" request index-view)
  (route/not-found (not-found-view)))

(def app
  (wrap-defaults app-routes site-defaults))
