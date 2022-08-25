package com.github.pawelj_pl.nafarmie

import com.github.pawelj_pl.nafarmie.config.AppConfig
import org.http4s.server.Server
import zio.{Scope, ZIO, ZIOAppDefault}
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault {

  override val bootstrap = zio.Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  override def run: ZIO[Scope, Throwable, Server] = HttpServer.live.provide(AppConfig.live)

}
