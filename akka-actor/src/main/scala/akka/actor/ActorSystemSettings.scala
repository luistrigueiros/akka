/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.actor

import java.util.Optional

import scala.annotation.varargs
import scala.compat.java8.OptionConverters._
import scala.reflect.ClassTag

/**
 * Marker trait for a setting that can be put inside [[ActorSystemSettings]]
 */
trait ActorSystemSetting

object ActorSystemSettings {

  val empty = new ActorSystemSettings(Map.empty)

  /**
   * Scala API:
   */
  def apply(settings: ActorSystemSetting*): ActorSystemSettings =
    new ActorSystemSettings(settings.map(s ⇒ s.getClass → s).toMap)

  /**
   * Java API:
   */
  @varargs
  def create(settings: ActorSystemSetting*): ActorSystemSettings = apply(settings: _*)
}

/**
 * A set of settings for programatic configuration of the actor system
 */
final class ActorSystemSettings(settings: Map[Class[_], AnyRef]) {

  /**
   * Java API: Extract a concrete [[ActorSystemSetting]] if it is defined
   */
  def get[T <: ActorSystemSetting](clazz: Class[T]): Optional[T] = {
    settings.get(clazz).map(_.asInstanceOf[T]).asJava
  }

  /**
   * Scala API: Extract a concrete [[ActorSystemSetting]] if it is defined
   */
  def get[T <: ActorSystemSetting: ClassTag]: Option[T] = {
    val clazz = implicitly[ClassTag[T]].runtimeClass
    settings.get(clazz).map(_.asInstanceOf[T])
  }

  /**
   * Add a concrete [[ActorSystemSetting]]. If a value of the same setting already was present it will be
   * replaced with this.
   */
  def withSetting[T <: ActorSystemSetting](t: T): ActorSystemSettings = {
    new ActorSystemSettings(settings + (t.getClass → t))
  }

  override def toString: String = s"""ActorSystemSettings(${settings.keys.map(_.getName).mkString(",")})"""
}
