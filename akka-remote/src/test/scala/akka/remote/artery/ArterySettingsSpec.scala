/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.remote.artery

import akka.actor.{ ActorSystem, ActorSystemBootstrapSettings, ActorSystemSettings }
import akka.remote.{ RARP, RemotingSettings }
import akka.testkit.{ SocketUtil, TestKit }
import com.typesafe.config.ConfigFactory
import org.scalatest.{ Matchers, WordSpec }

class ArterySettingsSpec extends WordSpec with Matchers {

  "The artery settings" should {

    "pickup programmatic settings" in {
      var system: ActorSystem = null
      try {
        val (host, port) = SocketUtil.temporaryServerHostnameAndPort()
        system = ActorSystem(
          "programmatic-artery-config",
          ActorSystemBootstrapSettings().withActorRefProvider("remote") and RemotingSettings.artery(host, port)
        )

        RARP(system).provider.remoteSettings.Artery.Enabled should ===(true)
        val address = RARP(system).provider.getDefaultAddress
        address.host should ===(Some(host))
        address.port should ===(Some(port))

      } finally {
        TestKit.shutdownActorSystem(system)
      }

    }

  }

}
