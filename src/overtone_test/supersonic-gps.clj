(ns overtone-test.core
  (:use [overtone.live])
  (:require [clj-time.coerce :as time])
)

(definst beep [frequency 440 duration 1]
  (let [envelope (line 1 1 duration :action FREE)]
          (* envelope (sin-osc frequency))))

(beep)
(stop)

(defn emitter [interval frequency]
  (println "The time is: " (time/to-long (now)) interval frequency)
  (let [next-t (get-next-time interval)]
   (beep frequency (/ interval 2000))
  ;  (beep (/ interval 2000) (sin-osc frequency))
   (println "Executing next in " next-t "ms")
   (apply-at next-t #'emitter [interval frequency])
  )
)

(emitter 1500 550)
(beeper (now) 0)

(stop)


(defn get-next-time [interval]
  (let [millis (time/to-long (now))]
    (+ millis (- interval (mod millis interval)))
  )
)

(- (get-next-time 100) (time/to-long (now)))
