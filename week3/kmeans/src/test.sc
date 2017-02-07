val x = List(15, 10, 5, 8, 20, 12)
val ops = List(true, false)
val y1a = x.groupBy(_ > 20)

x map (_ > 20)

val missing = ops filterNot (y1a contains)


missing map ((_,List()))

y1a + (true -> List())

y1a