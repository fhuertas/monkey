package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.orchestration.OrchestrationConfig
import com.fhuertas.monkey.utils.{FastConfiguration, FastMonkey}
import org.scalatest.{Matchers, WordSpecLike}

class CanyonMonkeyIT extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with FastConfiguration {

  def getMaxTime: Int = getInt(MonkeyConfig.KEY_CROSS_TIME).get +
    getInt(MonkeyConfig.KEY_ROPE_TIME).get +
    getInt(OrchestrationConfig.KEY_MAX_TIME).get

  def getNumMonkeys: Int = getInt(OrchestrationConfig.KEY_NUM_MONKEYS).get


  "Canyon and Monkey" should {
    "orchestrate" in new FastConfiguration {

      val canyon = TestActorRef(Canyon.props)
      val monkeys = for {
        i <- 1 to getNumMonkeys
        monkey = TestActorRef(FastMonkey.props(canyon))
      } yield monkey

      Thread.sleep(getNumMonkeys * getMaxTime)

      for {
        monkey <- monkeys
      } yield monkey ! WhereAreYou
      expectMsg(IHaveCrossed)
    }
  }
}

