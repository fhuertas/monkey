package com.fhuertas.monkey.orchestration

import akka.actor.{Actor, ActorRef, Props, Terminated}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.utils.Utils
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaz.Reader

class Leading(canyonProps: Props, monkeyClass: Class[_]) extends Actor with OrchestrationConfig with LazyLogging {

  var monkeysInTheCanyon = Set.empty[ActorRef]

  val canyon: ActorRef = context.actorOf(canyonProps)

  def waiting: Receive = {
    case StartSimulation =>
      context.become(simulating)
      self ! NewMonkeyInTheValley(Option(getNumMonkeys))

  }

  def simulating: Receive = {
    case NewMonkeyInTheValley(Some(1)) =>
      logger.info("The last monkey is in the canyon")
      createMonkey
      context.become(waitingTheEnd)
    case NewMonkeyInTheValley(state) if state.get > 1 =>
      logger.info(s"Leading: New monkey in the valley. Monkeys left = ${state.get}")
      createMonkey
      context.system.scheduler.scheduleOnce(
        Utils.generateTime(getMinTime, getMaxTime) milliseconds,
        self, NewMonkeyInTheValley(newState(state)))
    case NewMonkeyInTheValley(Some(_)) =>
      logger.info("are There are 0 or less monkeys? Is this possible?")
      stopThisActor
    case Terminated(_) =>
  }

  def createMonkey: Unit = {
    context.watch(context.actorOf(Props(monkeyClass, canyon)))
  }

  def waitingTheEnd: Receive = {
    case Terminated(_) if context.children.size == 1 =>
      logger.info("The simulation has end")
      stopThisActor
  }

  def stopThisActor: Unit = {
    context.stop(canyon)
    context.stop(self)
    context.stop(context.parent)
  }


  override def receive: Receive = waiting

  val a: (Int, Int) = (1, 2)

  private def newState(state: Option[Int]) = state.map(_ - 1)
}

object Leading {
  val props = Reader {
    (canyonAndMonkey: (Props, Class[_])) => Props(classOf[Leading], canyonAndMonkey._1, canyonAndMonkey._2)

  }
}

