(reset)

(stop)
(start)

(-> a/void
    (a/auto :ch [:scene :bg]
            :in 5
            :to [255 255 0])
    (a/fire system))

(-> a/void
    (a/auto :ch [:scene :bg]
            :in 1
            :to [0 0 0])
    (a/fire system))

(-> a/void
    (a/auto :ch [:simple-square :xlate]
            :in 5
            :to [100 100])
    (a/fire system))

(-> a/void
    (a/auto :ch [:simple-square :rotate]
            :in 10
            :to 1/4)
    (a/auto+ :ch [:simple-square :rotate]
             :at 10
             :in 0
             :to 0)
    (a/fire system))

(-> a/void
    (a/auto :ch [:simple-square :xlate]
            :to [0 0])
    (a/auto :ch [:simple-square :xlate]
             :at 0.1
             :in 5
             :to [100 100])
    (a/fire system))
