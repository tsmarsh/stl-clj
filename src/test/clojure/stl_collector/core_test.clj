(ns stl-collector.core-test
  (:require [clojure.test :refer :all]
            [stl-collector.reader :as r]
            [stl-collector.writer :as w]
            [nio.core :as nio]
            [clojure.java.io :as io])
  (:import (java.nio ByteOrder)
           (java.io File)))

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

(deftest test-rw-stl-file
  (testing "can read and write a real file"
    (let [tmp-file (File/createTempFile "bob" ".stl")
          real-file (->> "stl/Creeper.stl"
                         io/resource
                         io/file) 
          read-file (r/read-stl real-file)
          write-file (w/write-stl read-file tmp-file)
          new-read-file (r/read-stl tmp-file)]
      (= read-file new-read-file)
      (.delete tmp-file)))) 
