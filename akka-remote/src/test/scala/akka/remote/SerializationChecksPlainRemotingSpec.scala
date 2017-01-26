/**
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.remote

import akka.testkit.AkkaSpec

class SerializationChecksPlainRemotingSpec extends AkkaSpec {

  "Settings serialize-messages" must {

    "be on for tests" in {
      // serialize creators are more selectively enabled
      // because not everything needs to be remotely deployable
      system.settings.SerializeAllMessages should ===(true)
    }

  }

}
