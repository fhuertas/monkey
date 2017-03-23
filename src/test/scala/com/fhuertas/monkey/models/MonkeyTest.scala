package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.fhuertas.monkey.messages._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._

class MonkeyTest extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers {

  val canyonTester = TestProbe()

  val wait_time = 50 millis

  class MonkeyMock extends Monkey(canyonTester.ref) {
    override def getCrossTime: Int = 100

    override def getClimbingRobeTime: Int = 50

    override def getWaitingTimeMin: Int = 10

    override def getWaitingTimeMax: Int = 22
  }


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
      robeTime should be >= monkey.underlyingActor.getClimbingRobeTime.toLong
      totalTime should be >= monkey.underlyingActor.getTotalTime.toLong
    }

    "The monkey only should attend to one cross messaging" in {
      val monkey = TestActorRef[Monkey](new MonkeyMock)
      canyonTester expectMsgAllClassOf classOf[CanICross]
      monkey ! CanCross
      monkey ! CanCross
      canyonTester expectMsg ClimbingRobe
      canyonTester expectMsg CrossingCanyon
      canyonTester expectMsg CrossedCanyon
      canyonTester expectNoMsg wait_time
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

    "send a CanICross message after receive AreYouReady where it is waiting" in {
      val monkey = TestActorRef[Monkey](new MonkeyMock)
      val timeout = 5 millis
      val direction = monkey.underlyingActor.objective
      canyonTester expectMsgAllClassOf classOf[CanICross]
      monkey ! AreYouReady
      canyonTester expectMsg CanICross(direction)
    }
    "change to crossed state when it has crossed" in {
      val monkey = TestActorRef[Monkey](new MonkeyMock)
      canyonTester expectMsgAllClassOf classOf[CanICross]
      monkey ! CanCross
      canyonTester expectMsgAllOf(ClimbingRobe,CrossingCanyon,CrossedCanyon)
      canyonTester.send(monkey,YouAreInTheOtherSide)
      monkey ! "A message"
      expectMsg(IHaveCrossed)
    }
  }
}
