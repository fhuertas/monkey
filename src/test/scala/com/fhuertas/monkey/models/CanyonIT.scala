package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
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

      val canyon = TestActorRef[Canyon](new Canyon)


      canyon ! CanICross(East) // _/1/-----/_/_ Monkey 1
      expectMsg(CanCross)
      canyon ! CrossingCanyon // /_/1----/_/_ Monkey 1
      expectNoMsg(wait_time)

      canyon ! CanICross(East) // /2/1----/_/_ Monkey 2
      expectMsg(CanCross)

      canyon ! CrossedCanyon // /2/-----/_/1 Monkey 1
      expectNoMsg(wait_time)
      canyon ! CrossingCanyon // /_/2----/_/1 Monkey 2
      expectNoMsg(wait_time)

      canyon ! CanICross(East) // /3/2----/_/1 Monkey 3
      expectMsg(CanCross)
      canyon ! CrossingCanyon // /_/32---/_/1 Monkey 3


      canyon ! CrossedCanyon // /_/3----/_/21 Monkey 2

      canyon ! CrossedCanyon // /_/-----/_/321 Monkey 3
      expectNoMsg(wait_time)

    }

    "allow to cross the canyon with the robe is free" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      val wait_time = 100 millis

      canyon ! CanICross(East) // _/1/-----/_/_ Monkey 1
      expectMsg(CanCross)
      canyon ! CrossingCanyon // /_/1----/_/_ Monkey 1
      expectNoMsg(wait_time)


      canyon ! CanICross(East) // /2/1----/_/_ Monkey 2
      expectMsg(CanCross)

      canyon ! CrossedCanyon // /2/-----/_/ Monkey 1
      expectNoMsg(wait_time)

      canyon ! CrossingCanyon // /_/2----/_/ Monkey 2
      expectNoMsg(wait_time)


      canyon ! CanICross(East) // /3/2----/_/ Monkey 3
      expectMsg(CanCross)

      canyon ! CanICross(West) // /3/2----/_/4 Monkey 3
      expectMsg(CannotCross)

      canyon ! CrossingCanyon // /_/32---/_/4 Monkey 3
      expectNoMsg(wait_time)


      canyon ! CrossedCanyon // /_/3----/_/4 Monkey 2
      expectNoMsg(wait_time)

      canyon ! CrossedCanyon // /_/-----/_/4 Monkey 3
      expectMsg(AreYouReady)

      canyon ! CanICross(West) // /_/-----/4/_ Monkey 4
      expectMsg(CanCross)


    }

    "prevent the starvation" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      val wait_time = 100 millis

      canyon ! CanICross(West) // _/1/-----/_/_ Monkey 1
      expectMsg(CanCross)
      canyon ! CanICross(West) // 2/1/-----/_/_ Monkey 2
      expectMsg(CannotCross)
      canyon ! CrossingCanyon // 2/_/1----/_/_ Monkey 1
      expectNoMsg(wait_time)


      canyon ! CanICross(West) // /2/1----/_/_ Monkey 2
      expectMsg(CanCross)

      // Starvation!!
      canyon ! CanICross(East) // /2/1----/_/4 Monkey 4
      expectMsg(CannotCross)

      canyon ! CrossingCanyon // /_/21---/_/4 Monkey 2
      expectNoMsg(wait_time)

      // Starvation!!
      canyon ! CanICross(West) // 3/_/21----/_/4 Monkey 3
      expectMsg(CannotCross)

      canyon ! CrossedCanyon // 3/_/------/_/4 Monkey 1
      canyon ! CrossedCanyon // 3/_/------/_/4 Monkey 2
      expectMsg(AreYouReady)
    }
  }
}
