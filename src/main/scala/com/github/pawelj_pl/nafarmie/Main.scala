package com.github.pawelj_pl.nafarmie

import com.github.pawelj_pl.nafarmie.config.{AppConfig, SecurityConfig}
import com.github.pawelj_pl.nafarmie.http.{Authentication, HttpServer, Jwt}
import org.http4s.server.Server
import zio.{Scope, ZEnvironment, ZIO, ZIOAppDefault, ZLayer}
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault {

  override val bootstrap = zio.Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  override def run: ZIO[Scope, Throwable, Server] =
    ZIO.log("Starting HTTP server") *> HttpServer.live.provide(AppConfig.live, Jwt.live, Authentication.live, AppConfig.narrow(_.security))

}
