(ns overtone-test.core
  (:use [overtone.live])
  (:require [clj-time.coerce :as time])
)

(def frequency 400)

(defn emitter [time]
  (println (time/to-long (now)))
  (let [next-t (get-next-time)]
   (apply-at next-t #'emitter [next-t (inc val)])
  )
)


(emitter (get-next-time))

(beeper (now) 0)

(stop)


(defn get-next-time [interval]
  (let [millis (time/to-long (now))]
    (+ millis (- interval (mod millis interval)))
  )
)

(- (get-next-time 100) (time/to-long (now)))
