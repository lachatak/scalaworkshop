package org.kaloz.gumtreeworkshop.basic

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
   When working with async code, quite frequently you will have to deal with nested monads.
   Like Future[Try[SomeResult]] or Future[Option[SomeResult]]. While composing monads in Scala code is quite easy
   thanks to for comprehension, doing this for nested monads is more complex.
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

  val resultFuture: Future[Option[Int]] = for {
    //Easy...
    op1result <- div(6, 2)
    op2result <- get(7)
    op3result <- if (op1result.isDefined && op2result.isDefined) { sum(op1result.get, op2result.get)} else Future.successful(None)
    //Well that's not so easy to read
  } yield op3result

  val finalResult = Await.result(resultFuture, 10 seconds)

  if (finalResult.isDefined) {
    println(s"Success: ${finalResult.get}")
  } else {
    println(s"Failure!!! No result produced")
  }

  /* EXERCISE: Try to change the test to raise div by 0 error */

}
