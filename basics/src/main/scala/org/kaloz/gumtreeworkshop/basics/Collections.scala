package org.kaloz.gumtreeworkshop.basics

import scala.util.matching.Regex

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

  /**
    * SOLUTION
    */
  val result = sentences.mkString.replaceAll(" ", "")
    .groupBy(identity)
    .collect { case (_, value) if value.size >= 5 && value.size < 20 => value.size }.toList
    .sortWith(_ > _)
    .take(5).sum * 2

  /**
    * TASK: Convert every 4 and 5 letter word in the sentences variable to a number where the number for a word equals to the sum of the positions of all the characters in a given dictionary
    * 'us' -> 90 since position of 'u' is 46 and position of 's' is 44 in the given dictionary
    * If something cannot be found in the alphabet then its value is -1
    */

  val alphabet = (('A' to 'Z') ++ ('a' to 'z')).zipWithIndex.toMap
  val Word = "(.{4,5})".r

  val result2 = sentences.mkString(" ").split(" ").toList.collect { case Word(word) => word.map(alphabet.getOrElse(_, -1)).sum }
  println(result2)

  /**
    * Split functions
    */
  val prepareWordList: List[String] => List[String] = sentences => sentences.mkString(" ").split(" ").toList
  val convertWordList: List[String] => List[Int] = words => words.collect { case Word(word) => word.map(alphabet.getOrElse(_, -1)).sum }

  println((prepareWordList andThen convertWordList) (sentences).mkString(" "))

}
