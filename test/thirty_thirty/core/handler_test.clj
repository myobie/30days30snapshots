(ns thirty-thirty.core.handler-test)
;   (:require [clojure.test :refer :all]
;             [ring.mock.request :as mock]
;             [thirty-thirty.core.handler :refer :all]))

; (deftest test-app
;   (testing "main route"
;     (let [response (app (mock/request :get "/"))]
;       (is (= (:status response) 200))))

;   (testing "not-found route"
;     (let [response (app (mock/request :get "/invalid"))]
;       (is (= (:status response) 404)))))
