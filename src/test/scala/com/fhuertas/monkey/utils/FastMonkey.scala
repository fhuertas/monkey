package com.fhuertas.monkey.utils

import akka.actor.{ActorRef, Props}
import com.fhuertas.monkey.models.{CanyonMonkeyIT, Monkey}
import com.typesafe.config.{Config, ConfigFactory}

import scalaz.Reader


class FastMonkey(canyon: ActorRef) extends Monkey(canyon) with FastConfiguration

object FastMonkey {
  val props = Reader {
    (canyon: ActorRef) => Props(classOf[FastMonkey], canyon)
  }
}

