package com.fhuertas.monkey.models

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Directions._
import com.typesafe.scalalogging.LazyLogging

class Canyon extends Actor with LazyLogging {

  override def receive: Receive = empty

  var starvationActorRef: Option[ActorRef] = None

  // starvation condition partial function
  private def starvationCondition: Receive = {
    case _ if starvationActorRef.isDefined =>
      logger.debug(logMsg(s"At least a monkey has starvation. You should wait"))
      sender ! CannotCross
  }

  // starvation detection partial function
  private def cannotCrossAndDetectStarvation(direction: Direction): Receive = {
    case CanICross(newDirection) if newDirection != direction =>
      logger.debug(
        logMsg(s"You cannot cross to $newDirection because there are monkeys in the other direction"))
      starvationActorRef = Option(sender)
      sender ! CannotCross
    case CanICross(newDirection) =>
      logger.debug(
        logMsg(s"You cannot cross to $newDirection. "))
      sender ! CannotCross
  }

  private def empty: Receive = {
    case CanICross(direction) =>
      logger.debug(logMsg(s"A monkey is trying to cross the canyon to $direction"))
      context.become(receiveClimbingRobe(direction, incrementMonkeys(Set.empty[ActorRef], sender)))
      sender ! CanCross
    case message => logger.debug(logMsg(s"I don't understand you: $message"))
  }

  private def receiveClimbingRobe(direction: Direction, monkeys: Set[ActorRef]): Receive =
    climbingRobeOtherMonkeys(direction, monkeys) orElse
      lastMonkeyCrossingCanyon(direction) orElse
      starvationCondition orElse
      cannotCrossAndDetectStarvation(direction)

  private def climbingRobeOtherMonkeys(direction: Direction, monkeys: Set[ActorRef]): Receive = {
    case CrossingCanyon =>
      logger.info(logMsg(s"You are now in the robe to cross to $direction, Be aware. Monkeys = ${monkeys.size}"))
      context.become(receiveCrossing(direction, monkeys))
    case CrossedCanyon if monkeys.size > 1 =>
      sender ! YouAreInTheOtherSide
      val monkeysInTheRobe = decrementMonkeys(monkeys,sender)
      logger.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). " +
        s"There are monkeys in the robe. Monkeys = ${monkeysInTheRobe.size}"))
      context.become(receiveClimbingRobe(direction, monkeysInTheRobe))
  }

  private def lastMonkeyCrossingCanyon(direction: Direction): Receive = {
    case CrossedCanyon =>
      sender ! YouAreInTheOtherSide
      logger.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). The robe is empty"))
      starvationActorRef.foreach(actorRef => actorRef ! AreYouReady)
      starvationActorRef = None
      context.become(empty)
  }


  private def receiveCrossing(direction: Direction, numMonkeys: Set[ActorRef]): Receive =
    crossing(direction, numMonkeys) orElse
      lastMonkeyCrossingCanyon(direction) orElse
      starvationCondition orElse
      cannotCrossAndDetectStarvation(direction)

  // Crossing partial function
  private def crossing(direction: Direction, monkeys: Set[ActorRef]): Receive = {
    case CanICross(newDirection) if direction.equals(newDirection) && starvationActorRef.isEmpty =>
      logger.debug(logMsg(s"A monkey is trying to cross the canyon to $direction"))
      val monkeysInTheRobe = incrementMonkeys(monkeys,sender)
      logger.info(logMsg(s"You can cross to $newDirection, but the robe is being used. Be aware. Monkeys = ${monkeysInTheRobe.size}"))
      context.become(receiveClimbingRobe(direction, monkeysInTheRobe))
      sender ! CanCross
    case CrossedCanyon if monkeys.size > 1 =>
      sender ! YouAreInTheOtherSide
      val monkeysInTheRobe = decrementMonkeys(monkeys,sender)
      logger.info(logMsg(s"Congratulation. A monkey is in the other side ($direction). " +
        s"There are monkeys in the robe. Monkeys = ${monkeysInTheRobe.size}"))
      sender ! YouAreInTheOtherSide
      context.become(receiveCrossing(direction, monkeysInTheRobe))
  }

  private def logMsg(msg: String) = s"<canyon>: $msg"

  private def incrementMonkeys(initialMonkeys: Set[ActorRef], newMonkey: ActorRef) =
    initialMonkeys + newMonkey

  private def decrementMonkeys(initialMonkeys: Set[ActorRef], monkey: ActorRef) =
    initialMonkeys - monkey
}

object Canyon {
  val props = Props(classOf[Canyon])
}
