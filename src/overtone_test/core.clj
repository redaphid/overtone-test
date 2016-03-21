(ns overtone-test.core
  (:use [overtone.live])
)

(definst tone [frequency 440] (sin-osc frequency))
(tone 300)
(kill tone)

(comment
(definst tone [frequency 440] (sin-osc frequency))

(tone 300)
(tone 300.5)
(kill tone)
(periodic 1000 #(println "foo"))
(periodic 1000 #(beep))
(definst beep [frequency 440 duration 1]
  (let [envelope (line 1 0 duration :action FREE)]
          (* envelope (sin-osc frequency))))

(periodic 500 tone)
(definst doubletone [freq1 440 freq2 440]
  (+
    (sin-osc freq1)
    (sin-osc freq2)
  )
)

(doubletone 300 300.5)

(kill doubletone)


(definst beep [frequency 440 duration 1]
  (let [envelope (line 1 0 duration :action FREE)]
          (* envelope (sin-osc frequency))))

(println "Hi")

(definst sin-beep [frequency 440 envelope-frequency 500]
  (let [envelope (sin-osc envelope-frequency)]
        (* envelope (sin-osc:kr frequency))))

(sin-beep)
(ctl sin-beep :frequency 150)
(ctl sin-beep :envelope-frequency 151)

(stop)
(kill sin-beep)

(def kick (sample (freesound-path 333991)))
(def one-twenty-bpm (metronome 120))
(looper one-twenty-bpm kick)
(kill kick)
(kill looper)




(beep 600 1)
(kill beep)



(ctl beep :frequency 300 )
(definst fsaw [] (saw 400))
(fsaw)
(kill fsaw)


(foo)
(kill 41)


(definst baz [freq 440] (* 0.3 (saw freq)))
(baz 220)
(baz 440)
(kill baz)
)
