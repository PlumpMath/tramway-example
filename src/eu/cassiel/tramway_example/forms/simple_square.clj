(ns eu.cassiel.tramway-example.forms.simple-square
  "Simple test - mouse click in a square. Currently, mouse events
   hacked via a channel."
  (:require (eu.cassiel [tramway :as m])
            (eu.cassiel.tramway [auto :as a])
            (eu.cassiel.tramway [forms :refer [FORM]]
                                [scene :as $])
            (eu.cassiel [twizzle :as tw])
            (eu.cassiel.twizzle [interpolators :as twi])
            (tween-clj [core :as tween])
            [clojure.core.async :as async]))

(def simple-square
  ;; ch: channel which accepts automation state mappers.
  (let [ch (async/chan 10)]
    (reify FORM
      (init-form-state [self] nil)
      (automation-inits [self] {[:simple-square :xlate] [0 0]
                                [:simple-square :rotate] 0})
      (automation-interps [self]
        (let [expo (twi/wrap-tween tween/ease-in-out tween/transition-expo)]
          {[:simple-square :xlate] (partial twi/interp-vectors expo)
           [:simple-square :rotate] expo}))

      ;; Update also looks at our event channel.
      (update [self t struct-state auto-state]
        {:auto-state (async/alt!! ch ([f] (f auto-state))
                                  :default auto-state)})

      (nodes [self struct-state auto-state]
        (let [fill-grey (* 255 (or (tw/sample auto-state [:simple-square :fill]) 0))
              [tx ty] (tw/sample auto-state [:simple-square :xlate])]
          [(->> ($/rect 0 0 0 200 200
                        :mouse-fn (fn [& {:keys [click-type x y]}]
                                    (println click-type x y)
                                    (let [norm-x (/ (+ x 100) 200)
                                          norm-y (/ (+ y 100) 200)]
                                      (async/>!! ch (-> a/void
                                                        (a/auto :ch [:scene :bg]
                                                                :in 0
                                                                :to [(* 255 norm-x) 0 0])
                                                        (a/auto :ch [:simple-square :fill]
                                                                :in 0
                                                                :to norm-y)))
                                      true)))
                ($/fill [fill-grey])
                ($/stroke [50])
                ($/with-rotation (or (tw/sample auto-state [:simple-square :rotate]) 0))
                ($/with-translation [tx ty 0]))])))))
