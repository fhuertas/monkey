package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Directions._
import org.scalatest.{Matchers, WordSpecLike}

class CanyonTest extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers {
  "Canyon" should {
    "Receive messages" in {
      val canyon = system.actorOf(Canyon.props)
      canyon ! "message"
      expectNoMsg()
    }

    "response with CanCross to the ask CanICross" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(West)
      expectMsg(CanCross)
      expectNoMsg()
    }

    "store the direction that the monkey are crossing" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East)
      expectMsg(CanCross)
      expectNoMsg()
    }

    "not allow to cross in a direction if other direction is crossing" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East)
      expectMsg(CanCross)
      canyon ! CanICross(West)
      expectMsg(CannotCross)
      expectNoMsg()
    }

    "free the robe when end to cross" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East)
      expectMsg(CanCross)
      canyon ! CrossingCanyon
      canyon ! CrossedCanyon
      canyon ! CanICross(West)
      expectMsg(CanCross)
      expectNoMsg()
    }

    "not allow to use the robe if other monkey is using it" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East)
      expectMsg(CanCross)
      canyon ! CrossingCanyon
      canyon ! CanICross(East)
      expectMsg(CanCross)
      expectNoMsg()
    }
  }
}
