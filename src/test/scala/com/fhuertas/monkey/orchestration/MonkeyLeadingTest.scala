package com.fhuertas.monkey.orchestration

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by fhuertas on 16/03/17.
  */
class MonkeyLeadingTest extends FlatSpec with Matchers {

  "MonkeyLeading" should "Generate a Monkey" in new MonkeyLeading {
    val monkey = generateMonkey

    monkey shouldBe an[String]
  }
}
