(ns overtone-test.core
  (:use [overtone.live])
  (:require [clj-time.coerce :as time])
)

(def frequency 400)

(defn beeper [t val]
  (println (in-millis (now)))
  (let [next-t (+ t 200)]
   (apply-at next-t #'foo [next-t (inc val)])
  )
)

(beeper (now) 0)

(stop)


(defn get-next-time []
  (let [millis (time/to-long (now))]
    (+ millis (- 200 (mod millis 200)))
  )
)

(- (get-next-time) (time/to-long (now)))
