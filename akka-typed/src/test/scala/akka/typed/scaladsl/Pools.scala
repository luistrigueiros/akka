/*
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.typed.scaladsl

import akka.typed._
import akka.typed.scaladsl.Actor._

object RoundRobin {

  case class RoundRobinSettings[T](
    nrOfInstances: Int,
    routeeFactory: ActorContext[T] => Behavior[T])

  def roundRobin[T](settings: RoundRobinSettings[T]) = Deferred[T] { ctx =>
    var current: Int = 0
    val children = (0 to settings.nrOfInstances).map { n =>
      ctx.spawn(Deferred[T] { childCtx =>
        settings.routeeFactory(childCtx)
      }, s"routee-$n")
    }

    Stateful[T] {
      case (_, msg) =>
        children(current) ! msg
        current = (current + 1) % settings.nrOfInstances
        Same
    }
  }

}

object RoundRobinApp extends App {
  val dummy = Stateless[String] {
    case (ctx, msg) => println(ctx.self.path + ": " + msg)
  }

  import RoundRobin._
  val system = ActorSystem("rrobin", roundRobin(RoundRobinSettings[String](5, ctx => dummy)))

  system ! "hello"
  system ! "hello"
  system ! "hello"
  system ! "hello"
  system ! "hello"
  system ! "hello"

}
