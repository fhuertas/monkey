package com.fhuertas.monkey.orchestration

import akka.actor.{ActorSystem, Terminated}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.{Canyon, MonkeyConfig}
import com.fhuertas.monkey.utils.{FastConfiguration, FastLeading, FastMonkey}
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._

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
    "recreate a scenario with a least 1 monkey" in new TestKit(ActorSystem("MySpec-1")) {
      var monkeys = 0
      val leading = TestActorRef[Leading](new FastLeading(Canyon.props, classOf[FastMonkey]) {
        override def getNumMonkeys: Int = 1

        override def createMonkey: Unit = {
          monkeys += 1
          super.createMonkey
        }
      })
      leading ! StartSimulation
      watch(leading)
      monkeys shouldBe 1
      expectMsgClass(classOf[Terminated])
    }
    "recreate a scenario with monkeys" in new TestKit(ActorSystem("MySpec-2")) {
      var monkeys = 0
      val leading = TestActorRef[Leading](new FastLeading(Canyon.props, classOf[FastMonkey]) {
        override def createMonkey: Unit = {
          monkeys += 1
          super.createMonkey
        }
      })
      leading ! StartSimulation
      watch(leading)
      expectMsgClass(getMaxTime * (getNumMonkeys + 1) millis, classOf[Terminated])
      monkeys shouldBe getNumMonkeys
    }

    "with 0 or less monkey there is no simulation" in new TestKit(ActorSystem("MySpec-3")) {
      var monkeys = 0
      val leading = TestActorRef[Leading](new FastLeading(Canyon.props, classOf[FastMonkey]) {
        override def getNumMonkeys: Int = 0

        override def createMonkey: Unit = {
          monkeys += 1
          super.createMonkey
        }
      })
      leading ! NewMonkeyInTheValley(Option(0))
      leading ! StartSimulation
      watch(leading)
      expectMsgClass(classOf[Terminated])
      monkeys shouldBe 0
    }
  }
}
