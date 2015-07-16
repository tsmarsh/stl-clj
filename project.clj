(defproject stl-collector "0.2.0"
  :description "Library for collecting multiple STL files into a single STL file"
  :url "http://tailoredshapes.com/"
  :license {:name "BSD"
            :url "http://opensource.org/licenses/BSD-2-Clause"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [net.mikera/core.matrix "0.35.0"]
                 [nio "1.0.3"]
                 [prismatic/schema "0.4.3"]
                 [packager "0.1.0"]]

  :aot [stl-collector.core]
  :plugins [[lein-cloverage "1.0.6"]
            [lein-cljfmt "0.1.12"]])
