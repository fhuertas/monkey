package com.fhuertas.monkey.orchestration

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Monkey
import com.typesafe.scalalogging.LazyLogging
import me.atrox.haikunator.HaikunatorBuilder

import scalaz.Reader

class MonkeyLeading(monkeyProps: Props) extends Actor with OrchestrationConfig with LazyLogging {
  //  private def generateName = MonkeyLeading.haikunator.haikunate()
  //  val actorSystem: ActorSystem = ActorSystem("Scheduler-System")



  //  def generateMonkey: Monkey = {
  //    val monkey = Monkey(generateName)
  //    logger.debug(s"New Monkey in the valley. The name ${monkey.name}")
  //    monkey
  //  }
  override def receive: Receive = {
    case NewMonkeyInTheValley =>
      val monkeyRef = context.actorOf(monkeyProps)
      monkeyRef ! YouAreInTheValley
  }
}

object MonkeyLeading {
  val props = Reader {
    (actorClass: Class[_]) => Props(classOf[MonkeyLeading], actorClass)
  }
}

