(ns giant-bomb.helpers.collection-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [giant-bomb.helpers.collection :as sut]))


(deftest index-by-test
  (testing "index by :id"
    (is (= {1 {:id 1 :foo :bar}
            2 {:id 2 :bar :baz}}
           (sut/index-by :id [{:id 1 :foo :bar} {:id 2 :bar :baz}])))))
