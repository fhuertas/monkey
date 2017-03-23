package com.fhuertas.monkey


import com.fhuertas.monkey.messages.StartSimulation
import com.fhuertas.monkey.orchestration.Leading

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Boot extends App with BootDependencies {

  override def main(args: Array[String]): Unit = {
    val leading = actorSystem.actorOf(Leading.props(canyonProps, monkeyActor))
    leading ! StartSimulation
    Await.result(actorSystem.whenTerminated, Duration.Inf)
  }
}
