package com.fhuertas.monkey

import org.scalatest.{Matchers, WordSpec}

class BootTest extends WordSpec with Matchers {
  "Boot" should {
    "should end" in {
      Boot.main(Array.empty[String])
    }
  }

}
