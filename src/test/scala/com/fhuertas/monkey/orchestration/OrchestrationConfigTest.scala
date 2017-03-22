package com.fhuertas.monkey.orchestration

import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by fhuertas on 16/03/17.
  */
class OrchestrationConfigTest extends WordSpec with Matchers {

  "Configuration" should {
    "read the configuration from the file when exists" in new OrchestrationConfig {
      getMaxTime should be(10000)
      getMinTime should be(1250)
      getNumMonkeys should be (10)
    }

    "read default parameters where they are not defined" in new OrchestrationConfig {
      override val config = ConfigFactory.empty()
      getMaxTime should be(OrchestrationConfig.DEFAULT_MAX_TIME)
      getMinTime should be(OrchestrationConfig.DEFAULT_MIN_TIME)
      getNumMonkeys should be(OrchestrationConfig.DEFAULT_NUM_MONKEYS)
    }

  }
}
