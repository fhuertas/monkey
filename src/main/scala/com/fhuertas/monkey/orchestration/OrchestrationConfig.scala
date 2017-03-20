package com.fhuertas.monkey.orchestration

import com.fhuertas.monkey.common.ConfigComponent

trait OrchestrationConfig {

  import OrchestrationConfig._

  def getNumberOfMonkeys: Option[Int] = ConfigComponent.getInt(KEY_NUMBER_MONKEYS)
  def getTimeFactor: Int = ConfigComponent.getInt(KEY_TIME_FACTOR, DEFAULT_TIME_FACTOR)
  def getMaxTime: Int = ConfigComponent.getInt(KEY_MAX_TIME, DEFAULT_MAX_TIME)
  def getMinTime: Int = ConfigComponent.getInt(KEY_MIN_TIME, DEFAULT_MIN_TIME)
}

object OrchestrationConfig {
  val KEY_MONKEY_ROOT = "monkey"
  val KEY_NUMBER_MONKEYS = s"$KEY_MONKEY_ROOT.number"
  val KEY_TIME_FACTOR = s"$KEY_MONKEY_ROOT.factor"
  val KEY_MAX_TIME = s"$KEY_MONKEY_ROOT.max_time"
  val KEY_MIN_TIME = s"$KEY_MONKEY_ROOT.min_time"
  val DEFAULT_NUMBER_MONKEYS = 8
  val DEFAULT_TIME_FACTOR = 1
  val DEFAULT_MAX_TIME = 1000
  val DEFAULT_MIN_TIME = 8000
}
