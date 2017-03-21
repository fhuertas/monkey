package com.fhuertas.monkey

import com.fhuertas.monkey.models.Directions.Direction

package object messages {

  case object YouAreInTheValley

  case class NewMonkeyInTheValley(state: Option[Int] = None)

  case class CanICross(direction: Direction)

  case object CanCross

  case object CannotCross

  case object IJustCrossed

  case object ClimbingRobe

  case object CrossingCanyon

  case object CrossedCanyon

}
