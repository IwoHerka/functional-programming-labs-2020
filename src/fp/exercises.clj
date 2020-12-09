(ns fp.exercises)

; 1. Write a function which returns the last element in a sequence.

(defn f [coll] (first (reverse coll)))

; 2. Write a function which returns the second to last element from a sequence.

(defn f [coll] (second (reverse coll)))

(f coll)

; 3. Write a function which returns the Nth element from a sequence.

(defn f [coll n]
  (last (take (inc n) coll)))

(defn f [coll n]
  (first (drop n coll)))

; 4. Write a function which returns the total number of elements in a sequence.

(reduce (fn [count _] (inc count)) coll)

; 5. Write a function which reverses a sequence.

(reduce (fn [acc x] (cons x acc)) '() coll)

; 6. Write a function which returns only the odd numbers from a sequence.

(filter odd? coll)

; 7. Write a function which returns true if the given sequence is a palindrome.
; Hint: "racecar" does not equal '(\r \a \c \e \c \a \r)

(defn palindrome [string]
  (= (seq string) (reverse string)))

; 8. Write a function which flattens a sequence.

[[1 2 3] 4 [5 6]] => [1 2 3 4 5 6]
(palindrome "abcba")

; 9. Write a function which takes a string and returns a new string containing only the capital letters.

(apply str (filter (fn [s] (Character/isUpperCase s)) "HelLo WoRLd"))
