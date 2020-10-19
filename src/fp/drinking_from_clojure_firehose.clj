(ns fp.drinking-from-clojure-firehose)

; I. Comments ------------------------------------------------------------------
; Comments start with semicolons.
;; By conventions, sometimes two ; are used.

; II. Scalars ------------------------------------------------------------------
; Integers comprise the entire number set, both positive and negative. Any number
; starting with an optional sign or digit followed exclusively by digits is
; considered and stored as some form of integer, although several different
; concrete types are used depending on the circumstance.
42
+9
-107
95386581487658

; The following illustrates the use of decimal, hexadecimal, octal, radix-32, and
; binary literals, respectively, all representing the same number:
[127 0x7F 0177 32r3V 2r01111111]

; Floating-point numbers are the decimal expansion of rational numbers. Like
; Clojure’s implementation of integers, the floating-point values can be
; arbitrarily precise.
1.17
+1.22
-2.
366e7
32e-14
10.7e-3

; Clojure provides a rational type in addition to integer and floating-point
; numbers.Rational numbers offer a more compact and precise representation of a
; given valueover floating-point.
22/7
-7/22
104147947491/4719674914
-103/4

; Symbols in Clojure are objects in their own right but are often used to
; represent another value:
(def foo 22/7)
; => #'user/foo
foo
; => 22/7
(type 'foo)
; => clojure.lang.Symbol

; Keywords are similar to symbols, except that they always evaluate to themselves.
:2
:?
:ffviiisthebestgame
:bar

(type :foo)
; => clojure.lang.Keyword

;; A string is any sequence of characters enclosed within a set of double quotes,
;; including newlines, as shown:

"This is a string"
"This is also a
         String"

; Clojure characters are written with a literal syntax prefixed with a backslash
; and are stored as Java Character objects:
\a     ; The character lowercase a
\A     ; The character uppercase
\u0042 ; The Unicode character uppercase B
\\     ; The back-slash character \
\u30DE ; The Unicode katakana character ?

; III. Collections -------------------------------------------------------------
; Lists are the classic collection type in Lisp (the name comes from list
; processing, after all) languages, and Clojure is no exception. Literal lists are
; written with parentheses:
'(yankee hotel foxtrot)
'(1234)
()
'(:fred ethel)
'(1 2 (abc) 4 5)

; Like lists, vectors store a series of values:
[12 :a :b :c]

; Lists are linked-list data structures, while vectors are array-backed.
; Vectors and Lists are java classes too!
(class [1 2 3]); => clojure.lang.PersistentVector
(class '(1 2 3)); => clojure.lang.PersistentList

; A list would be written as just (1 2 3), but we have to quote
; it to stop the reader thinking it's a function.
; Also, (list 1 2 3) is the same as '(1 2 3)

; "Collections" are just groups of data
; Both lists and vectors are collections:
(coll? '(1 2 3)) ; => true
(coll? [1 2 3]) ; => true

; Use cons to add an item to the beginning of a list or vector.
(cons 4 [1 2 3]) ; => (4 1 2 3)
(cons 4 '(1 2 3)) ; => (4 1 2 3)

; Conj will add an item to a collection in the most efficient way.
; For lists, they insert at the beginning. For vectors, they insert at the end.
(conj [1 2 3] 4) ; => [1 2 3 4]
(conj '(1 2 3) 4) ; => (4 1 2 3)

; Maps store unique keys and one value per key—similar to what some languages and
; libraries call dictionaries or hashes.

{:a 1 :b 2 :c 3}
; Maps can use any hashable type as a key:
{1 "one" "2" :two :3 'three}

(def a-map {:a 1 :b 2 :c 3})

; Retrieve a value from map by calling it as a function:
(a-map :a)
; => 1
; If map uses keywords as keys, they can be used as functions too:
(:a a-map)
; => 1

; Use assoc to add new keys to hash-maps
(def new-map (assoc a-map :d 4))
new-map ; => {:a 1, :b 2, :c 3, :d 4}

; But remember, clojure types are immutable!
a-map ; => {:a 1, :b 2, :c 3}

; Use dissoc to remove keys
(dissoc new-map :a :b) ; => {:c 3}

; Sets store zero or more unique items. They’re written using curly braces with a
; leading hash:
#{1 2 "three" :four 0x5}
(set [1 2 3 1 2 3 3 2 1 3 2 1]) ; => #{1 2 3}

; Add a member with conj
(conj #{1 2 3} 4) ; => #{1 2 3 4}

; Remove one with disj
(disj #{1 2 3} 1) ; => #{2 3}

; Test for existence by using the set as a function:
(#{1 2 3} 1) ; => 1
(#{1 2 3} 4) ; => nil

; IV. Vars ---------------------------------------------------------------------
; Programmers are typically accustomed to dealing with variables and mutation.
; Clojure’s closest analogy to the variable is the var. A var is named by a symbol
; and holds a single value. Vars provide a mechanism to refer to a mutable
; storage location that can be dynamically rebound.
(def foo 42)
; => #'user/foo

; Each "def" invocation creates a global binding, so it should never
; be used in local blocks.

; If the Var did not already exist and no initial value is supplied, the var is unbound:
(def x) ; => #'user/x
x ; => #object[clojure.lang.Var$Unbound 0x14008db3 "Unbound: #'user/x"]

; V. Functions -----------------------------------------------------------------
; An anonymous (unnamed) Clojure function can be defined using a special form. A
; special form is a Clojure expression that’s part of the core language but not
; created in terms of functions, types, or macros.
(fn [x y] (println "Making a set") #{x y})
; => #function[user/eval14479/fn--14480]

; The "def" special form can be used to define functions via vars.
(def make-set (fn [x y] (println "Making a set") #{x y}))
; => #'user/make-set

; A convienient shortcut for that is the "defn" macro:
(defn make-set
  "Takes two values and makes a set from them."
  [x y]
  (println "Making a set")
  #{x y})

; You can also use the lambda shorthand notation to create functions:
(def hello2 #(str "Hello " %1))
(hello2 "Julie") ; => "Hello Julie"

; You can have multi-variadic functions, too
(defn hello3
  ([] "Hello World")
  ([name] (str "Hello " name)))
(hello3 "Jake") ; => "Hello Jake"
(hello3) ; => "Hello World"

; Functions can pack extra arguments up in a seq for you
(defn count-args [& args]
  (str "You passed " (count args) " args: " args))
(count-args 1 2 3) ; => "You passed 3 args: (1 2 3)"

; You can mix regular and packed arguments
(defn hello-count [name & args]
  (str "Hello " name ", you passed " (count args) " extra args"))
(hello-count "Finn" 1 2 3)
; => "Hello Finn, you passed 3 extra args"

; Clojure also has many advanced features such as pattern matching and
; destructuring, but we will take a look at them in future.

; VI. Locals, loops, and blocks ------------------------------------------------
; Use the do form when you have a series or block of expressions that need to be
; treated as one. All the expressions are evaluated, but only the last one is
; returned:
(do
  (def x 5)
  (def y 4)
  (+ x y)
  [x y])
; => [5 4]

; Clojure doesn’t have local variables, but it does have locals; they just can’t
; vary. Locals are created and their scope is defined using a let form, which
; starts with a vector that defines the bindings, followed by any number of
; expressions that make up the body.
(let [r 5
      pi 3.1415
      r-squared (* r r)]
  (println "radius is" r)
  (* pi r-squared))
; radius is 5
; => 78.53750000000001

; The simplest way to loop in Clojure is via "for" function
; which yields a lazy sequence of evaluations of expressions.
(for [i (range 10)] i)
; => (0 1 2 3 4 5 6 7 8 9)
(take 100 (for [x (range 100000000) y (range 1000000) :while (< y x)] [x y]))
; => ...

; The classic way to build a loop in a Lisp, however, is a recursive call, and
; this is true in Clojure as well.
(defn print-down-from
  [x]
  (if (pos? x)
    (println x)
    (recur (dec x))))

(print-down-from 3)
; 3
; 2
; 1
; => nil

; Sometimes you want to loop back not to the top of the function, but to somewhere
; inside it. In such case, you could use "loop":
(defn sum-down-from
  [initial-x]
  (loop [sum 0 x initial-x]
    (if (pos? x)
      (recur (+ sum x) (dec x))
      sum)))

(sum-down-from 3) ; => 3

; VII. Quoting -----------------------------------------------------------------
; When a collection is evaluated, each of its contained items is evaluated first:
(cons 1 [2 3])
; => (1 2 3)

; However, if we want to prevent evaluation and treat the above expression
; as a simple list which happens to have a symbol "cons" as its first
; element, we can use quoting:

'(cons 1 [2 3])
; => (cons 1 [2 3])

(type '(cons 1 [2 3]))
; => clojure.lang.PersistentList

; If needed, you can also unquote (evaluate) inside quoted expression.
; To do this, however, we will be using more powerful form of quoting
; using backtick, called syntax-quote. To unoquote inside an expression we
; preprend it with ~:
`(+ 10 ~(* 3 2))
; => (clojure.core/+ 10 6)
; (One of the differences between simple quoteing and syntax-quote is that
; all symbols in a form are automatically qualified (hence "clojure.core/+"
; instead of simple "+").

; VIII. Interop with Java ------------------------------------------------------

; Java has a huge and useful standard library, so
; you'll want to learn how to get at it.

; Use import to load a java module
(import java.util.Date)

; Use the class name with a "." at the end to make a new instance
(Date.)
; => #inst "2020-09-11T08:59:54.659-00:00"

; Alternatively, we can use the "new" function:
(new Date)
; => #inst "2020-09-11T08:59:54.659-00:00"

; Use . to call methods. Or, use the ".method" shortcut
(. (Date.) getTime) ; => 1599814822101
(.getTime (Date.)) ; => 1599814822101

; Use / to call static methods
(System/currentTimeMillis) ; => 1599814855127

; Use doto to make dealing with (mutable) classes more tolerable
(import java.util.Calendar)
(doto (Calendar/getInstance)
  (.set 2000 1 1 0 0 0)
  .getTime) ; => A Date. set to 2000-01-01 00:00:00

; Instance fields can be set via the set! function:
(let [origin (java.awt.Point. 0 0)]
  (set! (.-x origin) 15)
  (str origin))
; => "java.awt.Point[x=15,y=0]"

; When working with Java, it’s common practice to chain together a sequence of
; method calls on the return type of the previous method call:
; new java.util.Date().toString().endsWith("2014") /* Java code */

; This code can be write in Clojure as follows using ".." macro:
(.. (java.util.Date.) toString (endsWith "2014")) ; => true

; IX. Namespaces ---------------------------------------------------------------
; ------------------------------------------------------------------------------
; Clojure’s namespaces provide a way to bundle related functions, macros, and
; values.
; To create a new namespace, you can use the "ns" macro:
(ns foo)
; => nil
; The prompt will now display:
; foo>

; We can check current namespace via special var "*ns*:
*ns*
; => #namespace[foo]

; Use require to import a module
(require 'clojure.string)

; Use "use" to get all functions from the module
(use 'clojure.set)

; Using :require in "ns" macro indicates that you want the clojure.set
; namespace loaded. You can also use the :as directive to create an additional
; alias to clojure.set:
(ns foo (:require [clojure.set :as s]))

; To use unqualified Java classes in any given namespace, you should import them
; via the :import directive:
(import java.util.Date)

; Let's return to previous namespace:
(in-ns 'fp.drinking-from-clojure-firehose)

; X. STM -----------------------------------------------------------------------

; Software Transactional Memory is the mechanism clojure uses to handle
; persistent state. There are a few constructs in clojure that use this.

; An atom is the simplest. Pass it an initial value
(def my-atom (atom {}))

; Update an atom with swap!.
; swap! takes a function and calls it with the current value of the atom
; as the first argument, and any trailing arguments as the second
(swap! my-atom assoc :a 1) ; Sets my-atom to the result of (assoc {} :a 1)
(swap! my-atom assoc :b 2) ; Sets my-atom to the result of (assoc {:a 1} :b 2)

; Use '@' to dereference the atom and get the value
my-atom  ;=> Atom<#...> (Returns the Atom object)
@my-atom ; => {:a 1 :b 2}

; Here's a simple counter using an atom
(def counter (atom 0))
(defn inc-counter []
  (swap! counter inc))

(inc-counter)
(inc-counter)
(inc-counter)
(inc-counter)
(inc-counter)

@counter ; => 5