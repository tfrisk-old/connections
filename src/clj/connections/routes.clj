(ns connections.routes
  (:use
    compojure.core
    [hiccup.middleware :only (wrap-base-url)])
  (:require
    [compojure.route :as route]
    [compojure.handler :as handler]
    [compojure.response :as response]))

(defn index []
  (ring.util.response/file-response
    "public/index.html" {:root "resources"}))

(defroutes app-routes
  (GET "/" [] (index))
  (route/files "/" {:root "resources/public"})
  (route/not-found "Page not found"))

(def app
  (-> (handler/site app-routes)
      (wrap-base-url)))

