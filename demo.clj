;;;;;;;;;;;
;;;;;
;;;;;;;;;;;;;;;                                                          888
;;;;;;;                                                                  888
;;                      _ooooooooo._                                     888
;;;;;;;;;;           ,o888PP""""PP88   .d88b.  888  888  .d88b.  888d888 888888 .d88b.  88888b.   .d88b.
;;;;               d88P''          '  d88""88b 888  888 d8P  Y8b 888P"   888   d88""88b 888 "88b d8P  Y8b
;;;;;;           ,88P                 88    88 Y88  88P 88888888 888     888   88    88 888  888 88888888
;;;;;;;;;       ,88                   Y88..88P  Y8bd8P  Y8b.     888     Y88b. Y88..88P 888  888 Y8b.
;;;;           ,88'                    "Y88P"    Y88P    "Y8888  888      "Y888 "Y88P"  888  888  "Y8888
;;;;;;;        d8P
;;             d8b                        88[
;;;;;          `88                       J88
;;              Y8b                     ,88'
;;;;;;;;         Y8b.                  d88'
;;;;;             `Y8b._            _o88P
;;;;;;;            `Y888oo.____ooo888P'
;;;;;;;;;              '"PP888888PP''
;;;;;;;
;;;;;;;;
;;;;;;;
;;;;;;;                     SmartMonday 03/2017 - Making music through programming: Overtone and Clojure
;;;;;;;;;                                             Presented by: Abde
;;;;;;;;;;;;;;;
;;;;;;;;
;;;;;;;;;;



;; dependencies
(use 'overtone.core)
(use 'overtone.inst.piano) ;; -> for piano
(use 'overtone.inst.drum)

;; connexion, just in case
(connect-external-server 57110)


;; some wave manipulation, so crazy
(demo  (pan2 (* (sin-osc 110)
                (saw 220)
                (sin-osc 330)
                (sin-osc 440))))

;; some synth explanation (could be done better) -> explain gens
(defsynth xdlol [freq 440 amp 1]
  (out 0 (pan2 ( * (env-gen (lin 0 0.2 0.1))
                (sin-osc freq)
                amp))))

(defsynth xdhat [amp 1]
  (out 0 (pan2 ( * (env-gen (perc 0 0.5 :curve -9) )
                (white-noise)
                amp)
               )))
(xdlol)
(xdhat)

(stop)

;; lpf and hpf -> low pass filter and high pass filter, explain


;; get some scales (what is a scale? -> explain)
(def scaleE (into [] (scale :E5 :aeolian)))
(def scaleC (into [] (scale :C3 :aeolian)))

;; global "variable" (it's actually a macro)
(def noote :E5)

;; we can also load some crazy samples !
(def kick (sample "./wav/kick.wav"))
(kick)



;; loop beat
(defn player [beat]
  (at (metro beat) (xdhat 0.5))
  (apply-by (metro (inc beat)) #'player (inc beat) []))


;; loop melody (just a random choice in the aeolian scale
(defn melodyPlayer [beat]
  ;;(at (metro beat) (xdlol (midi->hz (note ((rand-nth melodies) (mod beat 8))))))
  (at (metro beat) (piano (note (rand-nth (scale noote :aeolian)))))
  ;;(at (metro beat) (xdlol (cosr )))
  (apply-by (metro (inc beat)) #'melodyPlayer (inc beat) [])
  )

;; base, using my xdlol synth (maybe test with a piano)
(defn base [beat]
  (at (metro beat) (xdlol (midi->hz (note noote)) 0.5))
  (if (= (mod beat 4) 0)
    (def noote ((into [] (remove #{noote} (list :E5 :C5))) 0) ))
  (apply-by (metro (inc beat)) #'base (inc beat) [])
  )

;; now we can define our global metronome, so our sequencers will be syncronised
;; we can change the tempo with the metro-bpm command
(def metro (metronome 120))
(metro-bpm metro 240)
(metro)

;; play this, redefine functions on the go ~~~~~
(player (metro))
(melodyPlayer (metro))
(base (metro))

(stop)

;; we can record too
(recording-start "c:/test.wav")
(recording-stop)
;; to complete




;; look at this it's a sinwave: |~~~~~~~~~~|
