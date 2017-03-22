package com.fhuertas.monkey.models

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fhuertas.monkey.messages.{CrossedCanyon, _}
import com.fhuertas.monkey.models.Directions._
import com.fhuertas.monkey.utils.Utils
import me.atrox.haikunator.{Haikunator, HaikunatorBuilder}
import scala.language.postfixOps

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scalaz.Reader

class Monkey(val canyon: ActorRef) extends Actor with ActorLogging with MonkeyConfig {
  private val name = Monkey.haikunator.haikunate()

  val objective: Direction = Directions.randomDirection

  log.info(logMsg(s"I'm $name and I'm trying to go to $objective"))

  canyon ! CanICross(objective)

  override def receive: Receive = waiting

  def waiting: Receive = {
    case AreYouReady =>
      log.info(logMsg("I was waiting but now, I'm ready to cross"))
      canyon ! CanICross(objective)
    case CannotCross =>
      val waitingTime = Utils.generateTime(getWaitingTimeMin,getWaitingTimeMax)
      context.system.scheduler.scheduleOnce(waitingTime milliseconds, canyon, CanICross(objective))
    case CanCross =>
      context.become(crossing)
      log.info(logMsg("Crossing the canyon"))
      canyon ! ClimbingRobe
      context.system.scheduler.scheduleOnce(getClimbingRobeTime milliseconds, canyon, CrossingCanyon)
      context.system.scheduler.scheduleOnce(getTotalTime milliseconds, canyon, CrossedCanyon)
    case message => log.info(logMsg(s"I don't understand your message: $message"))
  }

  def crossing: Receive = {
    case _ =>
      log.info(logMsg("I don't hear you because I'm crossing the canyon"))
  }

  private def logMsg(message: String) = s"[$name]: $message"
}

object Monkey {
  lazy val haikunator: Haikunator = new HaikunatorBuilder().setTokenLength(0).setDelimiter("").build()

  val props = Reader {
    (canyon: ActorRef) => Props(classOf[Monkey], canyon)
  }
}
