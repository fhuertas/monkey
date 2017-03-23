package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import akka.util.Timeout
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Directions._
import org.scalatest.{Matchers, WordSpecLike}

import scala.concurrent.duration._

class CanyonIT extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers {
  "Canyon" should {
    "The monkeys in the same direction should can cross" in {
      val wait_time = 100 millis

      implicit val timeout = Timeout(5 seconds)

      val monkey1, monkey2, monkey3 = TestProbe()

      val canyon = TestActorRef[Canyon](new Canyon)


      monkey1.send(canyon, CanICross(East)) // _/1/-----/_/_ Monkey 1
      monkey1 expectMsg CanCross

      monkey1.send(canyon, CrossingCanyon) // /_/1----/_/_ Monkey 1
      monkey1 expectNoMsg wait_time

      monkey2.send(canyon, CanICross(East)) // /2/1----/_/_ Monkey 2
      monkey2 expectMsg CanCross

      monkey1.send(canyon, CrossedCanyon) // /2/-----/_/1 Monkey 1
      monkey1 expectMsg YouAreInTheOtherSide
      monkey2.send(canyon, CrossingCanyon) // /_/2----/_/1 Monkey 2
      monkey2 expectNoMsg wait_time

      monkey3.send(canyon, CanICross(East)) // /3/2----/_/1 Monkey 3
      monkey3 expectMsg CanCross

      monkey3.send(canyon, CrossingCanyon) // /_/32---/_/1 Monkey 3


      monkey2.send(canyon, CrossedCanyon) // /_/3----/_/21 Monkey 2
      monkey2 expectMsg YouAreInTheOtherSide

      monkey3.send(canyon, CrossedCanyon) // /_/-----/_/321 Monkey 3
      monkey3 expectMsg YouAreInTheOtherSide

    }

    "allow to cross the canyon with the robe is free" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      val wait_time = 100 millis
      val monkey1, monkey2, monkey3, monkey4 = TestProbe()
      monkey1.send(canyon, CanICross(East)) // _/1/-----/_/_ Monkey 1
      monkey1 expectMsg CanCross
      monkey1.send(canyon, CrossingCanyon) // /_/1----/_/_ Monkey 1
      monkey1 expectNoMsg wait_time


      monkey2.send(canyon, CanICross(East)) // /2/1----/_/_ Monkey 2
      monkey2 expectMsg CanCross

      monkey1.send(canyon, CrossedCanyon) // /2/-----/_/ Monkey 1
      monkey1 expectMsg YouAreInTheOtherSide

      monkey2.send(canyon, CrossingCanyon) // /_/2----/_/ Monkey 2
      monkey2 expectNoMsg wait_time


      monkey3.send(canyon, CanICross(East)) // /3/2----/_/ Monkey 3
      monkey3 expectMsg CanCross

      monkey4.send(canyon, CanICross(West)) // /3/2----/_/4 Monkey 4
      monkey4 expectMsg CannotCross

      monkey3.send(canyon, CrossingCanyon) // /_/32---/_/4 Monkey 3
      monkey3 expectNoMsg wait_time


      monkey2.send(canyon, CrossedCanyon) // /_/3----/_/4 Monkey 2
      monkey2 expectMsg YouAreInTheOtherSide

      monkey3.send(canyon, CrossedCanyon) // /_/-----/_/4 Monkey 3
      monkey3 expectMsg YouAreInTheOtherSide
      monkey4 expectMsg AreYouReady

      monkey4.send(canyon, CanICross(West)) // /_/-----/4/_ Monkey 4
      monkey4 expectMsg CanCross


    }

    "prevent the starvation" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      val wait_time = 100 millis

      val monkey1, monkey2, monkey3, monkey4 = TestProbe()

      monkey1.send(canyon , CanICross(West)) // _/1/-----/_/_ Monkey 1
      monkey1 expectMsg CanCross

      monkey2.send(canyon , CanICross(West)) // 2/1/-----/_/_ Monkey 2
      monkey2 expectMsg CannotCross

      monkey1.send(canyon , CrossingCanyon) // 2/_/1----/_/_ Monkey 1
      monkey1 expectNoMsg wait_time


      monkey2.send(canyon , CanICross(West)) // /2/1----/_/_ Monkey 2
      monkey2 expectMsg CanCross

      monkey2.send(canyon , CrossingCanyon) // /_/21---/_/_ Monkey 2
      monkey2 expectNoMsg wait_time

      // Starvation!!
      monkey4.send(canyon , CanICross(East)) // /_/21----/_/4 Monkey 4
      monkey4 expectMsg CannotCross


      // Starvation!!
      monkey3.send(canyon , CanICross(West)) // 3/_/21----/_/4 Monkey 3
      monkey3 expectMsg CannotCross

      monkey1.send(canyon , CrossedCanyon) // 3/_/------/_/4 Monkey 1
      monkey1 expectMsg YouAreInTheOtherSide
      monkey2.send(canyon , CrossedCanyon) // 3/_/------/_/4 Monkey 2
      monkey2 expectMsg YouAreInTheOtherSide
      monkey4 expectMsg AreYouReady
    }
  }
}
