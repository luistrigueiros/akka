import akka._
import com.typesafe.sbt.pgp.PgpKeys.publishSigned

enablePlugins(JmhPlugin, ScaladocNoVerificationOfDiagrams)
disablePlugins(Unidoc, MimaPlugin)

AkkaBuild.defaultSettings

AkkaBuild.dontPublishSettings
AkkaBuild.dontPublishDocsSettings
Dependencies.benchJmh

libraryDependencies += "io.monix" %% "monix-forkjoin" % "1.0"
