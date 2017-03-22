package com.fhuertas.monkey.utils

import akka.actor.Props
import com.fhuertas.monkey.orchestration.Leading

import scalaz.Reader

/**
  * Created by fhuertas on 22/03/17.
  */
class FastLeading(canyonProps: Props, monkeyClass: Class[_])
  extends Leading(canyonProps, monkeyClass) with FastConfiguration


object FastLeading {
  val props = Reader {
    (canyonAndMonkey: (Props, Class[_])) => Props(classOf[Leading], canyonAndMonkey._1, canyonAndMonkey._2)

  }
}