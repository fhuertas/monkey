package com.fhuertas.monkey

package object messages {

  case object YouAreInTheValley

  case class NewMonkeyInTheValley(state: Option[Int] = None)

}
