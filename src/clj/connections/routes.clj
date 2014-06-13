(ns connections.routes
  (:use
    compojure.core
    [connections.views :as views]
    [hiccup.middleware :only (wrap-base-url)])
  (:require
    [compojure.route :as route]
    [compojure.handler :as handler]
    [compojure.response :as response]))

(defroutes app-routes
  (GET "/" [] (views/index-page))
  (GET "/details/:id" [id] (views/details-page id))
  (GET "/persons" [] (views/list-all-persons))
  (GET "/organizations" [] (views/list-all-organizations))
  (POST "/search" {params :params} (views/search-page params))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site app-routes)
      (wrap-base-url)))

