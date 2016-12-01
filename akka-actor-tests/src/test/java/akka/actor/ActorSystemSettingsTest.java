/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.actor;

import org.junit.Test;
import org.scalatest.junit.JUnitSuite;
import scala.Option;

import java.util.Optional;

import static org.junit.Assert.*;

public class ActorSystemSettingsTest extends JUnitSuite {

  static class JavaSetting implements ActorSystemSetting {
    public final String name;
    public JavaSetting(String name) {
      this.name = name;
    }
  }

  @Test
  public void apiMustBeUsableFromJava() {
    final JavaSetting javaSetting = new JavaSetting("Jasmine Rice");
    final Optional<JavaSetting> result = ActorSystemSettings.create()
        .withSetting(javaSetting)
        .get(JavaSetting.class);

    assertTrue(result.isPresent());
    assertEquals(result.get(), javaSetting);

  }

}
