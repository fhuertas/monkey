package com.fhuertas.monkey.common

import org.scalatest.{Matchers, WordSpec}

class ConfigComponentTest extends WordSpec with Matchers {
  "ConfigComponentTest" should {
    "Return values if there are value as default" in new ConfigComponent {
      getBoolean("foo.bar.boolean", default = false) shouldBe true
      getString("foo.bar.str", default = "cad") shouldBe "foobar"
      getInt("foo.bar.int", default = 32) shouldBe 1
    }

    "Return default it the values cannot be parsed" in new ConfigComponent {
      getBoolean("foo.bar.str", default = false) shouldBe false
      getInt("foo.bar.str", default = 32) shouldBe 32
    }
    "Return default it the values don't exist" in new ConfigComponent {
      getBoolean("fake.foo.bar.boolean", default = false) shouldBe false
      getString("fake.foo.bar.str", default = "cad") shouldBe "cad"
      getInt("fake.foo.bar.int", default = 32) shouldBe 32
    }

  }

}
