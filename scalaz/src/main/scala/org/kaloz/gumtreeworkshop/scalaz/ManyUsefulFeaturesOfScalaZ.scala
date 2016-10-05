package org.kaloz.gumtreeworkshop.scalaz

import scala.concurrent.{Await, Future}
import scalaz.Scalaz._
import scalaz._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
object ManyUsefulFeaturesOfScalaZ extends App {

  // ScalaZ introduces lots of very small but useful extensions to standard Scala library

  /*
    There are fold methods for most of basic scala types, these methods allows to map all possible states of specific
     type to values
   */

  val option : Option[Int] = Some(5)
  println(option.fold("Empty")(value => s"There is a value $value"))

  val either : Either[String, String] = Right("RIGHT")
  println(either.fold(left => s"Left: $left", right => s"Right: $right"))

  val boolean : Boolean = false
  println(boolean.fold("True", "False"))
  (1 == 1) ? "TEST" | "NO TEST" //Actually it's the same as fold

  /*
    You can simplify the code that creates Option with value only when specific condition occurred
   */
  val withoutScalaZ: Option[String] = if (1 == 1) {
    Some("value")
  } else None

  val withScalaZ = (1 == 1).option("value")
  println(withScalaZ)

  //Typesafe equals
  println(1 == "1")
  println(1 === 1)
  //println(1 === "1") - uncomment - this wont compie

  // Converting english singulars to plurals

  println("factory".plural(5))
  println("cat".plural(5))

  // Determine ordering
  1.0 ?|? 2.0 match {
    case scalaz.Ordering.LT => println("Less")
    case scalaz.Ordering.EQ => println("Equal")
    case scalaz.Ordering.GT => println("Greater")
  }

  //Simplified way of creating Some("X")
  println("X".some)

  //Convert List of futures into future of Lists

  val op1 = Future { "Do " }
  val op2 = Future { "things " }
  val op3 = Future { "concurrently" }

  val sequenceOfFutures: List[Future[String]] = List(op1, op2, op3)
  val futureOfSequences: Future[List[String]] = sequenceOfFutures.sequenceU

  println(Await.result(futureOfSequences.map(_.mkString), 10 seconds))

}
