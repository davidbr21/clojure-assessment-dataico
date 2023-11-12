(ns problem_3_spec
  (:require [clojure.test :refer :all]
            [invoice-item :as ii]))

; Test 1: Basic test with no discount
(deftest test-subtotal-no-discount
  (is (= 100 (ii/subtotal {:precise-quantity 2 :precise-price 50}))))

; Test 2: Basic test with discount
(deftest test-subtotal-with-discount
  (is (= 90 (ii/subtotal {:precise-quantity 2 :precise-price 50 :discount-rate 10}))))

; Test 3: Test with zero quantity
(deftest test-subtotal-zero-quantity
  (is (= 0 (ii/subtotal {:precise-quantity 0 :precise-price 50 :discount-rate 10}))))

; Test 4: Test with zero price
(deftest test-subtotal-zero-price
  (is (= 0 (ii/subtotal {:precise-quantity 2 :precise-price 0 :discount-rate 10}))))

; Test 5: Test with negative quantity
(deftest test-subtotal-negative-quantity
  (is (= 0 (ii/subtotal {:precise-quantity -2 :precise-price 50 :discount-rate 10}))))