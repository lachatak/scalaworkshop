package org.kaloz.gumtreeworkshop.basics

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * List[String] => Future[String]
  * String => Future[List[Int]]
  * List[Int] => Future[Int]
  *
  * List[String] => Future[String] andThen String => Future[List[Int]] andThen List[Int] => Future[Int]
  * Doesn't work. kliesli from scalaz could help. Not topic of this workshop
  *
  * Without scalaz use for comprehension or flatMap
  * It works exactly the same way for any monads. For now it is enough to say that
  * it works for List, Option, Try, Future. By default it is not working for Either
  *
  */
object Futures extends App {

  val sentences = List(
    "Monad transformers allow us to stack monads.",
    """Say we have a monad, like Option, and we want to wrap it in another monad, like \/, in a convenient way (where convenient is to be defined shortly).""",
    "Monad transformers let us do this.",
    "Scalaz comes with lots of monad transformers.",
    "Let’s see how to use them and the benefits they supply."
  )

  def prepareText(stringList: List[String]): Future[String] = Future {
    stringList.mkString.replaceAll(" ", "")
  }

  def calculateCharacterOccurrences(text: String): Future[List[Int]] = Future {
    text.groupBy(identity).collect { case (_, value) if value.size >= 5 && value.size < 20 => value.size }.toList
  }

  def calculateOccurrenceSummary(occurrences: List[Int], num: Int): Future[Int] = Future {
    occurrences.sortWith(_ > _).take(num).sum
  }

  //for-comprehension to extract value from generated container type
  val sum = for {
    prepared <- prepareText(sentences)
    occurrences <- calculateCharacterOccurrences(prepared)
    sum <- calculateOccurrenceSummary(occurrences, 5)
  } yield sum * 2

  val sum2 = prepareText(sentences).flatMap(calculateCharacterOccurrences).flatMap(calculateOccurrenceSummary(_, 5)).map(_ * 2)

  val result = Await.result(sum, 5 second)
  val result2 = Await.result(sum2, 5 second)

  println(result)
  println(result2)

  //Future[String \/ String] would be better. For the test's sake exception is fine
  val reverse: String => Future[String] = text => Future {
    import Utils._
    if (text.toLowerCase.contains("krs")) throw new IllegalArgumentException("Contains 'krs' keyword")
    text.flipWords
  }

  val reverse1: Future[List[String]] = Future.sequence(sentences.map(reverse))
  val reverse2: Future[List[String]] = Future.traverse(sentences)(reverse)

  val result3 = Await.result(reverse1, 5 second)
  val result4 = Await.result(reverse2, 5 second)

  println(result3)
  println(result4)

  private val teamWithKrs: List[String] = List("Gideon", "Mo", "Gordon", "krs")

  val reverse3: Future[List[String]] = Future.traverse(teamWithKrs)(reverse).recover {
    case _ => List("krs in the team...")
  }

  val reverse4: Future[List[String]] = Future.traverse(teamWithKrs)(reverse).recoverWith {
    case _ => Future {
      List("krs in the team...")
    }
  }

  val reverse5: Future[List[String]] = Future.traverse(teamWithKrs)(reverse).fallbackTo {
    Future {
      throw new IllegalArgumentException("Fallback error!")
    }
  }

  val result5 = Await.result(reverse3, 5 second)
  val result6 = Await.result(reverse4, 5 second)
  val result7 = Await.result(reverse5, 5 second)

  println(result5)
  println(result6)
  println(result7)

  /**
    * TASK: Convert the provided list of strings to numbers and then calculate the summary of those numbers using Future combinators.
    * If the string cannot be converted to number default it back to 0
    */

  val strings = List("2", "10", "a", "15")

  val convert: String => Future[Int] = string => Future {
    string.toInt
  }.recover { case _ => 0 }

  val sum3 = Future.traverse(strings)(convert).map(_.sum)

  val result8 = Await.result(sum3, 5 second)

  println(result8)
}
