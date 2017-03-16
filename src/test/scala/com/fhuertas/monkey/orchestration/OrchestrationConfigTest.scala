package com.fhuertas.monkey.orchestration

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by fhuertas on 16/03/17.
  */
class OrchestrationConfigTest extends FlatSpec with Matchers with OrchestrationConfig {

  "Configuration" should "be read correctly" in {
    getNumberOfMonkeys should be(10)
  }
}
