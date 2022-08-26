import sbt._

object Dependencies {

  private val kindProjector = compilerPlugin("org.typelevel" % "kind-projector" % Version.plugins.kindProjector cross CrossVersion.full)

  private val zio = "dev.zio" %% "zio" % Version.zio

  private val zioMacros = "dev.zio" %% "zio-macros" % Version.zio

  private val zioCats = "dev.zio" %% "zio-interop-cats" % Version.zioCats

  private val zioSlf4j = "dev.zio" %% "zio-logging-slf4j" % Version.zioLogging

  private val zioTest = "dev.zio" %% "zio-test" % Version.zio % Test

  private val zioTestSbt = "dev.zio" %% "zio-test-sbt" % Version.zio % Test

  private val http4sServer = "org.http4s" %% "http4s-ember-server" % Version.http4s

  private val http4sServerDsl = "org.http4s" %% "http4s-dsl" % Version.http4s

  private val logback = "ch.qos.logback" % "logback-classic" % Version.logback

  private val ciris = "is.cir" %% "ciris" % Version.ciris

  private val fuuid = "io.chrisdavenport" %% "fuuid" % Version.fuuid

  private val fuuidCirce = "io.chrisdavenport" %% "fuuid-circe" % Version.fuuid

  private val jwt = "com.pauldijou" %% "jwt-core" % Version.jwt

  private val circeGeneric = "io.circe" %% "circe-generic" % Version.circe

  private val circeParser = "io.circe" %% "circe-parser" % Version.circe

  val backendDependencies =
    Seq(
      kindProjector,
      zio,
      zioMacros,
      zioCats,
      zioSlf4j,
      zioTest,
      zioTestSbt,
      http4sServer,
      http4sServerDsl,
      logback,
      ciris,
      fuuid,
      fuuidCirce,
      jwt,
      circeGeneric,
      circeParser
    )

}

private object Version {

  object plugins {

    val kindProjector = "0.13.2"

  }

  val zio = "2.0.1"

  val zioCats = "3.3.0"

  val zioLogging = "2.1.0"

  val http4s = "1.0.0-M35"

  val logback = "1.3.0-beta0"

  val ciris = "2.3.3"

  val fuuid = "0.8.0-M2"

  val jwt = "5.0.0"

  val circe = "0.15.0-M1"

}
