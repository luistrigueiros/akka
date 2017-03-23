/*
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.typed.scaladsl

import akka.typed.{ActorRef, ActorSystem, Behavior}
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object ScalaAlarmSample extends App {

  trait RootMessage
  case class SetupAlarm(password: String, recipient: ActorRef[AlarmCreated]) extends RootMessage
  case class AlarmCreated(alarm: ActorRef[AlarmMessage])

  sealed trait AlarmMessage
  case class TurnOnAlarm(password: String) extends AlarmMessage
  case class TurnOffAlarm(password: String) extends AlarmMessage
  case class ChangePassword(oldPassword: String, newPassword: String) extends AlarmMessage
  case object Activity extends AlarmMessage

  import Actor._

  def disabledAlarm(password: String): Behavior[AlarmMessage] = Stateful {
    case (_, TurnOnAlarm(pass)) if pass == password ⇒
      println("Alarm turned on")
      enabledAlarm(password)
    case (_, TurnOnAlarm(_)) ⇒
      println("Someone with the wrong password tried to turn on the alarm")
      Same
    case (_, ChangePassword(newPassword, oldPassword)) if oldPassword == password ⇒
      println("Setting a new password")
      disabledAlarm(newPassword)
    case _ ⇒
      Unhandled
  }

  def enabledAlarm(password: String): Behavior[AlarmMessage] = Stateful {
    case (_, TurnOffAlarm(pass)) if pass == password ⇒
      println("Alarm turned off")
      disabledAlarm(password)
    case (_, TurnOffAlarm(_)) ⇒
      println("Someone with the wrong password tried to turn off the alarm")
      Same
    case (_, Activity) ⇒
      println("Detected activity, oeoeoeoeoe!")
      Same
    case _ ⇒
      Unhandled
  }

  val guardian = Actor.Stateful[RootMessage] {
    case (ctx, SetupAlarm(initialPass, recpipent)) ⇒
      val alarmRef = ctx.spawn(disabledAlarm(initialPass), "alarm")
      recpipent ! AlarmCreated(alarmRef)
      Actor.Same
  }

  val system = ActorSystem("whatever", guardian)

  implicit val askTimeout = Timeout(3.seconds)
  import akka.typed.scaladsl.AskPattern._
  implicit val scheduler = system.scheduler
  val response: Future[AlarmCreated] = system ? (ref ⇒ SetupAlarm("secret", ref))

  val initialized = Await.result(response, 3.seconds)
  val alarm = initialized.alarm
  alarm ! Activity
  alarm ! TurnOnAlarm("secret")
  alarm ! TurnOffAlarm("wrong")
  alarm ! Activity
  alarm ! TurnOffAlarm("secret")
  alarm ! Activity

}
