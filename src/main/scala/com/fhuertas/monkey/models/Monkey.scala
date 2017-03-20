package com.fhuertas.monkey.models

import akka.actor.Actor
import akka.actor.Actor.Receive
import akka.actor.ActorLogging
import akka.actor.ActorRef
import me.atrox.haikunator.HaikunatorBuilder


/**
  * Created by fhuertas on 12/03/17.
  */
class Monkey extends Actor with ActorLogging {
  val name = Monkey.haikunator.haikunate()
  override def receive: Receive = {
    case message => log.info(s"[$name] I don't understand your message: $message")
  }
}


object Monkey {
  lazy val haikunator = new HaikunatorBuilder().setTokenLength(0).setDelimiter("").build()

}
