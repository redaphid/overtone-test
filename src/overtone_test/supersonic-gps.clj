(ns overtone-test.core
  (:use [overtone.live])
  (:require [clj-time.coerce :as time])
)
(def baseFrequency 300)
(def addHz 100)
(def interval 100)

(definst beep [frequency baseFrequency volume 0.1]
  (* volume (sin-osc frequency)))

(defn changeBeep []
  (let [
    millis (time/to-long (now))
    tick (mod (long (/ millis interval)) 2)]
    (println (mod (offset-from-interval interval) interval) tick)
    (ctl beep :frequency
      (+ baseFrequency (* addHz tick)))
  )
)

(defn emitter []
  (let [initial-delay (+ interval (offset-from-interval interval) 1000)]
    (periodic interval changeBeep initial-delay)
  )
)

(ctl beep :volume 1)
(beep)
(emitter)
(stop)

(defn get-next-time [interval]
  (let [millis (time/to-long (now))]
    (+ millis (- interval (mod millis interval)))
  )
)

(defn offset-from-interval [interval]
    (- interval (mod (time/to-long (now)) interval))
)
