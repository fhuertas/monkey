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

class MonkeyLeadingTest extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers {
  val testerActor = TestProbe()
  val monkeys = 13
  class MonkeyLeadingMock extends MonkeyLeading(MonkeyMock.props(testerActor.ref)) {
    override def getNumberOfMonkeys: Int = monkeys

    override def getTimeFactor: Int = 1

    override def getMaxTime: Int = 1

    override def getMinTime: Int = 2
  }


  "MonkeyLeading" should {
    "Generate at least a monkey" in {
      val monkeyLeaderActor = TestActorRef[MonkeyLeading](new MonkeyLeadingMock)
      monkeyLeaderActor ! NewMonkeyInTheValley
      testerActor expectMsg YouAreInTheValley
    }

    "Generate the correct number of monkeys" in {
      val monkeyLeaderActor = TestActorRef[MonkeyLeading](new MonkeyLeadingMock)
      monkeyLeaderActor ! NewMonkeyInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectMsg YouAreInTheValley
      testerActor expectNoMsg()
    }
  }

  //  "MonkeyLeading" should "Generate a Monkey" in new MonkeyLeading {
  //    val monkey = generateMonkey
  //
  //    monkey shouldBe an[Monkey]
  //  }
}
