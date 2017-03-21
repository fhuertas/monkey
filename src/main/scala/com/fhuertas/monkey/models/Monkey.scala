package com.fhuertas.monkey.models

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fhuertas.monkey.messages.{CrossedCanyon, _}
import com.fhuertas.monkey.models.Directions._
import me.atrox.haikunator.{Haikunator, HaikunatorBuilder}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scalaz.Reader

class Monkey(canyon: ActorRef) extends Actor with ActorLogging with MonkeyConfig {
  private val name = Monkey.haikunator.haikunate()

  val objective: Direction = Directions.randomDirection

  log.info(logMsg(s"I'm $name and I'm trying to go to $objective"))

  askCanyon(objective)

  def askCanyon(direction: Direction): Unit = this.canyon ! CanICross(direction)

  override def receive: Receive = waiting

  def waiting: Receive = {
    case YouCanCross =>
      log.info(logMsg("Crossing the canyon"))
      sender ! ClimbingRobe
      context.system.scheduler.scheduleOnce(getClimbingRobeTime milliseconds, sender, CrossingCanyon)
      context.system.scheduler.scheduleOnce(getTotalTime milliseconds, sender, CrossedCanyon)
    case message => log.info(logMsg(s"I don't understand your message: $message"))
  }

  private def logMsg(message: String) = s"[$name]: $message"
}

object Monkey {
  lazy val haikunator: Haikunator = new HaikunatorBuilder().setTokenLength(0).setDelimiter("").build()

  val props = Reader {
    (canyon: ActorRef) => Props(classOf[Monkey], canyon)
  }
}
