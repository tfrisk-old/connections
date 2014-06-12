(ns connections.routes
  (:use
    compojure.core
    [connections.web :as web]
    [hiccup.middleware :only (wrap-base-url)])
  (:require
    [compojure.route :as route]
    [compojure.handler :as handler]
    [compojure.response :as response]))

(defroutes app-routes
  (GET "/" [] (web/index-page))
  (GET "/details/:id" [id] (web/details-page id))
  (POST "/search" {params :params} (web/search-page params))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site app-routes)
      (wrap-base-url)))

