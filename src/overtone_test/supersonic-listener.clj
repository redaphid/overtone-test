(ns overtone.examples.buses.getonthebus
  (:use overtone.live)
  (:require [overtone.studio.util :as util])
  (:require [overtone.studio.scope :as scope])
  )

(stop)

(defonce mic-bus (audio-bus))

(defsynth mic []
  (out mic-bus (sound-in 0)))

(defonce mic-group (group "mic-group main"))
(def mic-buffer (buffer 2048))
(util/bus->buf mic-bus mic-buffer)
(scope/scope mic-buffer)
(mic)
;; Let's create some source synths that will send signals on our buses. Let's
;; also put them in the early group to ensure that their signals get sent first.

(comment
  (def tri-synth-inst (tri-synth [:tail early-g] tri-bus))
  (def sin-synth-inst (sin-synth [:tail early-g] sin-bus))
  )

;; Notice how these synths aren't making or controlling any sound. This is because
;; they're control rate synths and also because their output is going to the buses
;; we created which aren't connected to anything. The signals are therefore ignored.

;; We can verify that they're running by viewing the node tree. We can do this
;; easily with the following fn:
(pp-node-tree)

;; This will print the current synthesis node-tree to the REPL. This can get pretty
;; hairy and large, but if you've only evaluated this tutorial since you started
;; Overtone, it should be pretty manageable. You should be able to see the tri-synth
;; and sin-synth within the early birds group, which itself is within the
;; get-on-the-bus main group which itself is within the Overtone Default group.

;; Now, let's use these signals to make actual noise!

;; First, let's define a synth that we'll use to receive the signal from the bus to
;; make some sound:

(defsynth modulated-vol-tri [vol-bus 0 freq 220]
  (out 0 (pan2 (* (in:kr vol-bus) (lf-tri freq)))))

;; Notice how this synth is using the default audio rate version of the out
;; ugen to output a signal to the left speaker of your computer. It is also
;; possible to use out:ar to achieve the same result.

;; This synth reads the value off the bus (at control rate)
;; and multiplies it with the lf-tri ugen's sample value.  The overall
;; result is then sent to two consecutive buses: 0 and 1. (pan2 duplicates
;; a single channel signal to two channels; this is documented in more detail
;; in some of the getting-started examples).


;; This synth is a little trickier.  It calculates the frequency
;; by taking the sample value from the bus, multipling it by
;; the frequency amplitude, and then adding the result to the midpoint
;; or median frequency.  Therefore, if you hook it up to a bus carrying
;; a sine wave signal and use the defalt mid-freq and freq-amp values
;; you'll get an lf-tri ugen that oscillates between 165 and 275 Hz
;; (165 is 55 below 220, and 275 is 55 above 220)
(defsynth modulated-freq-tri [freq-bus 0 mid-freq 220 freq-amp 55]
  (let [freq (+ mid-freq (* (in:kr freq-bus) freq-amp))]
    (out 0 (pan2 (lf-tri freq)))))


;; One of the nifty things about buses is that you can have multiple synths reading
;; them from the same time.

;; Evaluate these to use the signals on the buses to modulate synth parameters
(comment
  (def mvt (modulated-vol-tri [:tail later-g] sin-bus))
  (def mft (modulated-freq-tri [:tail later-g] sin-bus))
  )

(stop)
;; Fun fact: These two examples are key features
;; of AM and FM radio transmitters, respectively.

;; Switch the bus that is modulating the frequency
;; to be the triangle bus.
;;
(comment
  (ctl mft :freq-bus tri-bus)
  )

;; Change the frequency of the triangle wave on the tri-bus
;; This causes the modulation of the volume to happen more slowly
(comment
  (ctl tri-synth-inst :freq 0.5)
  )

;; Switch the modulated-vol-tri instance to be modulated by the triangle
;; bus as well.
(comment
  (ctl mvt :vol-bus tri-bus)
  )

;; Kill the two things that are making noise
(comment
  (do
    (kill mft)
    (kill mvt))
  )

;; At this point, the buses are still carrying data from the tri-synth and sin-synth;
;; you'll have to kill them as well explicitly or invoke (stop) if you want them to stop.

;; Or can re-use them!
(comment
  (def mvt-2 (modulated-vol-tri [:tail later-g] sin-bus 110))
  (kill mvt-2)
  )

(control-bus-get sin-bus)
(periodic 5 #(println (control-bus-get sin-bus)))
(stop)
;; Wacky heterodyning stuff!
(comment
  (do
    (ctl tri-synth-inst :freq 5)
    (ctl sin-synth-inst :freq 5)
    (def mft-2 (modulated-freq-tri [:tail later-g] sin-bus 220 55))
    (def mft-3 (modulated-freq-tri [:tail later-g] tri-bus 220 55)))
  (ctl sin-synth-inst :freq 4)
  (kill mft-2 mft-3)
  )

(comment
  "For when you're ready to stop all the things"
  (stop)
  )
