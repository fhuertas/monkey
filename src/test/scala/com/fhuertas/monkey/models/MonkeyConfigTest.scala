package com.fhuertas.monkey.models

import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpec}

class MonkeyConfigTest extends WordSpec with Matchers {

  "MonkeyConfig" should {
    "read the file when the values exists" in new MonkeyConfig {
      getCrossTime should be(2000)
      getClimbingRobeTime should be(500)
      getWaitingTimeMax should be(250)
      getWaitingTimeMin should be(125)

    }

    "read the default parameters when they are not defined" in new MonkeyConfig {
      override val config = ConfigFactory.empty()
      getCrossTime should be(MonkeyConfig.DEFAULT_CROSS_TIME)
      getClimbingRobeTime should be(MonkeyConfig.DEFAULT_ROPE_TIME)
      getWaitingTimeMax should be(MonkeyConfig.DEFAULT_MAX_WAITING)
      getWaitingTimeMin should be(MonkeyConfig.DEFAULT_MIN_WAITING)
    }

    "return the correct total time" in new MonkeyConfig {
      override def getClimbingRobeTime: Int = 10

      override def getCrossTime: Int = 20

      getTotalTime should be(30)
    }
  }

}
