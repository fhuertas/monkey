package com.fhuertas.monkey.models

import akka.actor.{Actor, ActorLogging, Props}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Directions._

class Canyon extends Actor with ActorLogging {

  override def receive: Receive = empty

  private def empty: Receive = {
    case CanICross(direction) =>
      log.info(logMsg(s"New monkey in the canyon. It goes to $direction"))
      context.become(climbingRobe(direction, incrementMonkeys(0)))
      sender ! CanCross
    case message => log.debug(logMsg(s"I don't understand you: $message"))
  }

  private def climbingRobe(direction: Direction, numMonkeys: Int)(): Receive = {
    case CrossingCanyon =>
      log.info(logMsg(s"You are now in the robe to cross to $direction, Be aware"))
      context.become(crossing(direction, numMonkeys))
    case CrossedCanyon if numMonkeys > 1 =>
      val monkeysInTheRobe = decrementMonkeys(numMonkeys)
      log.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). " +
        s"There are ($monkeysInTheRobe) monkeys in the robe"))
      context.become(climbingRobe(direction, monkeysInTheRobe))
    case CrossedCanyon =>
      log.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). The robe is empty"))
      context.become(empty)
    case CanICross(newDirection) =>
      log.info(
        logMsg(s"You cannot cross ($newDirection) because other monkeys ($numMonkeys) is climbing to the robe,"))
      sender ! CannotCross
  }

  private def crossing(direction: Direction, numMonkeys: Int)(): Receive = {
    case CanICross(newDirection) if direction.equals(newDirection) =>
      val monkeysInTheRobe = incrementMonkeys(numMonkeys)
      log.info(logMsg(s"You can cross to $newDirection, but the robe is being ($monkeysInTheRobe) used. Be aware"))
      context.become(climbingRobe(direction, monkeysInTheRobe))
      sender ! CanCross
    case CrossedCanyon if numMonkeys > 1 =>
      val monkeysInTheRobe = decrementMonkeys(numMonkeys)
      log.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). There are ($monkeysInTheRobe) monkeys " +
        s"in the robe"))
      context.become(crossing(direction, monkeysInTheRobe))
    case CrossedCanyon =>
      log.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). The robe is empty"))
      context.become(empty)
  }

  private def logMsg(msg: String) = s"[canyon]: $msg"

  private def incrementMonkeys(initialMonkeys: Int) = initialMonkeys + 1

  private def decrementMonkeys(initialMonkeys: Int) = initialMonkeys - 1
}

object Canyon {
  val props = Props(classOf[Canyon])
}
