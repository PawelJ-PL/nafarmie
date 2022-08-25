package com.github.pawelj_pl.nafarmie

import com.comcast.ip4s.IpLiteralSyntax
import com.github.pawelj_pl.nafarmie.config.AppConfig
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.Server
import zio.{Task, ZIO}
import zio.interop.catz._
import zio.managed.ZManaged

object HttpServer {

  val live: ZManaged[AppConfig, Throwable, Server] = for {
    _      <- ZManaged.log("Starting HTTP server")
    config <- ZManaged.fromZIO(ZIO.service[AppConfig])
    server <- EmberServerBuilder
                .default[Task]
                .withHost(config.server.bindAddress)
                .withPort(config.server.port)
                .withHttpApp(TestRoutes.routes.orNotFound)
                .build
                .toManagedZIO
  } yield server

}

private object TestRoutes extends Http4sDsl[Task] {

  val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case GET -> Root / "test" => Ok("Hello world")
  }

}
