(ns overtone-test.core
  (:use [overtone.live])
  (:require [clj-time.coerce :as time])
)
(def baseFrequency 400)
(def addHz 100)
(def interval 100)

(definst beep [frequency baseFrequency volume 0.1]
  (* volume (sin-osc frequency)))

(defn changeBeep []
  (let [
    millis (time/to-long (now))
    tick (mod (long (/ millis interval)) 2)]
    (println (offset-from-interval interval) tick)
    (ctl beep :frequency
      (+ baseFrequency (* addHz tick)))
  )
)

(defn emitter [interval]
  (let [initial-delay (+ interval (offset-from-interval interval))]
    (println "initial-delay" (+ initial-delay (time/to-long (now))) initial-delay)
    (periodic interval changeBeep initial-delay)
  )
)

(beep)
(emitter 100)
(stop)

(defn get-next-time [interval]
  (let [millis (time/to-long (now))]
    (+ millis (- interval (mod millis interval)))
  )
)

(defn offset-from-interval [interval]
    (- interval (mod (time/to-long (now)) interval))
)
