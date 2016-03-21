(ns overtone-test.core
  (:use [overtone.live])
  (:require [clj-time.coerce :as time])
)

(definst beep [frequency 440 duration 1]
  (let [envelope (line 1 1 duration :action FREE)]
          (* envelope (sin-osc frequency))))

(defn emitter [interval frequency]
  (let [initial-delay (+ interval (offset-from-interval interval))]
    (periodic interval #(beep frequency (/ interval 2)) initial-delay)
  )
)

(emitter 20 17000)

(stop)


(defn get-next-time [interval]
  (let [millis (time/to-long (now))]
    (+ millis (- interval (mod millis interval)))
  )
)

(defn offset-from-interval [interval]
    (mod (time/to-long (now)) interval)
)
