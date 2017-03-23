package com.fhuertas.monkey.models

import com.fhuertas.monkey.common.ConfigComponent

trait MonkeyConfig extends ConfigComponent {

  import MonkeyConfig._

  def getCrossTime: Int = getInt(KEY_CROSS_TIME, DEFAULT_CROSS_TIME)

  def getClimbingRobeTime: Int = getInt(KEY_ROPE_TIME, DEFAULT_ROPE_TIME)

  def getWaitingTimeMin: Int = getInt(KEY_MIN_WAITING, DEFAULT_MIN_WAITING)

  def getWaitingTimeMax: Int = getInt(KEY_MAX_WAITING, DEFAULT_MAX_WAITING)

  def getTotalTime = getCrossTime + getClimbingRobeTime
}

object MonkeyConfig {
  val KEY_CROSS_TIME = s"${ConfigComponent.KEY_MONKEY_ROOT}.cross_time"
  val KEY_ROPE_TIME = s"${ConfigComponent.KEY_MONKEY_ROOT}.rope_time"
  val DEFAULT_CROSS_TIME = 4000
  val DEFAULT_ROPE_TIME = 1000
  val KEY_MIN_WAITING = s"${ConfigComponent.KEY_MONKEY_ROOT}.waiting_min"
  val KEY_MAX_WAITING = s"${ConfigComponent.KEY_MONKEY_ROOT}.waiting_max"
  val DEFAULT_MIN_WAITING = 100
  val DEFAULT_MAX_WAITING = 200
}
