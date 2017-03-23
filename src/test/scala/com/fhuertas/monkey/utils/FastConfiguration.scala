package com.fhuertas.monkey.utils

import com.fhuertas.monkey.common.ConfigComponent
import com.typesafe.config.{Config, ConfigFactory}

trait FastConfiguration extends ConfigComponent {
  override val config: Config = ConfigFactory.load("fast_conf.conf")
}
