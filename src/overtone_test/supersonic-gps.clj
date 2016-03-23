(ns overtone-test.core
  (:use [overtone.live])
  (:require [clj-time.coerce :as time])
)
(def baseFrequency 400)
(def addHz 100)
(def interval 1000)

(definst beep [frequency baseFrequency volume 0.1]
  (* volume (sin-osc frequency)))

(defn changeBeep []
  (let [
    millis (time/to-long (now))
    tick (mod (long (/ millis interval)) 2)]
    (println millis tick)
    (ctl beep :frequency
      (+ baseFrequency (* addHz tick)))
  )
)

(defn emitter [interval]
  (let [initial-delay (+ interval (offset-from-interval interval))]
    (periodic interval changeBeep initial-delay)
  )
)
(beep)
(emitter 1000)
(stop)

(defn get-next-time [interval]
  (let [millis (time/to-long (now))]
    (+ millis (- interval (mod millis interval)))
  )
)

(defn offset-from-interval [interval]
    (mod (time/to-long (now)) interval)
)
