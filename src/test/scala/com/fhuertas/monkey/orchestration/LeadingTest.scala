package com.fhuertas.monkey.orchestration

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Monkey
import org.scalatest.{Matchers, WordSpecLike}

import scalaz.Reader

/**
  * Created by fhuertas on 16/03/17.
  */


class MonkeyMock(testerActor: ActorRef) extends Actor {
  override def receive: Receive = {
    case message => testerActor ! message
  }
}

object MonkeyMock {
  val props = Reader {
    (testerRef: ActorRef) => Props(classOf[MonkeyMock], testerRef)
  }

}

class LeadingTest extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers {
  val testerActor = TestProbe()
  val monkeys = 5
  val minTime = 10
  val maxTime = 80

  class LeadingMock extends Leading(MonkeyMock.props(testerActor.ref)) {
    override def getMaxTime: Int = maxTime

    override def getMinTime: Int = minTime
  }


  "MonkeyLeading" should {
    "Received messages" in {
      val leading = system.actorOf(Leading.props(MonkeyMock.props(testerActor.ref)))
      leading ! "message"

    }

    "Generate at least a monkey" in {
      val monkeyLeaderActor = TestActorRef[Leading](new LeadingMock)
      monkeyLeaderActor ! NewMonkeyInTheValley(Option(1))
      testerActor expectMsg YouAreInTheValley
      testerActor expectNoMsg()
    }

    "generate a indeterminate number of monkeys are not supported, not message are sent" in {
      val monkeyLeaderActor = TestActorRef[Leading](new LeadingMock)
      monkeyLeaderActor ! NewMonkeyInTheValley(None)
      testerActor expectNoMsg()
    }

    "Generate more than one monkeys and the times are corrects" in {
      val monkeyLeaderActor = TestActorRef[Leading](new LeadingMock)
      val minimumTime = minTime * monkeys
      val maximumTime = maxTime * (monkeys + 1)

      monkeyLeaderActor ! NewMonkeyInTheValley(Option(monkeys))

      val before = System.currentTimeMillis()
      1 to monkeys foreach { _ => testerActor expectMsg YouAreInTheValley }
      val after = System.currentTimeMillis()

      after - before should be <= maximumTime.toLong
      after - before should be >= minimumTime.toLong
    }
  }
}
