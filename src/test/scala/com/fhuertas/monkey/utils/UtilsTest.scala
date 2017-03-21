package com.fhuertas.monkey.utils

import akka.testkit.TestActorRef
import com.fhuertas.monkey.orchestration.Leading$
import org.scalatest.{Matchers, WordSpec}

class UtilsTest extends WordSpec with Matchers {
  "Utils functions" should {
    "generate times correctly, between minimum and maximum" in {
      val min = 10
      val max = 20
      val random = Utils.generateTime(min,max)

      random should be <= max
      random should be > min
    }
  }
}
