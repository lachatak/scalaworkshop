package org.kaloz.gumtreeworkshop.basic

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scalaz._
import Scalaz._

/**
   When working with async code, quite frequently you will have to deal with nested monads.
   Like Future[Try[SomeResult]] or Future[Option[SomeResult]]. While composing monads in Scala code is quite easy
   thanks to for comprehension, doing this for nested monads is more complex.

   We can use ScalaZ Monad transformers to be able to work with F[M[A]] monads like with M[A] monads, then
    we can compose M[A] monads in much simplier way e.g. using for comprehension.
    ScalaZ contains monad transformers for the following monads:
    - Option (so that you can transform F[Option[A]] to OptionT[A]
    - List (F[List[T])
    - Either (F[List[T]) - be aware that ScalaZ defines it's own Either type - which is different from scala's Either
    - and many more
*/
object MonadTransformers extends App {

  /** Asynchronously divides x by y.
    * This will return Success with dividing result but return Failure in case of
    * dividing by zero */
  def div(x : Int, y : Int) : Future[Option[Int]] = Future {
    try {
      Some(x / y)
    } catch {
      case ex: Throwable => None
    }
  }

  def get(x : Int) : Future[Option[Int]] = Future(Some(x))

  def sum(x : Int, y : Int): Future[Option[Int]] = Future(Some(x + y))

  val functionComposition: OptionT[Future, Int] = for {
    op1result <- OptionT(div(6, 2))
    op2result <- OptionT(get(7))
    op3result <- OptionT(sum(op1result, op2result))
  } yield op3result

  /* The operation above produces just a composition of three operations. In order to run the composition we have to
    call the 'run' function
  */
  val resultFuture = functionComposition.run

  val finalResult = Await.result(resultFuture, 10 seconds)

  if (finalResult.isDefined) {
    println(s"Success: ${finalResult.get}")
  } else {
    println(s"Failure!!! No result produced")
  }

  /* EXERCISE: Try to change the test to raise div by 0 error */

}
