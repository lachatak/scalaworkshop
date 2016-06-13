package org.kaloz.gumtreeworkshop.basics

object PatternMatching extends App {

  def matching(x: Any): Unit = {
    x match {
      case x: Int if x > 5 => println(s"$x > 5")
      case 3 => println(s"$x = 3")
      case "string" => println(s"$x is String")
      case List(first, _, _, last) => println(s"$first - $last")
      case head :: tail => println(tail)
      case _ => println("else")
    }
  }

  matching(3)
  matching("string")
  matching(List(1, 2, 3, 4))
  matching(List(1, 2, 3, 4, 5))

  //Pattern matching in try catch
  case class SomeError(error: String) extends RuntimeException(error)

  try {
    throw new SomeError("error")
  } catch {
    case SomeError(message) => println(message)
  }

  //Pattern matching in functions
  val l = List(Some(1), None, Some(5), Some(10))
  l.filter {
    case Some(x) if x >= 5 => true
    case _ => false
  }.foreach(println)

  //Enabling pattern matching in custom classes
  class ClassWithUnapply(val value: String)

  object ClassWithUnapply {
    def unapply(unapply: ClassWithUnapply): Option[String] = Some(unapply.value)
  }

  val c = new ClassWithUnapply("test")

  c match {
    case ClassWithUnapply(value) => println(value)
  }

  //Pattern matching and regex
  val DatePattern =
    """(\d{2})-(\d{2})-(\d{4})""".r
  val date = "30-04-2016"

  //Extract day, month, year variables using mattern matching
  date match {
    case DatePattern(day, month, year) => println(s"day: $day, month: $month, year: $year")
  }

  val DatePattern(day, month, year) = date
  println(s"day: $day, month: $month, year: $year")

  //Filter items which comply with the pattern
  val items = List("30-04-2016", "not-valid", "15-12-2016")
  for {
    DatePattern(day, month, year) <- items
  } yield println(s"day: $day, month: $month, year: $year")

  items.collect { case DatePattern(day, month, year) => s"day: $day, month: $month, year: $year" }.foreach(println)

  //Fails if you don't provide exactly 2 programme arguments
  val Array(first, second) = args
  println(s"first argument is $first")
  println(s"second argument is $second")

  //Complex pattern matching in nested case class
  case class Person(name: String, age: Option[Int] = None)

  val persons = List(Person("test"), Person("test2", Some(30)), Person("test3", Some(20)))
  println(persons.filter { case Person(name, Some(age)) if age > 20 => true; case _ => false }.map(_.name))
  println(persons.collect { case Person(name, Some(age)) if age > 20 => name })
  println(persons.collect { case Person(name, a@Some(age)) if age > 20 => a })

  /**
    * TASK: write a simple argument parser which stores the results in a map
    */
}
