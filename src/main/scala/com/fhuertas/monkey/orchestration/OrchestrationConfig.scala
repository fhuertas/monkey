package com.fhuertas.monkey.orchestration

import com.fhuertas.monkey.common.ConfigComponent

trait OrchestrationConfig {

  import OrchestrationConfig._

  def getNumberOfMonkeys: Int = ConfigComponent.getInt(KEY_NUMBER_MONKEYS, DEFAULT_NUMBER_MONKEYS)
}

object OrchestrationConfig {
  val KEY_NUMBER_MONKEYS = "monkey.number"
  val DEFAULT_NUMBER_MONKEYS = 8
}
