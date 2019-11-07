package main.app

import akka.actor.ActorSystem
import akka.event.{LogSource, Logging}
import akka.stream.ActorMaterializer

trait AppSystem {
  implicit val system: ActorSystem = ActorSystem("actor-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val logSource: LogSource[AnyRef] = new LogSource[AnyRef] {
    override def genString(t: AnyRef): String = t.getClass.getName
    override def getClazz(t: AnyRef): Class[_] = t.getClass
  }
  val logger = Logging(system, this)
}
