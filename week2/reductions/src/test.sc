val str1 = "(o_()"
val str2 = ":-)"
val str3 = "())("

val str4 = "(if (zero? x) max (/ 1 x))"
val str5 = "I told him (that it's not (yet) done). (But he wasn't listening)"

val f:(Int, Char) => Int = (a, b) => a + (if (b == '(') 1 else if (b == ')') -1 else 0)

str1.scanLeft(0)(f)
str2.scanLeft(0)(f)
str3.scanLeft(0)(f)

str4.scanLeft(0)(f)
str5.scanLeft(0)(f)

1/2

val from = 0
val until = 1
(from, (from + until) / 2)
((from + until) / 2, until)