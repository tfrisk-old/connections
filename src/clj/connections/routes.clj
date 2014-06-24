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
  (GET "/details/:id/edit" [id] (views/details-edit-page id))
  (POST "/details/:id/edit" [id & params] (views/details-edit-page-post id params))
  (GET "/paths/:id1/:id2" [id1 id2] (views/paths-view id1 id2))
  (GET "/persons" [] (views/list-all-persons))
  (GET "/organizations" [] (views/list-all-organizations))
  (POST "/search" {params :params} (views/search-page params))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site app-routes)
      (wrap-base-url)))

