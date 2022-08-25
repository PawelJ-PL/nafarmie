import sbt._

object Dependencies {

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

  val backendDependencies = Seq(zio, zioMacros, zioCats, zioSlf4j, zioTest, zioTestSbt, http4sServer, http4sServerDsl, logback, ciris)

}

private object Version {

  val zio = "2.0.1"

  val zioCats = "3.3.0"

  val zioLogging = "2.1.0"

  val http4s = "1.0.0-M35"

  val logback = "1.3.0-beta0"

  val ciris = "2.3.3"

}
