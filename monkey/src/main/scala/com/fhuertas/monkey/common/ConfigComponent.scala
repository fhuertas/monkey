package com.fhuertas.monkey.common

import scala.util.Try
import com.typesafe.config.{Config, ConfigFactory}

object ConfigComponent {

  val config: Config = ConfigFactory.load()

  def getString(key: String): Option[String] = Try(config.getString(key)).toOption

  def getString(key: String, default: String): String = getString(key) getOrElse default

  def getInt(key: String): Option[Int] = Try(config.getInt(key)).toOption

  def getInt(key: String, default: Int): Int = getInt(key) getOrElse default

  def getBoolean(key: String): Option[Boolean] = Try(config.getBoolean(key)).toOption

  def getBoolean(key: String, default: Boolean): Boolean = getBoolean(key) getOrElse default

}