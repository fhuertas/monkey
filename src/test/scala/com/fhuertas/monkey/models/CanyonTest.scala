package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.fhuertas.monkey.messages._
import org.scalatest.{Matchers, WordSpecLike}

class CanyonTest extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers {
  "Canyon" should {
    "Receive messages" in {
      val canyon = system.actorOf(Canyon.props)
      canyon ! "message"
    }

    "response with CanCross to the ask CanICross" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross
      expectMsg(CanCross)
    }
  }
}
