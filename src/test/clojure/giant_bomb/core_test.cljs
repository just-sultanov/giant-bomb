(ns giant-bomb.core-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [giant-bomb.core :as sut]))


(deftest square-test
  (testing "dummy test"
    (is (= 4 (sut/square 2)))))
