# Introduction

*Future* - Simple concept of asynchronous computation.
* Future represent computation that eventually will be finished (or fail, or timeout)
* Future can be executed asynchronously - in separate thread

# How to create it

    val asyncOne : Future[Int] = Future { Thread.sleep(1000); x = 1 }

Seem to be very easy :)

Unfortunately this will fail because in order to create Future you have to provide a way of creating/getting the
execution thread. This is done by implicit parameter of the future contructor:

    def apply[T](body: =>T)(implicit executor: ExecutionContext)

Not going into details - if you want to usestandard Scalas thread pool, you shoud add the following line to your imports:

     import scala.concurrent.ExecutionContext.Implicits.global

While futures are asynchronous, we can also future which is synchronous (which means it holds already processed
computation). This is useful in tests:

    val syncOne: Future[Int] = Future.successful(1)

# Promise

Future has it's close sibling which is a *Promise*. Promise is kind of builder of the future.
You can make the promise that you will provide the value, and the you may fulfill the promise.
From the promise you can derive the future.

    val p : Promise[Int] = Promise[Int]
    val f : Future[Int] = p.future
    p.complete(Success(16))

Each promise can be completed only once. Any subsequent calls to *complete* will be ignored. Completing the promise
makes the result of

# What can we do with future?

The code below returns Some(value) if future ended, None() if not:

     val result = asyncOne.value

There is a problem here. We don't know whether it's finished.
We can wait

     import scala.concurrent.duration._ //imports 'seconds' function

     Await.ready(asyncOne, 5 seconds)
     println(asyncOne.value)

or

     println(Await.result(asyncOne, 5 seconds)

# Future is a monad!

*A monad is just a monoid in the category of endofunctors!*

But for you it means that future has map/flatMap functions and can be used in for comprehensions
in order to transform results of computation or combine multiple computations

     def prepareText(stringList: List[String]): Future[String] = ...

     def calculateCharacterOccurrences(text: String): Future[List[Int]] = ...

     val sum : Future[Int] = for {
        prepared <- prepareText(sentences)
        occurrences <- calculateCharacterOccurrences(prepared)
        sum <- calculateOccurrenceSummary(occurrences, 5)
      } yield sum * 2

Important thing! Operations here are executed sequentially. If you want to run them concurrently you
have to do it in the following way.

      val task1 = Future { Thread.sleep(1000) println("Fizz"); "Fizz" }
      val task2 = Future { Thread.sleep(500) println("Buzz"); "Buzz" }

      val result : Future[String] = for {
        r1 <- task1
        r2 <- task2
      } yield r1 + r2

# Other

## Error handling and recovery

If future fails, apply pf to the error, builds new future from it and returns it:

      def recover(pf: PartialFunction[Throwable, U])

If future fails, applies pf to the error and returns it:

      def recoverWith(pf: PartialFunction[Throwable, Future[U])

If future fails returns 'that' ignoring the error:

      def fallbackTo(that: Future[U])


## Useful operations

Convert sequence of Futures to Future of sequences:

      val task1 = Future { Thread.sleep(1000) println("Fizz"); "Fizz" }
      val task2 = Future { Thread.sleep(500) println("Buzz"); "Buzz" }
      val result: Future[List[String]] = Future.sequence(Seq(task1, task2))

Build Future from the list and asynchronous function that returns Future:

      val reverse: String => Future[String] = ...
      val reverse2: Future[List[String]] = Future.traverse(sentences)(reverse)
