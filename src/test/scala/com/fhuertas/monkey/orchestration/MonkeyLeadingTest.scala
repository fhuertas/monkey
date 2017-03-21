package com.fhuertas.monkey.orchestration

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.fhuertas.monkey.messages._
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

class MonkeyLeadingTest extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers {
  val testerActor = TestProbe()
  val monkeys = 5
  val minTime = 10
  val maxTime = 80

  class MonkeyLeadingMock extends MonkeyLeading(MonkeyMock.props(testerActor.ref)) {
    override def getMaxTime: Int = maxTime

    override def getMinTime: Int = minTime
  }


  "MonkeyLeading" should {
    "Generate at least a monkey" in {
      val monkeyLeaderActor = TestActorRef[MonkeyLeading](new MonkeyLeadingMock)
      monkeyLeaderActor ! NewMonkeyInTheValley(Option(1))
      testerActor expectMsg YouAreInTheValley
      testerActor expectNoMsg()
    }

    "generate a indeterminate number of monkeys are not supported, not message are sent" in {
      val monkeyLeaderActor = TestActorRef[MonkeyLeading](new MonkeyLeadingMock)
      monkeyLeaderActor ! NewMonkeyInTheValley(None)
      testerActor expectNoMsg()
    }

    "Generate more than one monkeys and the times are corrects" in {
      val monkeyLeaderActor = TestActorRef[MonkeyLeading](new MonkeyLeadingMock)
      val minimumTime = minTime * monkeys
      val maximumTime = maxTime * (monkeys + 1)

      monkeyLeaderActor ! NewMonkeyInTheValley(Option(monkeys))

      val before = System.currentTimeMillis()
      1 to monkeys foreach { _ => testerActor expectMsg YouAreInTheValley }
      val after = System.currentTimeMillis()

      after - before should be < maximumTime.toLong
      after - before should be > minimumTime.toLong
    }

    "generate times correctly, between minimum and maximum" in {
      val monkeyLeaderActor = TestActorRef[MonkeyLeading](new MonkeyLeadingMock)
      val random = monkeyLeaderActor.underlyingActor.generateTime

      random should be < maxTime
      random should be >= minTime
    }
  }
}
