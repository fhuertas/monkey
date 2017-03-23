package com.fhuertas.monkey.models

import akka.actor.{ActorSystem, Terminated}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.utils.FastConfiguration
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._

class MonkeyTest extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers {

  val canyonTester = TestProbe()

  val wait_time = 50 millis

  class MonkeyMock extends Monkey(canyonTester.ref) with FastConfiguration


  "A Monkey" should {
    "receive a message" in {
      val canyonActor = TestProbe()
      val monkey = system.actorOf(Monkey.props(canyonActor.ref))
      monkey ! "message"
    }

    "look if it can cross the canyon with the correct side" in {
      val canyonActor = TestProbe()

      val monkey = TestActorRef[Monkey](Monkey.props(canyonActor.ref))

      val direction = monkey.underlyingActor.objective

      canyonActor expectMsg CanICross(direction)
    }

    "cross the canyon when receive a message that can receive and report the steps with the correct times" in {
      val monkey = TestActorRef[Monkey](new MonkeyMock)
      watch(monkey)
      val originalClimbingRobeTime = monkey.underlyingActor.getClimbingRobeTime.toLong
      val originalTotalTime = monkey.underlyingActor.getTotalTime.toLong
      canyonTester expectMsgAllClassOf classOf[CanICross]
      monkey ! CanCross
      canyonTester expectMsg ClimbingRobe
      val beforeClimbRobe = System.currentTimeMillis()
      canyonTester expectMsg CrossingCanyon
      val afterClimbRobe = System.currentTimeMillis()
      canyonTester expectMsg CrossedCanyon
      val afterCross = System.currentTimeMillis()
      canyonTester expectNoMsg wait_time
      val robeTime = afterClimbRobe - beforeClimbRobe
      val totalTime = afterCross - beforeClimbRobe
      robeTime should be >= originalClimbingRobeTime
      totalTime should be >= originalTotalTime
    }

    "The monkey only should attend to one cross messaging" in {
      val monkey = TestActorRef[Monkey](new MonkeyMock)
      canyonTester expectMsgAllClassOf classOf[CanICross]
      watch(monkey)
      monkey ! CanCross
      monkey ! CanCross
      canyonTester expectMsgAllOf(ClimbingRobe,CrossingCanyon,CrossedCanyon)
      canyonTester expectNoMsg wait_time
      expectMsgClass(classOf[Terminated])
    }

    "if cannot cross, try again after a while" in {
      val monkey = TestActorRef[Monkey](new MonkeyMock)
      canyonTester expectMsgAllClassOf classOf[CanICross]
      val before1 = System.currentTimeMillis()
      monkey ! CannotCross
      canyonTester expectMsg CanICross(monkey.underlyingActor.objective)
      val after = System.currentTimeMillis()
      after - before1 should be >= monkey.underlyingActor.getWaitingTimeMin.toLong
    }

    "A terminated message when it finish" in {
      val monkey = TestActorRef[Monkey](new MonkeyMock)
      val direction = monkey.underlyingActor.objective
      canyonTester expectMsgAllClassOf classOf[CanICross]
      monkey ! AreYouReady
      canyonTester expectMsg CanICross(direction)
    }
    "change to crossed state when it has crossed" in {
      val monkey = TestActorRef[Monkey](new MonkeyMock)
      canyonTester expectMsgAllClassOf classOf[CanICross]
      monkey ! CanCross
      canyonTester expectMsgAllOf(ClimbingRobe, CrossingCanyon, CrossedCanyon)
      expectMsgClass(classOf[Terminated])
    }
  }
}
