# Introduction

Pattern matching is a really powerful tool that allows you to control the flow of app
It's simple but very powerful tool. You can find it in scala in really lots of places,
sometimes even where you don't expect it.

    def matching(x: Any): Unit = {
        x match {
        //Simple pattern matching in primitive types
        case 3 => println(s"$x = 3")
        case "string" => println(s"$x is String")

        //Pattern matching with condition
        case x: Int if x > 5 => println(s"$x > 5")

        //Pattern matching on lists
        case List(first, _, _, last) => println(s"$first - $last")
        case head :: tail => println(tail)

        //Pattern matching on types
        case x: Double => println(s"$x is double")

        //Pattern matching on everythin
        case _ => println("else")
    }

    matching(3)
    matching(5.0)
    matching("string")
    matching(List(1, 2, 3, 4))
    matching(List(1, 2, 3, 4, 5))

Contrary to Java, match expression stops on the first matched condition (there is no need for *break* clause)

# Partial function

Pattern matching is used as a base for much more concepts in the Scala language:
- exception handling
- filtering
- variable assignment
- for comprehensions

Technical detail for advanced - scala provides concept called *partial function*. This is the one-argument
function which is not applicable to all possible input values. When applied for unsupported inputs, it may
throw an exception.

    val describe : PartialFunction[Any, String] = {
      case x : Int => s"$x is Int"
      case x : String => s"$x is String"
    }

    val description = describe(5)
    val description = describe(fail) //this will throw an exception

Match expression, and other code structures described below use partial functions.

# Usages of pattern matching


## Exception handling

Scala uses pattern matching to catch specific kind of exception

case class SomeError(error: String) extends RuntimeException(error)

try {
    throw new SomeError("error")
} catch {
    case SomeError(message) => println(message)
    case e : NullPointerException => println("Kaboom!")
}

Remember - you are not limited just to matching on exception type,
you can match on the exception arguments, you can use if's etc.

## Utility functions of standard classes

Pattern matching is used in plenty of functions in the language, this includes filter...

      val l = List(Some(1), None, Some(5), Some(10))
      l.filter {
        case Some(x) if x >= 5 => true
        case _ => false
      }.foreach(println)

...and map...

      l.map {
        case Some(x) => x
        case None => "nothing"
      }.foreach(println)

## Variable assignment

Pattern matching can be also used as a syntactic sugar for declaring variables.

    case class User(name : String, surname : String)

    val list = List(1,2,3,4)

    val head :: tail = list

This will pattern match list into expression *head :: tail* and then assign values to new variables called head and
tail.

# Advanced pattern matching

## Custom pattern matching


As we've seen pattern matching works fine for primitive values, type matching, conditional expressions and lists.
But we can extends it's usage to any other complex type.

Imagine we have a class:

      class Coordinates(x : Int, y : Int)

We would like to be able to match it with pattern matching using values x and y.
In order to do that we have to provide function from the coordinates object to the tuple (x,y).

Then, whenever partial matching will find 'Coordinates' in the case expression, it will apply this function, and
then match the arguments.

    c match {
      case Coordinates(3, 2) => doSomething
    }

Here is how it looks in details:

    //the class
    class ClassWithUnapply(val value: String)

    //companion object with function from the classes instance to matching method args
    object ClassWithUnapply {
        def unapply(unapply: ClassWithUnapply): Option[String] = Some(unapply.value)
    }

    val c = new ClassWithUnapply("test")

    c match {
    case ClassWithUnapply(value) => println(value)
    }


We had to specify unapply function manually.
Fortunately - Scala provides swiss-army-knife for value-objects - **case classes**.

Each case class provides unapply function which is based on it's constructor arguments so if you have case class
like this : User(name : String, surname : String) you can write

    case class User(name : String, surname : String)

    user match {
      case User("John", "Smith) => println("Hello John!")
    }

## Regular expressions

This trick allows us to do more and more powerful things. For example - allows to match regular expressions. Like here:

      //Pattern matching and regex
      val DatePattern: Regex =
        """(\d{2})-(\d{2})-(\d{4})""".r
      val date = "30-04-2016"

      //Extract day, month, year variables using mattern matching
      date match {
        case DatePattern(day, month, year) => println(s"day: $day, month: $month, year: $year")
      }

DatePattern is a regular expression (of Regex) type, Regular expressions support unapplying on capturing arguments.

# Combine it together

Lets define regular expression

    val DatePattern: Regex = """(\d{2})-(\d{2})-(\d{4})""".r

Define some sample input

    val date = "30-04-2016"

Use pattern matching to extract day/month/year from the input. Put the result in three new variables:

    val DatePattern(day, month, year) = date
    println(s"day: $day, month: $month, year: $year")

Powerful, isn't it?'