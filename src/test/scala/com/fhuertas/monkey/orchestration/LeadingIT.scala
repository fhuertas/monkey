package com.fhuertas.monkey.orchestration

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.{Canyon, MonkeyConfig}
import com.fhuertas.monkey.utils.{FastConfiguration, FastLeading, FastMonkey}
import org.scalatest.{Matchers, WordSpecLike}

/**
  * Created by fhuertas on 22/03/17.
  */
class LeadingIT extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with FastConfiguration {

  def getMaxTime: Int = getInt(MonkeyConfig.KEY_CROSS_TIME).get +
    getInt(MonkeyConfig.KEY_ROPE_TIME).get +
    getInt(OrchestrationConfig.KEY_MAX_TIME).get

  def getNumMonkeys: Int = getInt(OrchestrationConfig.KEY_NUM_MONKEYS).get

  "Leading" should {
    "recreate a scenario with a least 1 monkey" in {
      val leading = TestActorRef[Leading](new Leading(Canyon.props, classOf[FastMonkey]))
      leading ! NewMonkeyInTheValley(Option(1))

      Thread.sleep(getMaxTime)

      leading.underlyingActor.monkeysInTheCanyon.foreach(monkey => {
        monkey ! WhereAreYou
        expectMsg(IHaveCrossed)
      })


    }
    "recreate a scenario with monkeys" in {
      val leading = TestActorRef[Leading](new FastLeading(Canyon.props, classOf[FastMonkey]))
      leading ! NewMonkeyInTheValley(Option(getNumMonkeys))

      Thread.sleep(getMaxTime * (getNumMonkeys + 1))

      leading.underlyingActor.monkeysInTheCanyon.foreach(monkey => {
        monkey ! WhereAreYou
        expectMsg(IHaveCrossed)
      })
    }
  }
}
