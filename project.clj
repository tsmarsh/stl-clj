(defproject stl-collector "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://tailoredshapes.com/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [net.mikera/core.matrix "0.35.0"]
                 [nio "1.0.3"]
                 [prismatic/schema "0.4.3"]]

  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :repositories [["lib" "file:lib"]]
  :plugins [[lein-cloverage "1.0.6"]
            [lein-cljfmt "0.1.12"]])
