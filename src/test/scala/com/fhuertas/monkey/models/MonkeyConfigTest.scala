package com.fhuertas.monkey.models

import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpec}

/**
  * Created by fhuertas on 20/03/17.
  */
class MonkeyConfigTest extends WordSpec with Matchers {

  "MonkeyConfig" should {
    "read the file when the values exists" in new MonkeyConfig {
      getCrossTime should be(4250)
      getClimbingRobeTime should be(950)
    }

    "read the default parameters when they are not defined" in new MonkeyConfig {
      override val config = ConfigFactory.empty()
      getCrossTime should be(MonkeyConfig.DEFAULT_CROSS_TIME)
      getClimbingRobeTime should be(MonkeyConfig.DEFAULT_ROPE_TIME)
    }

    "return the correct total time" in new MonkeyConfig {
      override def getClimbingRobeTime: Int = 10

      override def getCrossTime: Int = 20

      getTotalTime should be(30)
    }
  }

}
