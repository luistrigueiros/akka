/*
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.typed.javadsl

import akka.annotation.InternalApi
import akka.typed.Behavior
import akka.japi.pf.FI.{ Apply, TypedPredicate }
import akka.japi.pf.{ CaseStatement, FI }
import akka.japi.function.Function2

/**
 * Internal API
 */
@InternalApi
private[akka] object StatefulBehaviorBuilder {
  private val acceptAnything = new FI.Predicate {
    override def defined(t: AnyRef): Boolean = true
  }

  def apply2ToApplyTuple[A, B, C](apply2: FI.Apply2[A, B, C]): FI.Apply[(A, B), C] = new Apply[(A, B), C] {
    override def apply(i: (A, B)): C = apply2.apply(i._1, i._2)
  }
}

@FunctionalInterface
trait StatefulBehaviorApply[T, M] {
  def apply(context: ActorContext[T], message: M): Behavior[T]
}

final class StatefulBehaviorBuilder[T] private[akka] (msgSuperclass: Class[T]) {

  import StatefulBehaviorBuilder._

  private var statements: PartialFunction[(ActorContext[T], T), Behavior[T]] = null

  private def addStatement(statement: PartialFunction[(ActorContext[T], T), Behavior[T]]) {
    if (statements == null) statements = statement
    else statements = statements.orElse(statement)
  }

  /**
   * Build a {@link scala.PartialFunction} from this builder. After this call
   * the builder will be reset.
   *
   * @return a PartialFunction for this builder.
   */
  def build(): Behavior[T] = {
    val empty = CaseStatement.empty
    if (statements == null) Actor.empty()
    else {
      Actor.stateful[T](new Function2[ActorContext[T], T, Behavior[T]]() {
        override def apply(ctx: ActorContext[T], msg: T): Behavior[T] = {
          val t = (ctx, msg)
          if (statements.isDefinedAt(t)) {
            statements.apply((ctx, msg))
          } else {
            // emulate scala match error
            throw new MatchError(s"$msg of class ${msg.getClass}")
          }
        }
      })
    }
  }

  /**
   * Add a new case statement to this builder.
   *
   * @param type a type to match the message against, must be `T` or a subtype of `T`
   * @param logic an action to apply to the actor context and the message if the type matches
   * @return a builder with the case statement added
   */
  def `match`[M](`type`: Class[M], logic: StatefulBehaviorApply[T, M]): StatefulBehaviorBuilder[T] = {
    if (!msgSuperclass.isAssignableFrom(`type`)) {
      throw new IllegalArgumentException(s"Builder is for messages that are subtypes of [${msgSuperclass.getName}] " +
        s"but [${`type`.getName}] is not")
    }
    val predicate = new FI.Predicate() {
      def defined(o: Any): Boolean = o match {
        case (_, msg) if `type`.isInstance(msg) ⇒ true
        case _                                  ⇒ false
      }
    }
    val tupledApply = new FI.Apply[(ActorContext[T], M), Behavior[T]]() {

      override def apply(i: (ActorContext[T], M)): Behavior[T] = {
        logic.apply(i._1, i._2)
      }
    }
    addStatement(new CaseStatement(predicate, tupledApply))
    this
  }

  /**
   * Add a new case statement to this builder.
   *
   * @param type a type to match the message against
   * @param predicate a predicate that will be evaluated on the message if the type matches
   * @param logic a function to apply to the argument if the type matches and the predicate returns true
   * @return a builder with the case statement added
   */
  def `matchWithPredicate`[M](`type`: Class[M], predicate: FI.TypedPredicate[M], logic: StatefulBehaviorApply[T, M]): StatefulBehaviorBuilder[T] = {
    if (!msgSuperclass.isAssignableFrom(`type`)) {
      throw new IllegalArgumentException(s"Builder is for messages that are subtypes of [${msgSuperclass.getName}] " +
        s"but [${`type`.getName}] is not")
    }
    val combinedPredicate = new FI.Predicate() {
      def defined(o: Any): Boolean = {
        o match {
          case (_, msg) if `type`.isInstance(msg) ⇒ predicate.defined(msg.asInstanceOf[M])
          case _ ⇒ false
        }
      }
    }
    val tupledApply = new FI.Apply[(ActorContext[T], M), Behavior[T]]() {
      def apply(t: (ActorContext[T], M)): Behavior[T] = {
        logic.apply(t._1, t._2)
      }
    }
    addStatement(new CaseStatement(combinedPredicate, tupledApply))
    this
  }

  /**
   * Add a new case statement to this builder, matching the message using equals
   *
   * @param object the object to compare equals with
   * @param logic an action to apply to the argument if the object compares equal
   * @return a builder with the case statement added
   */
  def matchEquals[M](`object`: M, logic: StatefulBehaviorApply[T, M]): StatefulBehaviorBuilder[T] = {
    if (!msgSuperclass.isAssignableFrom(`object`.getClass)) {
      throw new IllegalArgumentException(s"Builder is for messages that are subtypes of [${msgSuperclass.getName}] " +
        s"but [${`object`}] is not")
    }
    val predicate = new FI.TypedPredicate[M] {
      override def defined(t: M): Boolean = t.equals(`object`)
    }
    `matchWithPredicate`(`object`.getClass.asInstanceOf[Class[M]], predicate, logic)
    this
  }

  /**
   * Add a new case statement to this builder, that matches any message.
   *
   * @param apply
   * an action to apply to the argument
   * @return a builder with the case statement added
   */
  def matchAny(apply: FI.Apply2[ActorContext[T], T, Behavior[T]]): StatefulBehaviorBuilder[T] = {
    addStatement(new CaseStatement(
      acceptAnything,
      apply2ToApplyTuple(apply)))
    this
  }

}
