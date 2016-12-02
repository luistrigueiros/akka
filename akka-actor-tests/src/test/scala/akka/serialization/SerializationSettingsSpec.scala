/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.serialization

import akka.actor.{ ActorSystem, ActorSystemBootstrapSettings, ActorSystemSettings }
import akka.testkit.AkkaSpec
import com.typesafe.config.ConfigFactory

class ConfigurationDummy
class ProgrammaticDummy

object SerializationSettingsSpec {

  val testSerializer = new TestSerializer

  val serializationSettings = SerializationSettings { _ ⇒
    List(
      SerializerDetails("test", testSerializer, List(classOf[ProgrammaticDummy]))
    )
  }
  val bootstrapSettings = ActorSystemBootstrapSettings(None, Some(ConfigFactory.parseString("""
    akka {
      actor {
        serialize-messages = off
        serialization-bindings {
          "akka.serialization.ConfigurationDummy" = test
        }
      }
    }
    """)), None)
  val actorSystemSettings = ActorSystemSettings(bootstrapSettings, serializationSettings)

}

class SerializationSettingsSpec extends AkkaSpec(
  ActorSystem("SerializationSettingsSpec", SerializationSettingsSpec.actorSystemSettings)
) {

  import SerializationSettingsSpec._

  "The serialization settings" should {

    "allow for programmatic configuration of serializers" in {
      val serializer = SerializationExtension(system).findSerializerFor(new ProgrammaticDummy)
      serializer shouldBe theSameInstanceAs(testSerializer)
    }

    "allow a configured binding to hook up to a programmatic serializer" in {
      val serializer = SerializationExtension(system).findSerializerFor(new ConfigurationDummy)
      serializer shouldBe theSameInstanceAs(testSerializer)
    }

  }

}
