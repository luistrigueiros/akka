/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.actor

import java.util.Optional

import scala.annotation.varargs
import scala.compat.java8.OptionConverters._
import scala.reflect.ClassTag

/**
 * Marker trait for a setting that can be put inside [[ActorSystemSettings]], if a specific concrete setting
 * is not specified in the settings that means defaults are used (usually from the config file) - no concrete
 * setting should be mandatory in the [[ActorSystemSettings]] that an actor system is created with.
 */
trait ActorSystemSetting {

  /**
   * Construct an [[ActorSystemSettings]] with this setting and another one. Allows for
   * fluent creation of settings.
   */
  def and(other: ActorSystemSetting): ActorSystemSettings = ActorSystemSettings(this, other)

}

object ActorSystemSettings {

  val empty = new ActorSystemSettings(Map.empty)

  /**
   * Scala API: Create an [[ActorSystemSettings]] containing all the provided settings
   */
  def apply(settings: ActorSystemSetting*): ActorSystemSettings =
    new ActorSystemSettings(settings.map(s ⇒ s.getClass → s).toMap)

  /**
   * Java API: Create an [[ActorSystemSettings]] containing all the provided settings
   */
  @varargs
  def create(settings: ActorSystemSetting*): ActorSystemSettings = apply(settings: _*)
}

/**
 * A set of settings for programatic configuration of the actor system
 */
final class ActorSystemSettings(settings: Map[Class[_], AnyRef]) {

  /**
   * Java API: Extract a concrete [[ActorSystemSetting]] of type `T` if it is defined in the settings.
   */
  def get[T <: ActorSystemSetting](clazz: Class[T]): Optional[T] = {
    settings.get(clazz).map(_.asInstanceOf[T]).asJava
  }

  /**
   * Scala API: Extract a concrete [[ActorSystemSetting]] of type `T` if it is defined in the settings.
   */
  def get[T <: ActorSystemSetting: ClassTag]: Option[T] = {
    val clazz = implicitly[ClassTag[T]].runtimeClass
    settings.get(clazz).map(_.asInstanceOf[T])
  }

  /**
   * Add a concrete [[ActorSystemSetting]]. If a setting of the same concrete [[ActorSystemSetting]] already is
   * present it will be.
   */
  def withSetting[T <: ActorSystemSetting](t: T): ActorSystemSettings = {
    new ActorSystemSettings(settings + (t.getClass → t))
  }

  /**
   * alias for `withSetting` allowing for fluent combination of settings: `a and b and c`, where `a`, `b` and `c` are
   * concrete [[ActorSystemSetting]] instances.
   */
  def and[T <: ActorSystemSetting](t: T): ActorSystemSettings = withSetting(t)

  override def toString: String = s"""ActorSystemSettings(${settings.keys.map(_.getName).mkString(",")})"""
}
