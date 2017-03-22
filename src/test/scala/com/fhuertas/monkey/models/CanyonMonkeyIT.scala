package com.fhuertas.monkey.models

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.fhuertas.monkey.common.ConfigComponent
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.orchestration.OrchestrationConfig
import org.scalatest.{Matchers, WordSpecLike}

import scalaz.Reader

class MonkeyChangingTimes(canyon: ActorRef) extends Monkey(canyon) {
  override def getCrossTime: Int = super.getCrossTime / CanyonMonkeyIT.factor

  override def getClimbingRobeTime: Int = super.getClimbingRobeTime / CanyonMonkeyIT.factor

  override def getWaitingTimeMin: Int = super.getWaitingTimeMin / CanyonMonkeyIT.factor

  override def getWaitingTimeMax: Int = super.getWaitingTimeMax / CanyonMonkeyIT.factor
}

object MonkeyChangingTimes {
  val props = Reader {
    (canyon: ActorRef) => Props(classOf[MonkeyChangingTimes], canyon)
  }
}

class CanyonMonkeyIT extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with ConfigComponent {

  def getMaxTime: Int = (getInt(MonkeyConfig.KEY_CROSS_TIME).get +
        getInt(MonkeyConfig.KEY_ROPE_TIME).get +
        getInt(OrchestrationConfig.KEY_MAX_TIME).get) / CanyonMonkeyIT.factor

  def getNumMonkeys: Int = getInt(OrchestrationConfig.KEY_NUM_MONKEYS).get


  "Canyon and Monkey" should {
    "orchestrate" in {
      val canyon = TestActorRef(Canyon.props)
      val monkeys = for {
        i <- 1 to getNumMonkeys
        monkey = TestActorRef(MonkeyChangingTimes.props(canyon))
      } yield monkey

      Thread.sleep(getNumMonkeys * getMaxTime)

      for {
        monkey <- monkeys
      } yield monkey ! WhereAreYou
      expectMsg(CrossedCanyon)
    }
  }
}

object CanyonMonkeyIT {
  val factor: Int = 25
}
