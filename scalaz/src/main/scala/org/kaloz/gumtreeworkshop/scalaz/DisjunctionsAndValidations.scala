package org.kaloz.gumtreeworkshop.scalaz

/* Standard Scala provides special type that can be used to handle (successful or not) result of some operation.
   It's called Either[L, R], that has two possible subtypes - Left(value : L) or Right(value : R).

   Either can have either right value (by convention it's successful result of the operation), or the left value
   (by convention - the unsuccessful result of the operation).

  * */
object DisjunctionsAndValidations extends App {

  def div(x : Int, y : Int): Either[String, Int] = if (y == 0) Left("Divide by 0") else Right(x / y)

  println(div(7, 5))
  println(div(7, 0))


  //You can use pattern matching to perform different operations depending on the result

  div(7, 5) match {
    case Right(value) => println(s"Success $value")
    case Left(error) => println(s"Error $error")
  }

  //What is important, contrary to Option, the Either is not a monad, and we cannot use it in for
  //comprehensions.



}

