package com.fhuertas.monkey.orchestration

import org.scalatest.{FlatSpec, Matchers, WordSpec}

/**
  * Created by fhuertas on 16/03/17.
  */
class OrchestrationConfigTest extends WordSpec with Matchers with OrchestrationConfig {

  "Configuration" should {
    "read the configuration correctly" in {
      getNumberOfMonkeys should be(Some(10))
      getMaxTime should be (10000)
      getMinTime should be (1250)
      getTimeFactor should be (500)
    }
  }
}
