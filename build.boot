(set-env!
 :source-paths #{"src"}
 :resource-paths #{"html"}
 :dependencies '[[adzerk/boot-cljs             "1.7.228-1"]
                 [adzerk/boot-reload           "0.4.5"]
                 [adzerk/boot-test             "1.1.0"]
                 [adzerk/boot-cljs-repl        "0.3.0"]
                 [binaryage/devtools           "0.5.2"]
                 [cljsjs/media-stream-recorder "1.2.6-0"]
                 [compojure                    "1.4.0"]
                 [com.cemerick/piggieback      "0.2.1"]
                 [datascript                   "0.15.0"]
                 [hiccup                       "1.0.5"]
                 [http-kit                     "2.1.18"]
                 [org.clojure/clojure          "1.8.0"]
                 [org.clojure/clojurescript    "1.7.228"]
                 [org.clojure/core.async       "0.2.374"]
                 [org.clojure/test.check       "0.9.0"]
                 [org.clojure/tools.nrepl      "0.2.12"]
                 [org.omcljs/om                "1.0.0-alpha23"]
                 [pandeiro/boot-http           "0.7.2"]
                 [ring                         "1.4.0-RC2"]
                 [weasel                       "0.7.0"]])

(require '[adzerk.boot-cljs      :refer [cljs]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         '[adzerk.boot-reload    :refer [reload]]
         '[adzerk.boot-test      :refer [test]]
         '[pandeiro.boot-http    :refer [serve]])

(task-options!
 serve {:handler 'vidioting.server/handler
        :httpkit true
        :port 8080
        :resource-root "target"})

(deftask autotest
  "Runs test automatically on file change"
  []
  (comp
   (test)
   (speak)
   (watch)))

(deftask development
  "Launch Immediate Feedback Development Environment"
  []
  (comp
   (serve :reload true)
   (reload)
   (watch)
   (cljs-repl)
   (cljs)
   (target :dir #{"target"})))

(deftask build
  "Builds cljs and code for production"
  []
  (comp
     (cljs :optimizations :advanced)
     (target :dir #{"target"})))

(deftask production
  "Runs server in production mode"
  []
  (comp
   (serve :reload false)
   (wait)))
