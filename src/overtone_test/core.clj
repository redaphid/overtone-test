(ns overtone-test.core
  (:use [overtone.live])
)


(definst tone [frequency 440] (sin-osc frequency))

(tone 300)
(kill tone)
