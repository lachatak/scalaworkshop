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

  /* One problem with disjunction compositions is that they fail fast. This means the first operation in for
   * comprehension will result in skipping the rest of steps. This will also means that we will get an error
   * details only from the first operation that failed.
   *
   * Sometimes we would like to take multiple data inputs, validate them and then combine into one object.
   * Good example of that is validating input form.
   *
   * ScalaZ has a good solution to this problem, which is based of a concept of Applicative Functors.
   *
   * It introduces the new type - Validation, which is somewhat similar to Either/Disjunction, but rather than
   * being a monad, is an applicative.
   * You can combine multiple Validation objects into one and then apply a function on them.
   *
   * Validation come together with a specific data structure called Nel (non empty list)
   * They are usually used together in a form Validation[Nel[E], V] where V is a type of validated value while E is
   * a type of error that can occur during validation.
   *
   * Validation[Nel[E], V] has an alias ValidationNel[E, V]
   *
   * */

  case class User(name : String, surname : String)

  def validate(input : String) : ValidationNel[String, String] =
    if (input.isEmpty) "Input is empty".failureNel[String]
    else input.successNel[String]

  val firstname = validate("John")
  val lastname = validate("Smith")

    //Validation alone is not so important, it has quite similar capabilities as Either or Disjunction

    firstname match {
      case Success(value) => println("Validation succeeded")
      case Failure(errors) => println("Errors occurred " + errors.toList.mkString(","))
    }

    //Things are getting more interesting when we would like to combine multiple validations
    val user = (firstname |@| lastname) { User(_, _) }

    println(user)

  //EXERCISE: Check what happens if user entered empty value

}