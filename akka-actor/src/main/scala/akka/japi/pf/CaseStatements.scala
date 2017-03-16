/**
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */

package akka.japi.pf

import FI.{ Apply, Apply2, Predicate, UnitApply }
import akka.annotation.InternalApi

/**
 * Internal API
 */
@InternalApi
private[akka] object CaseStatement {
  def empty[F, T](): PartialFunction[F, T] = PartialFunction.empty
}

/**
 * Internal API
 */
@InternalApi
private[akka] class CaseStatement[-F, +P, T](predicate: Predicate, apply: Apply[P, T])
  extends PartialFunction[F, T] {

  override def isDefinedAt(o: F) = predicate.defined(o)

  override def apply(o: F) = apply.apply(o.asInstanceOf[P])
}

/**
 * Internal API
 */
@InternalApi
private[akka] class UnitCaseStatement[F, P](predicate: Predicate, apply: UnitApply[P])
  extends PartialFunction[F, Unit] {

  override def isDefinedAt(o: F) = predicate.defined(o)

  override def apply(o: F) = apply.apply(o.asInstanceOf[P])
}
