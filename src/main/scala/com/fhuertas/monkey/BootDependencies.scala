package com.fhuertas.monkey

import akka.actor.{ActorSystem, Props}
import com.fhuertas.monkey.models.{Canyon, Monkey}
import com.fhuertas.monkey.orchestration.Leading

trait BootDependencies {
  val actorSystem: ActorSystem = ActorSystem("Scheduler-System")

  val monkeyActor: Class[Monkey] = classOf[Monkey]

  val canyonProps: Props = Canyon.props

  val leadingProps: Props = Leading.props(canyonProps, monkeyActor)

}
