/*
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.typed.scaladsl

import akka.typed.{ActorSystem, PostStop}
import akka.typed.scaladsl.Actor.{Deferred, Stateful}

import scala.concurrent.duration._

object TypicalClusteActor extends App {

  import Actor._

  sealed trait Command

  val thatActor = Deferred[Command] { ctx =>
    // Cluster(ctx.system).subscribe(self.narrow ???)
    case object Tick extends Command
    val cancellable = ctx.schedule(10.seconds, ctx.self, Tick)

    SignalOrMessage[Command]({
      case (ctx, PostStop) =>
        cancellable.cancel()
        Same

      case _  => Unhandled
    }, {
      case (ctx, Tick) =>
        println("tick")
        Stopped
    })
  }

  val system = ActorSystem("whatever", thatActor)



}
