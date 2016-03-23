(ns overtone-test.core
  (:use [overtone core])
  (:require [clojure.core.matrix :as mat])
  (:import [mikera.matrixx Matrix AMatrix])
  (:import [java.awt.image BufferedImage]))

;; TODO
;; This namespace contains exanmple code using overtone. Should really separate out overtone into demo code

(defonce boot (boot-external-server))

(require '[mikera.spectral.inst :as inst])

(defonce buf (buffer 44100)) ;; create a buffer to store the audio

  ;; ============================================
  ;; buffer fun

(defsynth bus->buf [bus 20 buf 0]
      (record-buf (in bus) buf))

(defsynth barf [out-bus 20] (out out-bus (sin-osc 400)))

(def arr (into-array Double/TYPE (buffer-read samp-buf)))

(defn mag
  (^double [^double a ^double b]
    (Math/sqrt (+ (* a a) (* b b)))))

(defn fft-matrix [^doubles arr]
  (let [n (count arr)
        length 8192   ;; length of FFT window
        half-length (quot length 2)
        height (min 400 (quot half-length 2))
        fft (mikera.matrixx.algo.FFT. (int length))
        tarr (double-array (* 2 length))
        stride 1000
        ts (quot (- n length) stride)
        result-array (double-array (* height ts))]
    (dotimes [i ts]
      (System/arraycopy arr (* i stride) tarr 0 length)
      (.realForward fft tarr)
      (dotimes [j height]
        (aset result-array (+ i (* j ts))
              (mag (aget tarr (* j 2)) (aget tarr (inc (* j 2)))))))
    (Matrix/wrap height ts result-array)))

(defn colour ^long [^double val]
  (let [lval (* (inc (Math/log val)) 0.9)]
    (cond
    (<= lval 0.0) 0xFF000000
    (<= lval 1.0) (let [v (- lval 0.0)] (col/rgb 0.0 0.0 v))
    (<= lval 2.0) (let [v (- lval 1.0)] (col/rgb v 0.0 (- 1.0 v)))
    (<= lval 3.0) (let [v (- lval 2.0)] (col/rgb 1.0 v 0.0))
    (<= lval 4.0) (let [v (- lval 3.0)] (col/rgb 1.0 1.0 v))
    :else 0xFFFFFFFFF)))

(defn render
  "Renders a spectrogram matrix into a bufferedimage"
  ([M]
    (render M (img/new-image (mat/column-count M) (mat/row-count M) )))
  ([^AMatrix M ^BufferedImage bi]
    (let [w (.getWidth bi)
          h (.getHeight bi)]
      (dotimes [x w]
        (dotimes [y h]
          (.setRGB bi (int x) (- (dec h) (int y)) (unchecked-int (spec/heatmap (* 0.005 (.get M (int y) (int x))) ))))))
    bi))

(def M (fft-matrix arr))

(defn demo-code []

  (img/show (render (fft-matrix arr)))

  )
