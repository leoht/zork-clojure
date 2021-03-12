(ns zork.core
  (:require [zork.state :as state])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (state/print-state (state/init-state)))
