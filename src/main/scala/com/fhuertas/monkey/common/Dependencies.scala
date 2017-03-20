package com.fhuertas.monkey.common

import akka.actor.ActorSystem

trait Dependencies {
  implicit val actorSystem: ActorSystem = ActorSystem("Scheduler-System")
}
