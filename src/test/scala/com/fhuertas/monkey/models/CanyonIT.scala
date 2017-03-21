package com.fhuertas.monkey.models

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import akka.util.Timeout
import com.fhuertas.monkey.messages._
import com.fhuertas.monkey.models.Directions._
import org.scalatest.{Matchers, WordSpecLike}

class CanyonIT extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers {
  "Canyon" should {
    "The monkeys in the same direction should can cross" in {
      import akka.pattern.ask

      import scala.concurrent.duration._

      implicit val timeout = Timeout(5 seconds)

      val canyon = TestActorRef[Canyon](new Canyon)


      canyon ! CanICross(East) // _/1/-----/_/_ Monkey 1
      expectMsg(CanCross)
      canyon ? CrossingCanyon // /_/1----/_/_ Monkey 1


      canyon ! CanICross(East) // /2/1----/_/_ Monkey 2
      expectMsg(CanCross)

      canyon ? CrossedCanyon // /2/-----/_/1 Monkey 1

      canyon ? CrossingCanyon // /_/2----/_/1 Monkey 2


      canyon ! CanICross(East) // /3/2----/_/1 Monkey 3
      expectMsg(CanCross)
      canyon ? CrossingCanyon // /_/32---/_/1 Monkey 3


      canyon ? CrossedCanyon // /_/3----/_/21 Monkey 2

      canyon ? CrossedCanyon // /_/-----/_/321 Monkey 3
      expectNoMsg()

    }

    "allow to cross the canyon with the robe is free" in {
      val canyon = TestActorRef[Canyon](new Canyon)
      canyon ! CanICross(East) // _/1/-----/_/_ Monkey 1
      expectMsg(CanCross)
      canyon ! CrossingCanyon // /_/1----/_/_ Monkey 1


      canyon ! CanICross(East) // /2/1----/_/_ Monkey 2

      canyon ! CrossedCanyon // /2/-----/_/ Monkey 1

      canyon ! CrossingCanyon // /_/2----/_/ Monkey 2


      canyon ! CanICross(East) // /3/2----/_/ Monkey 3
      expectMsg(CanCross)
      canyon ! CanICross(West) // /3/2----/_/4 Monkey 3
      expectMsg(CannotCross)
      canyon ! CrossingCanyon // /_/32---/_/4 Monkey 3


      canyon ! CrossedCanyon // /_/3----/_/4 Monkey 2

      canyon ! CrossedCanyon // /_/-----/_/4 Monkey 3

      canyon ! CanICross(West) // /_/-----/4/_ Monkey 4
      expectMsg(CanCross)

    }
  }
}
