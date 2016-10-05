package org.kaloz.gumtreeworkshop.scalaz

import scalaz._
import Scalaz._
/* Standard Scala provides special type that can be used to handle (successful or not) result of some operation.
   It's called Either[L, R], that has two possible subtypes - Left(value : L) or Right(value : R).

   Either can have either right value (by convention it's successful result of the operation), or the left value
   (by convention - the unsuccessful result of the operation).

   ScalaZ introduces Disjunction - alternative version of Either[L,P] which is a monad, so that we can use for
   comprehension.

   Disjunction comes with lots of utility implicit conversions and functions that make the code simplier

  * */
object DisjunctionsAndValidations extends App {

  def div(x : Int, y : Int): Disjunction[String, Int] =
    if (y == 0) "Divide by 0".left
    else (x / y).right

  println(div(7, 5))
  println(div(7, 0))

  //You can use pattern matching to perform different operations depending on the result

  div(7, 5) match {
    case \/-(value) => println(s"Success $value")
    case -\/(error) => println(s"Error $error")
  }

  //One of the advantages of Disjunction over Either is that we cane use it in for comprehensions

  val result = for (
    x <- div(10, 2);
    y <- div(9, 3)
  ) yield x + y

  println(result)

  //EXERCISE: Check what will be the result of for comprehension when the first div fails, what if the second div fails

}