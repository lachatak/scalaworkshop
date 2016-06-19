package org.kaloz.gumtreeworkshop.basics

import org.kaloz.gumtreeworkshop.basics.Utils.{StringOps, _}

import scala.util.Try

object Implicits extends App {

  //Implicit conversion
  val value: String = 6
  println(value)

  //Pimp my library
  //https://dzone.com/articles/scala-snippets-4-pimp-my
  val text = "This is some important text with lots of unnecessary spaces!"

  println(text.removeWhiteSpaces)
  println(text.flipWords)

  //Implicit parameter
  implicit val repeatNumber = 5

  println(text.repeatN)

  implicit val stringToInt = (t: String) => Try(t.toInt).toOption.getOrElse(0)
  implicit val stringToList = (t: String) => t.toList

  println("45".as[Int])
  println("gumtree".as[Int])
  println("gumtree".as[List[Char]])

  /**
    * TASK:
    * - Provide IntOps implicit class with 'sumOfAllEven' method which
    * gives back the sum of all the even numbers between 0 and the given Int
    *
    * - Provide IntOps implicit class with 'sumOf' method which
    * gives back the sum of all the numbers between 0 and the given Int which satisfies
    * the provided implicit predicate
    */

  println(5.sumOfAllEven)

  implicit val predicate = (num: Int) => num.toString.contains("2")
  println(100.sumOf)

}

object Utils {

  implicit def numToString(num: Int): String = if (num % 2 == 0) "even" else "odd"

  implicit class StringOps(val text: String) extends AnyVal {
    def removeWhiteSpaces = text.replaceAll(" ", "")

    def flipWords = text.split(" ").map(_.reverse).mkString(" ")

    def repeatN(implicit repeatNumber: Int) = List.fill(repeatNumber)(text).mkString

    def as[T](implicit converter: String => T) = converter(text)
  }

  /**
    * SOLUTION:
    * - Provide IntOps implicit class with 'sumOfAllEven' method which
    * gives back the sum of all the even numbers between 0 and the given Int
    *
    * - Provide IntOps implicit class with 'sumOf' method which
    * gives back the sum of all the numbers between 0 and the given Int which satisfies
    * the provided implicit predicate
    */
  implicit class IntOps(val num: Int) extends AnyVal {
    def sumOfAllEven = (0 to num).filter(_ % 2 == 0).sum

    def sumOf(implicit predicate: Int => Boolean) = (0 to num).filter(predicate).sum
  }

}
