package org.kaloz.gumtreeworkshop.scalaz

import scalaz.Scalaz._
import scalaz._

/**
  * https://en.wikipedia.org/wiki/Monoid
  * - single associative binary operation
  * - identity element
  *
  * trait Monoid[A] {
  * an identity element
  * def zero: A
  *
  * an associative operation
  * def op(x: A, y: A): A
  * }
  */
object Monoids extends App {

  /**
    * Option[A] is monoid if A monoid
    * (A, B, ..N ) is monoid if A, B, .. N is monoid
    * Map[A, B] is monoid if B is monoid
    */

  val stringmonoid = "string" |+| "monoid"
  val intmonoid = 2 |+| 5
  val listmonoid = List(1, 2, 3) |+| List(4, 5, 6)
  val optionmonoid = Option(5) |+| Option(10)
  val optionmonoid2 = Option(5) |+| None
  val optionmonoid3 = none[Int] |+| 5.some
  val tuplemonoid = ("a", 1) |+|("b", 5)
  val mapmonoid = Map("a" -> 1, "b" -> 5) |+| Map("b" -> 3, "c" -> 2)

  println(stringmonoid)
  println(intmonoid)
  println(listmonoid)
  println(optionmonoid)
  println(optionmonoid2)
  println(optionmonoid3)
  println(tuplemonoid)
  println(mapmonoid)

  val list = List(("a", 1), ("b", 5), ("b", 6), ("c", 2))

  println(list.foldLeft(("", 0))((sum, item) => (sum._1 + item._1, sum._2 + item._2)))
  println(list.foldMap(identity))

  println(list.foldLeft(("", 0))(identity((sum, item) => (sum._1 + item._1, sum._2 + item._2))))
  println(list.foldLeft(("", 0))(identity(_ |+| _)))
  println(list.foldMap(identity))
  println(list.foldMap(i => (i._1, i._2 * 2)))

  println(list.groupBy(item => item._1).mapValues(_.map(i => i._2).sum))
  println(list.foldMap(Map(_)))

  //  implicit def listMonoid[A]: Monoid[List[A]] = new Monoid[List[A]] {
  //    def zero: List[A] = Nil
  //    def append(f1: List[A], f2: => List[A]) = f1 ::: f2
  //  }

  /**
    * TASK1: Count how many times an error message occurs.
    * not_found -> 1, invalid -> 2, error -> 1
    *
    */

  case class ErrorMessage(message: String)

  val errors = List(ErrorMessage("invalid"), ErrorMessage("not_found"), ErrorMessage("invalid"), ErrorMessage("error"))

  /**
    * TASK2: Provide a final ErrorMessage2 object with all the possible error messages and the number of all errors in the list.
    * ErrorMessage2(Set(invalid, not_found, error),5)
    */
  case class ErrorMessage2(message: Set[String], counter: Int = 1)

  val errors2 = List(ErrorMessage2(Set("invalid")), ErrorMessage2(Set("not_found")), ErrorMessage2(Set("invalid")), ErrorMessage2(Set("error")))

}
