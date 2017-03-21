package com.fhuertas.monkey.models

import akka.actor.{Actor, ActorLogging, Props}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Directions._

class Canyon extends Actor with ActorLogging {

  override def receive: Receive = empty

  private def empty: Receive = {
    case CanICross(direction) =>
      log.info(logMsg(s"New monkey in the canyon. It goes to $direction"))
      context.become(climbingRobe(direction, incrementMonkeys()))
      sender ! CanCross
    case message => log.debug(logMsg(s"I don't understand you: $message"))
  }

  private def climbingRobe(direction: Direction, numMonkeys: Int): Receive = {
    case CrossingCanyon =>
      log.info(logMsg(s"You are now in the robe to cross to $direction, Be aware"))
      context.become(crossing(direction, numMonkeys))
    case _ =>
      log.info(logMsg(s"Sorry, the robe is being used and cannot cross to $direction"))
      sender ! CannotCross
  }

  private def crossing(direction: Direction,numMonkeys: Int): Receive = {
    case CanICross(newDirection) if direction.equals(newDirection) =>
      log.info(logMsg(s"You can cross, but the robe is being used. Be aware"))
      context.become(climbingRobe(direction,incrementMonkeys()))
      sender ! CanCross
    case CrossedCanyon if numMonkeys > 1 =>
      log.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). There are ($numMonkeys) monkeys " +
        s"in the robe"))
      context.become(crossing(direction,decrementMonkeys(numMonkeys)))
    case CrossedCanyon =>
      log.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). The robe is empty"))
      context.become(empty)
    case _ =>
      log.info(logMsg(s"Sorry, you cannot cross to $direction because the robe is being used"))
      sender ! CannotCross
  }

  private def logMsg(msg: String) = s"[canyon]: $msg"

  private def incrementMonkeys(initialMonkeys: Int = 0) = initialMonkeys + 1

  private def decrementMonkeys(initialMonkeys: Int = 0) = initialMonkeys + 1
}

object Canyon {
  val props = Props(classOf[Canyon])
}
