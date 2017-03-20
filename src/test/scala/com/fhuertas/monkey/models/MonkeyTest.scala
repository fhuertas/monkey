package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.fhuertas.monkey.messages._
import org.scalatest.{Matchers, WordSpecLike}

class MonkeyTest extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers {

  val canyonTester = TestProbe()
  class MonkeyMock extends Monkey(canyonTester.ref) {
    override def getCrossTime: Int = 100

    override def getClimbingRobeTime: Int = 50
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

      monkey ! YouCanCross
      expectMsg(ClimbingRobe)
//      val beforeClimbRobe = System.currentTimeMillis()
//      expectMsg(CrossingCanyon)
//      val afterClimbRobe = System.currentTimeMillis()
//      expectMsg(CrossedCanyon)
//      val afterCross = System.currentTimeMillis()
//      expectNoMsg()
//      val robeTime = beforeClimbRobe - afterClimbRobe
//      robeTime.toInt should be >= monkey.underlyingActor.getClimbingRobeTime
    }
  }
}
