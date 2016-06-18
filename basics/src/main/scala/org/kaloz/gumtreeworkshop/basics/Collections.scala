package org.kaloz.gumtreeworkshop.basics

object Collections extends App {

  val l = List("a", "b", "c")
  val s = Set("a", "b", "c")
  val t = 1 to 10
  val v = 'b' to 'z'
  val m = Map(1 -> 'a', 2 -> 'b', 3 -> 'c')

  println(l.filter(_ == "a"))
  println(l.mkString)
  println(s.map(_ * 5))
  println(t.sum)
  println(t.reverse)
  println(t.reduce(_ * _))
  println(t.foldLeft(10)(_ + _))
  println(t.collect { case n if n % 2 == 0 => "a" * n })
  println(t.map(List.fill(5)(_)).flatten)
  println(t.flatMap(List.fill(5)(_)))
  println(t.map(x => (x % 2 == 1, x)).groupBy(_._1))
  println(t.partition(_ % 2 == 0))
  println(v.forall(_ > 'a'))
  println(v.take(2))
  println(v.filter(_ > 'g').headOption)
  println(v.zipWithIndex.map(_.swap).toMap)
  println(m.mapValues(_ * 2))
  println(m.get(1).map(_.toInt))
  println(m.getOrElse(4, '-'))
  v.foreach(println)

  /**
    * TASK: Aggregate and multiply by 2 the first 5 most frequently used character's occurrence,
    * where the occurrence is greater and equals to 5 and lower than 20
    */

  val sentences = List(
    "Monad transformers allow us to stack monads.",
    """Say we have a monad, like Option, and we want to wrap it in another monad, like \/, in a convenient way (where convenient is to be defined shortly).""",
    "Monad transformers let us do this.",
    "Scalaz comes with lots of monad transformers.",
    "Let’s see how to use them and the benefits they supply."
  )
}
