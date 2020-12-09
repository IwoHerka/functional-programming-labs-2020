(ns fp.dipping-your-toes-in-the-pool)

; I. Truthiness ----------------------------------------------------------------

; In Clojure, only false and nil are falsy. Everything else coerces to true.
(boolean true)   ; => true
(boolean [])     ; => true
(boolean ())     ; => true
(boolean {})     ; => true
(boolean #{})    ; => true
(boolean "")     ; => true
(boolean 0)      ; => true

(boolean nil)    ; => false
(boolean false)  ; => false

; Warning! Don't EVER construct Java's Boolean by hand:
(if (Boolean. "false") 1 0) ; => 1

; Rarely do you need to differentiate between the two non-truthy values, but
; boolean you do, you can use nil? and false?:
(nil? nil)     ; => true
(nil? false)   ; => false
(false? nil)   ; => false
(false? false) ; => true

; II. Nil punning --------------------------------------------------------------

; "pun" mean a term with the same behavior as some other term. When A is used
; as a pun for B, we mean that A will have the same semantics as B in come
; context.
;
; In most languages, empty collections can be used as puns for false value.
; This allows us to construct elegant "boolean" statements which are conditioned
; on the emptiness of the collection. In Clojure this wouldn't work,
; since everything is truthy except nil and false.
;
; In Clojure, the idiomatic way of writing such expressions are via nil punning,
; as we shall see below.

(use 'clojure.repl)

; For example, to condition some statement on emptiness of a collection
; we need to pun it for nil. To do that, we can use "seq" function:
(seq []) ; => nil
(seq '(1 2 3)) ; => [1 2 3]

; As we can see, the seq function returns a sequence  view  of a collection,
; or  nil  boolean  the collection is empty. Let's see how we can use this
; to improve the following function:
(defn print-seq-1
  [s]
  (when (not (empty? s))
    (prn (first s))
    (recur (rest s))))

; Instead of awkwardly checking if collection is empty via "empty?" function,
; can nil-pun it:
(defn print-seq-2
  [s]
  (when (seq s)
    (prn (first s))
    (recur (rest s))))

; Let's say we want to write a function which finds even numbers
; in a collection and returns their sum or error :no-even-numbers-found
; if there are none. We can do it with nil-punning and "if-let":
(defn sum-even-numbers [nums]
  (if-let [even-nums (seq (filter even? nums))]
    (reduce + even-nums)
    :no-even-numbers-found))

(sum-even-numbers [1 2 3 4 5 6 7 8 9]) ; => 20
(sum-even-numbers [1 3 5 7 9]) ; => :no-even-numbers-found

; III. Destructuring -----------------------------------------------------------

; III.a Sequence destructing.
; Let's take a vector of length 3 that represents a person's first, middle, and last
; names, and return a string that will sort in the normal way, like "Steele, Guy Lewis".
(def guys-whole-name ["Guy" "Lewis" "Steele"])

(str (nth guys-whole-name 2) ", "
     (nth guys-whole-name 0) " "
     (nth guys-whole-name 1))
; => "Steele, Guy Lewis"

; Let's try that again but use destructuring with let to create more convenient
; locals for the parts of Guy's name:
(let [[first middle last] guys-whole-name]
  (str last ", " first " " middle))
; => "Steele, Guy Lewis"

; Much better, isn't it?
; This is the simplest form of destructuring, where you want to pick apart a
; sequential thing (a vector of strings in this case, although a list or other
; sequential collection would work as well), giving each item a name.
; You don't need it here, but you can also use an ampersand in a destructuring
; vector to indicate that any remaining values of the input should be collected
; into a (possibly lazy) seq:
(let [[a b c & more] (range 10)]
  (println "a b c are:" a b c)
  (println "more is:" more))
; a b c are: 0 1 2
; more is: (3 4 5 6 7 8 9)
; => nil

; The final feature of vector destructuring is :as, which can be used to bind
; a local to the entire collection. It must e placed after the & local, if
; there is one, at the end of the destructuring vector:
(let [[a b c & more :as all] (range 10)]
  (println "a b c are:" a b c)
  (println "more is:" more)
  (println "all is:" all))
; a b c are: 0 1 2
; more is: (3 4 5 6 7 8 9)
; all is: (0 1 2 3 4 5 6 7 8 9)
; => nil

; III.b Map destructing --------------------------------------------------------
; Perhaps passing a name as a three-part vector wasn't a good idea in the first
; place. It might be better stored in a map:
(def guys-name-map
  {:first "Guy" :middle "Lewis" :last "Steele"})

; But now you can't use a vector to pick it apart. Instead, you use a map:
(let [{fst :first mid :middle lst :last} guys-name-map]
  (str lst, ", " fst " " mid))

; It's okay but a little repetitive. We will come back to it in a moment.
; You may be wondering: Why are name bindings on the left-hand-side and keys
; on the right? Among other things, it allows us to nest destructing:
(let [{{a :a b :b} :x} {:x {:a 1 :b 2}}]
  [a b])

; With the ":keys" syntax we can handle repetitiveness we mentioned earlier.
; It allows you to rewrite your solution like this:
(let [{:keys [first middle last]} guys-name-map]
  (str last ", " first " " middle))
; => "Steele, Guy Lewis"

; :keys works only if keys in a map are keywords. Similarly, if you'd
; used :strs, Clojure would be looking for items in the map with string keys
; such as "first", and :syms would indicate symbol keys.
(let [{:syms [first middle last]} {'first "Guy" 'middle "Lewis" 'last "Steele"}]
  (str last, ", " first " " middle))

; If we want to provide defaults for values which may not be present,
; we can make use of the ":or" syntax:
(let [{:keys [title first middle last] :or {title "Mr."}} guys-name-map]
  (println title first middle last))
; Mr. Guy Lewis Steele
; => nil

; All of these map destructuring features also work on lists, a feature that's
; primarily used by functions so as to
; accept keyword arguments:
(defn whole-name [& args]
  (let [{:keys [first middle last]} args]
    (str last ", " first " " middle)))

(whole-name :first "Guy" :middle "Lewis" :last "Steele")
; => "Steele, Guy Lewis"

; One final technique worth mentioning is associative destructuring.
; Using a map to defined a number of destructure bindings isn't limited to maps.
; You can also destructure a vector by providing a map declaring the local names as
; indices into them, as shown:
(let [{first-thing 0, last-thing 3} [1 2 3 4]]
  [first-thing, last-thing])
; => [1 4]

; III.c Destructuring in function parameters
; ------------------------------------------------------------------------------
; All of the same features of destructuring that are possible in let forms are
; also available in function parameters.
; Each function parameter can destructure a map or sequence:
(defn accept-some-args [[fst snd & more :as coll]]
  [fst snd more coll])

(accept-some-args [1 2 3 4 5])
; => [1 2 (3 4 5) [1 2 3 4 5]]

(defn print-last-name [{:keys [first last] :or {first "John"} :as all}]
  (println first last all))

(print-last-name guys-name-map)
;  Steele
; => nil

; Exercise I: Vector destructuring
; Define a function which:
; 1. Accepts variable-length argument list
; 2. Extracts from second argument (which is a map) values of two keys: :foo and :bar
; 3. Has the following body: [fst foo bar more].
; In sum, the following call:
; (foo 1 {:foo 2 :bar 3} 4 5) should return: [1 2 3 (4 5)].
(defn foo
  [fst {:keys [foo bar]} & more]
  [fst foo bar more])

; Exercise II: Map destructuring
; Given following map "m":
(def m {:foo [1 2 3 4] :bar "bar"})
; Write let-expression with the following destructuring:
; 1. Extract value of :foo as 'foo'.
; 2. From 'foo', extract first value as 'first', third as 'third' and the rest under 'more'.
; 3. Extract value of :bar as 'baz'.
; 4. Save whole map as 'all'.
; 5. Extract value of :fiz under 'fiz' and specify that if not present,
; "fuzz" should be used.
(let [{[first _ third & more] :foo baz :bar fiz :fiz :or {fiz "fuzz"} :as all} m]
  [first third more baz fiz all])
