package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Directions._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._

class CanyonTest extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers {

  val wait_time = 50 millis

  "Canyon" should {
    "Receive messages" in {
      val canyon = system.actorOf(Canyon.props)
      canyon ! "message"
      expectNoMsg(wait_time)
    }

    "response with CanCross to the ask CanICross" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(West)
      expectMsg(CanCross)
      expectNoMsg(wait_time)
    }

    "store the direction that the monkey are crossing" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East)
      expectMsg(CanCross)
      expectNoMsg(wait_time)
    }

    "not allow to cross in a direction if other monkey are in the robe" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East)
      expectMsg(CanCross)
      canyon ! CanICross(West)
      expectMsg(CannotCross)
      expectNoMsg(wait_time)
    }

    "not allow to cross in a direction if other direction is crossing" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East)
      expectMsg(CanCross)
      canyon ! CrossingCanyon
      canyon ! CanICross(West)
      expectMsg(CannotCross)
      expectNoMsg(wait_time)
    }

    "free the robe when end to cross" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East)
      expectMsg(CanCross)
      canyon ! CrossingCanyon
      canyon ! CrossedCanyon
      canyon ! CanICross(West)
      expectMsg(CanCross)
      expectNoMsg(wait_time)
    }

    "not allow to use the robe if other monkey is using it" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East)
      expectMsg(CanCross)
      canyon ! CrossingCanyon
      canyon ! CanICross(East)
      expectMsg(CanCross)
      expectNoMsg(wait_time)
    }

    "free the robe when end to (without free the robe)" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East)
      expectMsg(CanCross)
      canyon ! CrossedCanyon
      canyon ! CanICross(West)
      expectMsg(CanCross)
      expectNoMsg(wait_time)
    }

    "starvation when climbing the robe" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(West)
      expectMsg(CanCross)
      canyon ! CanICross(East)
      expectMsg(CannotCross)
      canyon.underlyingActor.starvationActorRef shouldBe Some(testActor)
      canyon ! CrossedCanyon
      expectMsg(AreYouReady)
      canyon.underlyingActor.starvationActorRef shouldBe None
    }

    "starvation when there are monkeys in the robe" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(West)
      expectMsg(CanCross)
      canyon ! CrossingCanyon
      canyon ! CanICross(East)
      expectMsg(CannotCross)
      canyon.underlyingActor.starvationActorRef shouldBe Some(testActor)
      canyon ! CrossedCanyon
      expectMsg(AreYouReady)
      canyon.underlyingActor.starvationActorRef shouldBe None
    }
  }
}
