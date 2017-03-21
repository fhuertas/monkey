package com.fhuertas.monkey.models

import akka.actor.{Actor, ActorLogging, Props}
import com.fhuertas.monkey.messages._

class Canyon extends Actor with ActorLogging{
  override def receive: Receive = {
    case CanICross =>
      sender ! CanCross
    case message => log.debug(logMsg(s"I don't understand you: $message"))
  }

  private def logMsg(msg:String) = s"[canyon]: $msg"
}

object Canyon {
  val props = Props(classOf[Canyon])
}
