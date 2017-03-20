package com.fhuertas.monkey

import scala.util.Random

/**
  * Created by fhuertas on 20/03/17.
  */
package object models {

  object Directions extends Enumeration {
    type Direction = Value
    val West, East = Value

    def randomDirection: Direction = {
      Directions.apply(Random.nextInt(Directions.maxId))
    }
  }
}
