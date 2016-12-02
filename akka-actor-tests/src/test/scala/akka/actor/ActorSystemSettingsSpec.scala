/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.actor

import akka.testkit.TestKit
import org.scalatest.{ Matchers, WordSpec }

case class DummySetting(name: String) extends ActorSystemSetting
case class DummySetting2(name: String) extends ActorSystemSetting
case class DummySetting3(name: String) extends ActorSystemSetting

class ActorSystemSettingsSpec extends WordSpec with Matchers {

  "The ActorSystemSettings" should {

    "store and retrieve a setting" in {
      val setting = DummySetting("Al Dente")
      val settings = ActorSystemSettings()
        .withSetting(setting)

      settings.get[DummySetting] should ===(Some(setting))
      settings.get[DummySetting2] should ===(None)
    }

    "replace setting if already defined" in {
      val setting1 = DummySetting("Al Dente")
      val setting2 = DummySetting("Earl E. Bird")
      val settings = ActorSystemSettings()
        .withSetting(setting1)
        .withSetting(setting2)

      settings.get[DummySetting] should ===(Some(setting2))
    }

    "be created with a set of settings" in {
      val setting1 = DummySetting("Manny Kin")
      val setting2 = DummySetting2("Pepe Roni")
      val settings = ActorSystemSettings(setting1, setting2)

      settings.get[DummySetting].isDefined shouldBe true
      settings.get[DummySetting2].isDefined shouldBe true
      settings.get[DummySetting3].isDefined shouldBe false
    }

    "be available from the ExtendedActorSystem" in {
      var system: ActorSystem = null
      try {
        val setting = DummySetting("Tad Moore")
        system = ActorSystem("name", ActorSystemSettings(setting))

        system
          .settings
          .actorSystemSettings
          .get[DummySetting] should ===(Some(setting))

      } finally {
        TestKit.shutdownActorSystem(system)
      }
    }
  }

}
