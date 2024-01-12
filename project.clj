(defproject timekeeper "0.1.0-SNAPSHOT"
  :description "A multiple calendars sync API."
  :url "https://github.com/matheusmassa1/timekeeper"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [aero "1.1.6"]
                 [com.github.seancorfield/next.jdbc "1.3.909"]
                 [org.postgresql/postgresql "42.6.0"]
                 [com.github.seancorfield/honeysql "2.5.1103"]
                 [happygapi "0.4.8"]
                 [ring/ring-core "1.11.0"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-jetty-adapter "1.11.0"]
                 [ring/ring-devel "1.11.0"]
                 [cheshire "5.10.0"]
                 [compojure "1.7.0"]])
