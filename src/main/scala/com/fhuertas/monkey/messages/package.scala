package com.fhuertas.monkey

import com.fhuertas.monkey.models.Directions.Direction

package object messages {

  case class NewMonkeyInTheValley(state: Option[Int] = None)

  case class CanICross(direction: Direction)

  case object CanCross

  case object CannotCross

  case object AreYouReady

  case object ClimbingRobe

  case object CrossingCanyon

  case object CrossedCanyon

  case object YouAreInTheOtherSide

  case object WhereAreYou

  case object IHaveCrossed

}
