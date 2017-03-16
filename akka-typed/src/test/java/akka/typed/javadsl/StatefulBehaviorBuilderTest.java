/*
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.typed.javadsl;

import akka.typed.Behavior;
import org.junit.Assert;
import org.junit.Test;
import org.scalatest.junit.JUnitSuite;
import scala.MatchError;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class StatefulBehaviorBuilderTest extends JUnitSuite {

  interface MsgSuperclass {}
  static class Msg implements MsgSuperclass {
    final String text;
    Msg(String text) { this.text = text; }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Msg msg = (Msg) o;
      return text != null ? text.equals(msg.text) : msg.text == null;
    }

    @Override
    public int hashCode() {
      return text != null ? text.hashCode() : 0;
    }
  }

  static class OtherMsg implements MsgSuperclass { }

  static class Unrelated {
    final String text;
    Unrelated(String text) {
      this.text = text;
    }
  }

  @Test
  public void matchShouldMatchOnClass() throws Exception {
    // just to close over something mutable
    AtomicReference<Msg> gotMsg = new AtomicReference<>();

    Behavior<MsgSuperclass> behavior =
        new StatefulBehaviorBuilder<>(MsgSuperclass.class)
            .match(Msg.class, (context, msg) -> {
              gotMsg.set(msg);
              return Actor.same();
            }).build();

    Msg message = new Msg("whatever");
    Behavior<MsgSuperclass> newBehavior = behavior.message(null, message);

    assertEquals(newBehavior, Actor.same());
    assertSame(gotMsg.get(), message);

    try {
      OtherMsg otherMsg = new OtherMsg();
      behavior.message(null, otherMsg);
      Assert.fail("Expected MatchError");
    } catch (MatchError matchError) {
      // This is fine
    }
  }

  @Test
  public void matchShouldThrowIfMatchedClassIsNotSubtypeOfMsgSupertype() {
    try {
      new StatefulBehaviorBuilder<>(MsgSuperclass.class)
          .match(Unrelated.class, (ctx, msg) -> Actor.same()).build();
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      // This is fine
    } catch (Throwable ex) {
      Assert.fail("Expected IllegalArgumentException but got " + ex);
    }
  }

  @Test
  public void matchShouldMatchOnClassWithPredicate() throws Exception {
    // just to close over something mutable
    AtomicReference<Msg> gotMsg = new AtomicReference<>();

    Behavior<MsgSuperclass> behavior =
        new StatefulBehaviorBuilder<>(MsgSuperclass.class)
            .matchWithPredicate(
                Msg.class,
                (msg) -> msg.text.equals("whatever"),
                (context, msg) -> {
              gotMsg.set(msg);
              return Actor.same();
            }).build();

    Msg whateverMessage = new Msg("whatever");
    Behavior<MsgSuperclass> newBehavior = behavior.message(null, whateverMessage);

    assertEquals(newBehavior, Actor.same());
    assertSame(gotMsg.get(), whateverMessage);

    try {
      Msg otherMessage = new Msg("othermessage");
      behavior.message(null, otherMessage);
      Assert.fail("Expected MatchError");
    } catch (MatchError matchError) {
      // This is fine
    }
  }

  @Test
  public void matchWithPredicateShouldThrowIfMatchedClassIsNotSubtypeOfMsgSupertype() {
    try {
      new StatefulBehaviorBuilder<>(MsgSuperclass.class)
          .matchWithPredicate(Unrelated.class, msg -> true, (ctx, msg) -> Actor.same()).build();
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      // This is fine
    }
  }


  @Test
  public void matchShouldMatchEquals() throws Exception {
    // just to close over something mutable
    AtomicReference<Msg> gotMsg = new AtomicReference<>();

    Msg expected = new Msg("expected");
    Behavior<MsgSuperclass> behavior =
        new StatefulBehaviorBuilder<>(MsgSuperclass.class)
            .matchEquals(expected, (context, msg) -> {
              gotMsg.set(msg);
              return Actor.same();
            }).build();

    Behavior<MsgSuperclass> newBehavior = behavior.message(null, expected);

    assertEquals(newBehavior, Actor.same());
    assertSame(gotMsg.get(), expected);

    try {
      OtherMsg otherMsg = new OtherMsg();
      behavior.message(null, otherMsg);
      Assert.fail("Expected MatchError");
    } catch (MatchError matchError) {
      // This is fine
    }
  }

  @Test
  public void matchEqualsShouldThrowIfMatchedClassIsNotSubtypeOfMsgSupertype() {
    try {
      new StatefulBehaviorBuilder<>(MsgSuperclass.class)
          .matchEquals(new Unrelated("whatever"), (ctx, msg) -> Actor.same()).build();
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      // This is fine
    } catch (Throwable ex) {
      Assert.fail("Expected IllegalArgumentException but got " + ex);
    }
  }

  @Test
  public void matchShouldMatchAny() throws Exception {
    // just to close over something mutable
    AtomicReference<MsgSuperclass> gotMsg = new AtomicReference<>();

    Behavior<MsgSuperclass> behavior =
        new StatefulBehaviorBuilder<>(MsgSuperclass.class)
            .matchAny(
                (context, msg) -> {
                  gotMsg.set(msg);
                  return Actor.same();
                }).build();

    Msg whateverMessage = new Msg("whatever");
    Behavior<MsgSuperclass> newBehavior = behavior.message(null, whateverMessage);

    assertEquals(newBehavior, Actor.same());
    assertSame(gotMsg.get(), whateverMessage);
  }

  @Test
  public void earlierMatchShouldHavePrecedence() throws Exception {
    // just to close over something mutable
    AtomicReference<Msg> firstMatch = new AtomicReference<>();
    AtomicReference<Msg> secondMatch = new AtomicReference<>();

    Msg expected = new Msg("expected");
    Behavior<MsgSuperclass> behavior =
        new StatefulBehaviorBuilder<>(MsgSuperclass.class)
            .matchEquals(expected, (context, msg) -> {
              firstMatch.set(msg);
              return Actor.same();
            }).matchEquals(expected, (context, msg) -> {
              secondMatch.set(msg);
              return Actor.same();
            }).build();

    Behavior<MsgSuperclass> newBehavior = behavior.message(null, expected);

    assertEquals(newBehavior, Actor.same());
    assertSame(firstMatch.get(), expected);
    assertNull(secondMatch.get());
  }

}
