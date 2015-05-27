(ns stl-collector.core-test
  (:require [clojure.test :refer :all]
            [stl-collector.core :refer :all]
            [nio.core :as nio]
            [clojure.java.io :as io])
  (:import (java.nio ByteOrder)))

(deftest test-rw-float
  (testing "Can read and write floats"
    (let [f (java.io.File/createTempFile "test-rw-float" nil)
          v (float 3.141592)
          float_size_bytes 4
          offset 0]
      (let [buffer (doto
                       (nio.core/mmap f offset float_size_bytes)
                     (.order ByteOrder/LITTLE_ENDIAN))]
        (.putFloat buffer v))
      (let [buffer (doto
                       (nio.core/mmap f)
                     (.order ByteOrder/LITTLE_ENDIAN))]

        (is (= v (.getFloat buffer)))))))


(deftest test-rw-pad
  (testing "can add padding before and after a float"
    (let [f (java.io.File/createTempFile "test-rw-pad" nil)
          v (float 3.141592)
          total_file_size 7
          offset 0]
      (let [buffer (doto
                       (nio.core/mmap f offset total_file_size)
                     (.order ByteOrder/LITTLE_ENDIAN))]
        (.put buffer (byte 0))
        (.put buffer (byte 0))
        (.putFloat buffer v)
        (.put buffer (byte 0)))
        
      (let [buffer (doto
                       (nio.core/mmap f)
                     (.order ByteOrder/LITTLE_ENDIAN))]
        (doseq [_ (range 2)]
          (.get buffer))
        
        (is (= v (.getFloat buffer)))))))
