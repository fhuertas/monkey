package com.fhuertas.monkey.utils

object Utils {
  def generateTime(min: Int, max: Int): Int = scala.util.Random.nextInt(max - min) + min + 1

}
