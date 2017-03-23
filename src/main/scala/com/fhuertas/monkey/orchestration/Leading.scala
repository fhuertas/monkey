package com.fhuertas.monkey.orchestration

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Canyon
import com.fhuertas.monkey.utils.Utils
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaz.Reader

class Leading(canyonProps: Props, monkeyClass: Class[_]) extends Actor with OrchestrationConfig with LazyLogging {

  var monkeysInTheCanyon = Seq.empty[ActorRef]

  val canyon: ActorRef = context.actorOf(canyonProps)

  override def receive: Receive = {
    case NewMonkeyInTheValley(Some(0)) =>
      logger.info("AllMonkeysAreInTheCanyon")
    case NewMonkeyInTheValley(None) =>
      logger.error("Infinite monkeys are not supported yet")
    case NewMonkeyInTheValley(state) =>
      logger.info(s"Leading: New monkey in the valley. Monkeys left = ${state.get}")
      monkeysInTheCanyon = monkeysInTheCanyon :+ context.actorOf(Props(monkeyClass,canyon))
      context.system.scheduler.scheduleOnce(
        Utils.generateTime(getMinTime, getMaxTime) milliseconds,
        self, NewMonkeyInTheValley(newState(state)))
  }

  val a: (Int, Int) = (1, 2)

  private def newState(state: Option[Int]) = state.map(_ - 1)
}

object Leading {
  val props = Reader {
    (canyonAndMonkey: (Props,Class[_])) => Props(classOf[Leading], canyonAndMonkey._1, canyonAndMonkey._2)

  }
}

