package com.fhuertas.monkey.orchestration

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.fhuertas.monkey.messages._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._
import scalaz.Reader

/**
  * Created by fhuertas on 16/03/17.
  */


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

  class LeadingMock extends Leading(CanyonMock.props(testerActor.ref),classOf[MonkeyMock]) {
    override def getMaxTime: Int = maxTime

    override def getMinTime: Int = minTime
  }


  "MonkeyLeading" should {
    "Received messages" in {
      val leading = system.actorOf(Leading.props(CanyonMock.props(testerActor.ref),classOf[MonkeyMock]))
      leading ! "message"

    }

    "Generate a canyon" in {
      val leading = TestActorRef[Leading](Leading.props(CanyonMock.props(testerActor.ref),classOf[MonkeyMock]))
      leading.underlyingActor.canyon ! "Ping"
      testerActor expectMsg "Ping"
      testerActor expectNoMsg wait_time

    }

    "Generate at least a monkey" in {
      val leaderActor = TestActorRef[Leading](new LeadingMock)
      leaderActor ! NewMonkeyInTheValley(Option(1))
      testerActor expectMsg NewMonkey
      testerActor expectNoMsg wait_time
      leaderActor.underlyingActor.monkeysInTheCanyon.size shouldBe 1
    }


    "The second monkey should have a delay" in {
      val leaderActor = TestActorRef[Leading](new LeadingMock)
      leaderActor ! NewMonkeyInTheValley(Option(2))
      testerActor expectMsg NewMonkey
      val before = System.currentTimeMillis()
      testerActor expectMsg NewMonkey
      val after = System.currentTimeMillis()
      testerActor expectNoMsg wait_time
      leaderActor.underlyingActor.monkeysInTheCanyon.size shouldBe 2
      after - before should be <= maxTime.toLong * 2
      after - before should be >= minTime.toLong

    }

    "generate a indeterminate number of monkeys are not supported, not message are sent" in {
      val leaderActor = TestActorRef[Leading](new LeadingMock)
      leaderActor ! NewMonkeyInTheValley(None)
      testerActor expectNoMsg wait_time
      leaderActor.underlyingActor.monkeysInTheCanyon.size shouldBe 0
    }

    "Generate more than one monkeys and the times are corrects" in {
      val leaderActor = TestActorRef[Leading](new LeadingMock)
      val minimumTime = minTime * monkeys
      val maximumTime = maxTime * monkeys

      leaderActor ! NewMonkeyInTheValley(Option(monkeys))

      val before = System.currentTimeMillis()
      1 to monkeys foreach { _ => testerActor expectMsg NewMonkey }
      val after = System.currentTimeMillis()

      after - before should be <= maximumTime.toLong
      after - before should be >= minimumTime.toLong
    }
  }
}
