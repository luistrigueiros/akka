/*
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.typed.scaladsl

import Actor._
import akka.typed.{ActorSystem, Behavior}

object StashyStashMcStash extends App {

  def actor: Behavior[String] = Deferred[String] { ctx =>

    var stash = List[String]()

    def stashing = Stateful[String] {
      case (ctx, "open") =>
        stash.foreach(msg =>
          ctx.self ! msg
        )
        running

      case (_, msg) =>
        stash = msg :: stash
        Same
    }

    def running: Behavior[String] = Stateful[String] {
      case (_, "close") =>
        stashing

      case (_, msg) =>
        println(msg)
        Same
    }

    running
  }


  val system = ActorSystem("stashy", actor)

  system ! "test1"
  system ! "test2"
  system ! "close"
  system ! "test3"
  system ! "test4"
  system ! "open"
  system ! "test5"
  system ! "test6"


}
