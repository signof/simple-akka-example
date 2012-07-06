import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }


trait DelaySerializable extends Serializable

abstract class CalculatorMessage extends DelaySerializable

abstract class MathOp extends CalculatorMessage
abstract class MathResult extends CalculatorMessage

trait SomeOp
case class Sum(numbers :Array[Int]) extends SomeOp
case class Multiply(n1 :Int, n2:Int) extends SomeOp
case class Print(s :String) extends SomeOp

case class Other(op :SomeOp) extends CalculatorMessage

case class Add(n1 :Int, n2 :Int) extends MathOp
case class AddResult(n1 :Int, n2 :Int, r :Int) extends MathResult
case class Subtract(n1 :Int, n2 :Int) extends MathOp
case class SubtractResult(n1 :Int, n2 :Int, r :Int) extends MathResult
case class Product(numbers :Traversable[Int]) extends MathOp
case class ProductResult(numbers :Traversable[Int], r :Int) extends MathOp


class CalculatorActor extends Actor {
  def receive = {
    case Add(n1, n2) => 
      println("Calculating %d + %d".format(n1, n2))
      sender ! AddResult(n1, n2, n1 + n2)
    case Subtract(n1, n2) =>
      println("Calculating %d - %d".format(n1, n2))
      sender ! SubtractResult(n1, n2, n1 - n2)
    case Product(ns :List[Int]) =>
      println("Calculating product of %s".format(ns))
      sender ! ProductResult(ns, ns.product)
    case Other(op) => 
      println("should do "+op)
  }
}


class ClientActor extends Actor {
  def receive = {
    case (actor: ActorRef, op: CalculatorMessage) => 
      actor ! op
    case result: MathResult => result match {
      case AddResult(n1, n2, r) => 
	println("Add result: %d + %d = %d".format(n1, n2, r))
      case SubtractResult(n1, n2, r) => 
	println("Sub result: %d - %d = %d".format(n1, n2, r))
    }
  }
}

object ClientMain { 
   def main(args: Array[String]) { 
     val system = ActorSystem("ClientApplication", ConfigFactory.load.getConfig("client"))
     val actor = system.actorOf(Props[ClientActor], "clientActor")
     val remoteActor = system.actorOf(Props[CalculatorActor], "calculator")
//     actor ! (remoteActor, Add(1,2))
//     actor ! (remoteActor, Other(Multiply(1,1)))
//     actor ! (remoteActor, Other(Sum(Array(1,2))))
     actor ! (remoteActor, Product(List(1,2)))
//     actor ! (remoteActor, Other(Print("Wello horld!")))
   }
}


object CalculatorMain { 
   def main(args: Array[String]) { 
     val system = ActorSystem("CalculatorApplication", ConfigFactory.load.getConfig("calculator"))
     val actor = system.actorOf(Props[CalculatorActor], "Calculator")
   }
}




