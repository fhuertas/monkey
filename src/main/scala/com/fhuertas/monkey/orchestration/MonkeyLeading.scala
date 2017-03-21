package com.fhuertas.monkey.orchestration

import akka.actor.{Actor, ActorLogging, Props}
import com.fhuertas.monkey.messages._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scalaz.Reader

class MonkeyLeading(monkeyProps: Props) extends Actor with OrchestrationConfig with ActorLogging {
  override def receive: Receive = {
    case NewMonkeyInTheValley(Some(0)) =>
      log.info("The simulation has finished")
    case NewMonkeyInTheValley(None) =>
      log.error("Infinite monkeys are not supported yet")
    case NewMonkeyInTheValley(state) =>
      val monkeyRef = context.actorOf(monkeyProps)
      monkeyRef ! YouAreInTheValley
      context.system.scheduler.scheduleOnce(generateTime milliseconds, self, NewMonkeyInTheValley(newState(state)))
  }

  private def newState(state: Option[Int]) = state.map(_ - 1)

  def generateTime: Int = {
    scala.util.Random.nextInt(getMaxTime - getMinTime) + getMinTime + 1
  }

}

object MonkeyLeading {
  val props = Reader {
    (actorClass: Class[_]) => Props(classOf[MonkeyLeading], actorClass)
  }
}

