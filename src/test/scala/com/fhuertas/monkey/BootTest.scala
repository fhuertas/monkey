package com.fhuertas.monkey

import org.scalatest.{Matchers, WordSpec}

class BootTest extends WordSpec with Matchers {
  "Boot" should {
    "Start the application and wait to end" in {
      Boot.main(Array.empty[String])
    }
  }

}
