package com.fhuertas.monkey.models

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Directions._

class Canyon extends Actor with ActorLogging {

  override def receive: Receive = empty

  var starvationActorRef: Option[ActorRef] = None

  // starvation condition partial function
  private def starvationCondition: Receive = {
    case _ if starvationActorRef.isDefined =>
      log.debug(logMsg(s"At least a monkey has starvation. You should wait"))
      sender ! CannotCross
  }

  // starvation detection partial function
  private def cannotCrossAndDetectStarvation(direction: Direction): Receive = {
    case CanICross(newDirection) if newDirection != direction =>
      log.debug(
        logMsg(s"You cannot cross to $newDirection because there are monkeys in the other direction"))
      starvationActorRef = Option(sender)
      sender ! CannotCross
    case CanICross(newDirection) =>
      log.debug(
        logMsg(s"You cannot cross to $newDirection. "))
      sender ! CannotCross
  }

  private def empty: Receive = {
    case CanICross(direction) =>
      log.info(logMsg(s"New monkey in the canyon. It goes to $direction"))
      context.become(receiveClimbingRobe(direction, incrementMonkeys(0)))
      sender ! CanCross
    case message => log.debug(logMsg(s"I don't understand you: $message"))
  }

  private def receiveClimbingRobe(direction: Direction, numMonkeys: Int): Receive =
    climbingRobeOtherMonkeys(direction, numMonkeys) orElse
      lastMonkeyCrossingCanyon(direction) orElse
      starvationCondition orElse
      cannotCrossAndDetectStarvation(direction)

  private def climbingRobeOtherMonkeys(direction: Direction, numMonkeys: Int): Receive = {
    case CrossingCanyon =>
      log.debug(logMsg(s"You are now in the robe to cross to $direction, Be aware"))
      context.become(receiveCrossing(direction, numMonkeys))
    case CrossedCanyon if numMonkeys > 1 =>
      val monkeysInTheRobe = decrementMonkeys(numMonkeys)
      log.debug(logMsg(s"Congratulation. A monkey is in the other side ($direction). " +
        s"There are ($monkeysInTheRobe) monkeys in the robe"))
      context.become(receiveClimbingRobe(direction, monkeysInTheRobe))
  }

  private def lastMonkeyCrossingCanyon(direction: Direction): Receive = {
    case CrossedCanyon =>
      log.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). The robe is empty"))
      starvationActorRef.foreach(actorRef => actorRef ! AreYouReady)
      starvationActorRef = None
      context.become(empty)
  }


  private def receiveCrossing(direction: Direction, numMonkeys: Int): Receive =
    crossing(direction, numMonkeys) orElse
      lastMonkeyCrossingCanyon(direction) orElse
      starvationCondition orElse
      cannotCrossAndDetectStarvation(direction)

  // Crossing partial function
  private def crossing(direction: Direction, numMonkeys: Int): Receive = {
    case CanICross(newDirection) if direction.equals(newDirection) && starvationActorRef.isEmpty =>
      val monkeysInTheRobe = incrementMonkeys(numMonkeys)
      log.debug(logMsg(s"You can cross to $newDirection, but the robe is being ($monkeysInTheRobe) used. Be aware"))
      context.become(receiveClimbingRobe(direction, monkeysInTheRobe))
      sender ! CanCross
    case CrossedCanyon if numMonkeys > 1 =>
      val monkeysInTheRobe = decrementMonkeys(numMonkeys)
      log.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). There are ($monkeysInTheRobe) monkeys " +
        s"in the robe"))
      context.become(receiveCrossing(direction, monkeysInTheRobe))
  }

  private def logMsg(msg: String) = s"[canyon]: $msg"

  private def incrementMonkeys(initialMonkeys: Int) = initialMonkeys + 1

  private def decrementMonkeys(initialMonkeys: Int) = initialMonkeys - 1
}

object Canyon {
  val props = Props(classOf[Canyon])
}
