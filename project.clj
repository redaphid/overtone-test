(defproject overtone-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [
    [org.clojure/clojure "1.8.0"]
    [quantisan/overtone "0.10-SNAPSHOT"]
    [clj-time "0.11.0"]
    [net.mikera/vectorz-clj "0.43.1"]
  ]
  :main ^:skip-aot overtone-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
