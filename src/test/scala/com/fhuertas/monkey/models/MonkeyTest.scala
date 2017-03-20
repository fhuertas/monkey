package com.fhuertas.monkey.models

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{Matchers, WordSpecLike}

class MonkeyTest extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers {
  "A Monkey" should {
    "receive a message" in {
      val monkey = system.actorOf(Props(classOf[Monkey]))
      monkey ! "message"

    }
  }
}
