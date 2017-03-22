package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.fhuertas.monkey.orchestration.OrchestrationConfig
import org.scalatest.{Matchers, WordSpecLike}

class CanyonMonkeyIT extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with OrchestrationConfig{

  "Canyon and Monkey" should {
    "orchestrate" in {
      val canyon = TestActorRef(Canyon.props)
      val monkeys = for {
        i <- 1 to getNumMonkeys
        monkey = TestActorRef(Monkey.props(canyon))
      } yield monkey
    }
  }
}
