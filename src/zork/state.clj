(ns zork.state)

(def player-count (atom 0))
(def next-player-position (swap! player-count inc))

(defn init-empty-room-vector
  "initialize an empty room vector"
  []
  [ \- \- \- \- \- \- \- 
    \- \- \- \- \- \- \- 
    \- \- \- \- \- \- \- 
    \- \- \- \- \- \- \- 
    \- \- \- \- \- \- \- 
    \- \- \- \- \- \- \-
    \- \- \- \- \- \- \- ])

(defn map-coord-to-vector-pos
  "maps 2-dimensional coordinate [x, y]
  to an index on the one-dimensional vector"
  [room-vec, [x, y]]
  (+ x (* y (count room-vec))))

(defn empty-room-char? [c] (= c \-))
(defn empty-room?
  [room-vec coord]
  (empty-room-char? (get room-vec (map-coord-to-vector-pos room-vec coord))))

(defn set-room
  [room-vec coord element]
  (assoc room-vec (map-coord-to-vector-pos room-vec coord) element))

(defn add-challenge
  [room-vec challenge]
  (let [size (count room-vec)]
    (loop [try-x (rand-int size) try-y (rand-int size)]
      (if (empty-room? room-vec [try-x try-y])
          (set-room room-vec [try-x try-y] challenge)
          (recur (rand-int size) (rand-int size))))))

(defn init-player-position
  [room-vec]
  (let [size (count room-vec)]
    (loop [try-x (rand-int size) try-y (rand-int size)]
      (if (empty-room? room-vec [try-x try-y])
          [try-x try-y]
          (recur (rand-int size) (rand-int size))))))

(defn init-challenges
  [room-vector challenges]
  (loop [rem-challenges challenges
         acc-room-vector room-vector]
        (if (empty? rem-challenges)
            acc-room-vector
            (recur
              (rest rem-challenges)
              (add-challenge acc-room-vector (first rem-challenges))))))

(def default-challenges {
  \a "An ancient creature of the night"
  \b "A balrog"
  \c "A dangerous crabe"
  \d "A dragon"
  \e "A mysterious elve"
  \f "A feary"
  \g "A mad gladiator armed with a sword"
})

(defn init-rooms
  "initialize the rooms vector with different challenges and the player position
    \\- represents an empty room
    \\S is the player starting position
    \\a to \\z are challenges defined in the opponent map"
  []
  (->
    (init-empty-room-vector)
    (init-challenges (keys default-challenges))))

(defn print-rooms
  [{:keys [rooms]}]
  (dorun
    (map
    (fn [row]
      (println (subvec rooms (* row 7) (+ 7 (* row 7)))) )
    (range 7))))
(defn print-challenges
  [{:keys [rooms]}]
  (dorun
    (map (fn [room]
      (print 
        (if (empty-room-char? room)
          ""
          (str room " -> " (get default-challenges room) "\n"))))
      rooms)))

(defn print-state
  [state]
  (do
    (println "\n --- room map --- \n")
    (print-rooms state)
    (println "\n -- challenges -- \n")
    (print-challenges state)
    (println)))
(defn init-state []
  (let [rooms (init-rooms)]
    {:rooms rooms
    :player-position (init-player-position rooms)}))