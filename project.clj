(defproject timekeeper "0.1.0-SNAPSHOT"
  :description "A multiple calendars sync API."
  :url "https://github.com/matheusmassa1/timekeeper"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [aero "1.1.6"]
                 [com.novemberain/monger "3.6.0"]
                 [clojure.java-time "1.4.2"]
                 [happygapi "0.4.10"]
                 [ring/ring-core "1.11.0"]
                 [ring/ring-devel "1.11.0"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-jetty-adapter "1.11.0"]
                 [ring-session-memcached "0.0.1"]
                 [cheshire "5.10.0"]
                 [compojure "1.7.0"]
                 [buddy/buddy-sign "3.5.351"]
                 [buddy/buddy-core "1.12.0-430"]
                 [buddy/buddy-auth "3.0.1"]])
