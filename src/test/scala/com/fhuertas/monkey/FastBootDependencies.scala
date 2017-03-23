package com.fhuertas.monkey

import akka.actor.Props
import com.fhuertas.monkey.models.Monkey
import com.fhuertas.monkey.utils.FastLeading

class FastBootDependencies extends BootDependencies {

  override val monkeyActor: Class[Monkey] = classOf[Monkey]

  override val leadingProps: Props = FastLeading.props(canyonProps, monkeyActor)

}
