package com.fhuertas.monkey.orchestration

import com.fhuertas.monkey.common.ConfigComponent

trait OrchestrationConfig extends ConfigComponent {

  import OrchestrationConfig._

  def getMaxTime: Int = getInt(KEY_MAX_TIME, DEFAULT_MAX_TIME)

  def getMinTime: Int = getInt(KEY_MIN_TIME, DEFAULT_MIN_TIME)

  def getNumMonkeys: Int = getInt(KEY_NUM_MONKEYS, DEFAULT_NUM_MONKEYS)
}

object OrchestrationConfig {

  val KEY_MAX_TIME = s"${ConfigComponent.KEY_MONKEY_ROOT}.max_time"
  val KEY_MIN_TIME = s"${ConfigComponent.KEY_MONKEY_ROOT}.min_time"
  val KEY_NUM_MONKEYS = s"${ConfigComponent.KEY_MONKEY_ROOT}.number"
  val DEFAULT_MAX_TIME = 8000
  val DEFAULT_MIN_TIME = 1000
  val DEFAULT_NUM_MONKEYS = 8
}
