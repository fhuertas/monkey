package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
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
      val monkey1, monkey2 = TestProbe()
      monkey1.send(canyon, CanICross(East))
      monkey1 expectMsg CanCross
      monkey2.send(canyon, CanICross(West))
      monkey2 expectMsg CannotCross
      expectNoMsg(wait_time)
    }

    "not allow to cross in a direction if other direction is crossing" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      val monkey1, monkey2 = TestProbe()
      monkey1.send(canyon, CanICross(East))
      monkey1 expectMsg CanCross
      monkey1.send(canyon, CrossingCanyon)
      monkey2.send(canyon, CanICross(West))
      monkey2 expectMsg CannotCross
      monkey2 expectNoMsg wait_time
    }

    "free the robe when end to cross" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      val monkey1, monkey2 = TestProbe()
      monkey1.send(canyon, CanICross(East))
      monkey1 expectMsg CanCross
      monkey1.send(canyon, CrossingCanyon)
      monkey1.send(canyon, CrossedCanyon)
      monkey2.send(canyon, CanICross(West))
      monkey2 expectMsg CanCross
      monkey2 expectNoMsg wait_time
    }

    "not allow to use the robe if other monkey is using it" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      val monkey1, monkey2 = TestProbe()
      monkey1.send(canyon, CanICross(East))
      monkey1 expectMsg CanCross
      monkey1.send(canyon, CrossingCanyon)
      monkey2.send(canyon, CanICross(East))
      monkey2 expectMsg CanCross
      monkey2 expectNoMsg wait_time
    }

    "free the robe when end to (without free the robe)" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      val monkey1, monkey2 = TestProbe()
      monkey1.send(canyon, CanICross(East))
      monkey1 expectMsg CanCross
      monkey1.send(canyon, CrossedCanyon)
      monkey2.send(canyon, CanICross(West))
      monkey2 expectMsg CanCross
      monkey2 expectNoMsg wait_time
    }

    "starvation when climbing the robe" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      val monkey1, monkey2 = TestProbe()
      monkey1.send(canyon, CanICross(West))
      monkey1 expectMsg CanCross
      monkey2.send(canyon, CanICross(East))
      monkey2 expectMsg CannotCross
      canyon.underlyingActor.starvationActorRef shouldBe Some(monkey2.ref)
      monkey1.send(canyon, CrossedCanyon)
      monkey2 expectMsg AreYouReady
      canyon.underlyingActor.starvationActorRef shouldBe None
    }

    "starvation when there are monkeys in the robe" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      val monkey1, monkey2 = TestProbe()
      monkey1.send(canyon, CanICross(West))
      monkey1 expectMsg CanCross
      monkey1.send(canyon, CrossingCanyon)
      monkey2.send(canyon, CanICross(East))
      monkey2 expectMsg CannotCross
      canyon.underlyingActor.starvationActorRef shouldBe Some(monkey2.ref)
      monkey1.send(canyon, CrossedCanyon)
      monkey2 expectMsg AreYouReady
      canyon.underlyingActor.starvationActorRef shouldBe None
    }
  }
}
