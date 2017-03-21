package com.fhuertas.monkey.orchestration

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Monkey
import com.typesafe.scalalogging.LazyLogging
import me.atrox.haikunator.HaikunatorBuilder
import scala.concurrent.duration._
import scalaz.{Reader, State}
import scala.concurrent.ExecutionContext.Implicits.global

class MonkeyLeading(monkeyProps: Props) extends Actor with OrchestrationConfig with ActorLogging {
  //  private def generateName = MonkeyLeading.haikunator.haikunate()
  //  val actorSystem: ActorSystem = ActorSystem("Scheduler-System")



  //  def generateMonkey: Monkey = {
  //    val monkey = Monkey(generateName)
  //    logger.debug(s"New Monkey in the valley. The name ${monkey.name}")
  //    monkey
  //  }

  override def receive: Receive = {
    case NewMonkeyInTheValley(Some(0)) =>
      log.info("The simulation has finished")
    case NewMonkeyInTheValley(None) =>
      throw new UnsupportedOperationException("Infinite monkeys are not supported yet")
    case NewMonkeyInTheValley(state) =>
      val monkeyRef = context.actorOf(monkeyProps)
      monkeyRef ! YouAreInTheValley
      context.system.scheduler.scheduleOnce(generateTime milliseconds,self,NewMonkeyInTheValley(newState(state)))
  }

  private def newState(state: Option[Int]) = state.map(_-1)

  def generateTime: Int = {
    scala.util.Random.nextInt(getMaxTime-getMinTime)+getMinTime
  }

}

object MonkeyLeading {
  val props = Reader {
    (actorClass: Class[_]) => Props(classOf[MonkeyLeading], actorClass)
  }
}

