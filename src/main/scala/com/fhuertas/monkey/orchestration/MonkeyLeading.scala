package com.fhuertas.monkey.orchestration

import com.typesafe.scalalogging.LazyLogging
import me.atrox.haikunator.HaikunatorBuilder

class MonkeyLeading extends OrchestrationConfig with LazyLogging {
  val haikunator = new HaikunatorBuilder().setTokenLength(0).setDelimiter("").build()

  private def generateName = haikunator.haikunate()

  def generateMonkey: String = generateName
}
