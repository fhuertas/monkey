package com.fhuertas.monkey.utils

import com.fhuertas.monkey.common.ConfigComponent
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by fhuertas on 22/03/17.
  */
trait FastConfiguration extends ConfigComponent {
  override val config: Config = ConfigFactory.load("fast_conf.conf")
}
