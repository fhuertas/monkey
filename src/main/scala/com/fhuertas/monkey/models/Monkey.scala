package com.fhuertas.monkey.models

import akka.actor.{Actor, ActorRef, Props}
import com.fhuertas.monkey.messages.{CrossedCanyon, _}
import com.fhuertas.monkey.models.Directions._
import com.fhuertas.monkey.utils.Utils
import com.typesafe.scalalogging.LazyLogging
import me.atrox.haikunator.{Haikunator, HaikunatorBuilder}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps
import scalaz.Reader


class Monkey(val canyon: ActorRef) extends Actor with LazyLogging with MonkeyConfig {

  private val name = Monkey.haikunator.haikunate()

  val objective: Direction = Directions.randomDirection

  logger.info(logMsg(s"I'm $name and I'm trying to go to $objective"))

  canyon ! CanICross(objective)

  override def receive: Receive = waiting

  def waiting: Receive = {
    case AreYouReady =>
      logger.info(logMsg(s"I was waiting but now, I'm ready to cross to $objective"))
      canyon ! CanICross(objective)
    case CannotCross =>
      logger.debug(logMsg(s"I Cannot cross to $objective"))
      val waitingTime = Utils.generateTime(getWaitingTimeMin, getWaitingTimeMax)
      context.system.scheduler.scheduleOnce(waitingTime milliseconds, canyon, CanICross(objective))
    case CanCross =>
      context.become(crossing)
      logger.info(logMsg("Taking the robe"))
      canyon ! ClimbingRobe
      context.system.scheduler.scheduleOnce(getClimbingRobeTime milliseconds, canyon, CrossingCanyon)
      context.system.scheduler.scheduleOnce(getTotalTime milliseconds, self, CrossedCanyon)
      context.system.scheduler.scheduleOnce(getTotalTime milliseconds, canyon, CrossedCanyon)
    case message => logger.info(logMsg(s"I don't understand your message: $message"))
  }

  def crossing: Receive = {
    case CrossedCanyon =>
      logger.info(logMsg(s"I just cross. I'm in $objective"))
      context.stop(sender)
    case _ =>
      logger.info(logMsg("I don't hear you because I'm crossing the canyon"))
  }

  private def logMsg(message: String) = s"[$name]: $message"
}

object Monkey {
  lazy val haikunator: Haikunator = new HaikunatorBuilder().setTokenLength(0).setDelimiter("").build()

  val props = Reader {
    (canyon: ActorRef) => Props(classOf[Monkey], canyon)
  }
}
