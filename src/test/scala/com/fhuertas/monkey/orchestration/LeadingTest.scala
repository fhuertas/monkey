package com.fhuertas.monkey.orchestration

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.fhuertas.monkey.messages._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._
import scalaz.Reader

case object NewMonkey

class MonkeyMock(testerActor: ActorRef) extends Actor {
  testerActor ! NewMonkey

  override def receive: Receive = {
    case _ =>
  }
}

object MonkeyMock {
  val props = Reader {
    (testerRef: ActorRef) => Props(classOf[MonkeyMock], testerRef)
  }
}

class CanyonMock(testerActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case message => testerActor ! message
  }
}

object CanyonMock {
  val props = Reader {
    (testerRef: ActorRef) => Props(classOf[CanyonMock], testerRef)
  }
}


class LeadingTest extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers {

  val wait_time = 50 millis
  val testerActor = TestProbe()
  val monkeys = 5
  val minTime = 10
  val maxTime = 80

  class LeadingMock extends Leading(CanyonMock.props(testerActor.ref), classOf[MonkeyMock]) {
    override def getMaxTime: Int = maxTime

    override def getMinTime: Int = minTime

    override def getNumMonkeys: Int = monkeys
  }


  "MonkeyLeading" should {
    "Received messages" in {
      val leading = system.actorOf(Leading.props(CanyonMock.props(testerActor.ref), classOf[MonkeyMock]))
      leading ! "message"

    }

    "Generate a canyon" in {
      val leading = TestActorRef[Leading](Leading.props(CanyonMock.props(testerActor.ref), classOf[MonkeyMock]))
      leading.underlyingActor.canyon ! "Ping"
      testerActor expectMsg "Ping"
      testerActor expectNoMsg wait_time

    }

    "The second monkey should have a delay" in {
      val leaderActor = TestActorRef[Leading](new LeadingMock {
        override def getNumMonkeys: Int = 2
      })
      leaderActor ! StartSimulation
      testerActor expectMsg NewMonkey
      val before = System.currentTimeMillis()
      testerActor expectMsg NewMonkey
      val after = System.currentTimeMillis()
      testerActor expectNoMsg wait_time
      after - before should be <= maxTime.toLong * 3 // For the latency
      after - before should be >= minTime.toLong
    }

    "not fail when a monkey end and it is not the last monkey" in {
      val leading = TestActorRef[Leading](Leading.props(CanyonMock.props(testerActor.ref), classOf[MonkeyMock]))
      leading.underlyingActor.context.become(leading.underlyingActor.waitingTheEnd)
      val monkey1 = leading.underlyingActor.context.actorOf(MonkeyMock.props(leading.underlyingActor.canyon))
      val monkey2 = leading.underlyingActor.context.actorOf(MonkeyMock.props(leading.underlyingActor.canyon))
      val testProbe = TestProbe()
      testProbe.watch(leading)
      leading.underlyingActor.context.watch(monkey1)
      leading.underlyingActor.context.watch(monkey2)
      leading.underlyingActor.context.stop(monkey1)
      testProbe expectNoMsg wait_time // No message of terminated
    }
  }
}
