(ns vidioting.server
  (:require
   [compojure.core :refer [defroutes GET]]
   [compojure.route :refer [not-found files resources]]
   [hiccup.page :refer [html5 include-css]]
   [org.httpkit.server :refer :all]
   [ring.middleware.reload :as reload]
   [ring.util.response :as response]))

(defn include-scripts [& urls]
  (for [url urls]
    [:script {:src url :type "text/javascript"}]))

(def index
  (html5
   [:head
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0,
                      maximum-scale=1.0, user-scalable=no"}]
    (include-css
     "https://fonts.googleapis.com/icon?family=Material+Icons"
     "css/materialize.min.css"
     "css/style.css")]
   [:body {:id "body"}
    [:div {:id "app"}]
    (include-scripts
     "js/jquery-2.1.4.min.js"
     "js/materialize.min.js"
     "js/main.js")]))

(defroutes static-handler
  (GET "/" [] index)
  (files "/" {:root "target"})
  (resources "/" {:root "target"})
  (not-found "Page Not Found"))

(defn handler [ring-request]
  (with-channel ring-request channel
    (if (websocket? channel)
      (on-receive channel (fn [data] (send! channel data)))
      (send! channel (static-handler ring-request)))))
