package com.github.pawelj_pl.nafarmie

import com.github.pawelj_pl.nafarmie.config.AppConfig
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import zio.{Task, ZIO}
import zio.interop.catz._

object HttpServer {

  val live: ZIO[AppConfig, Throwable, Nothing] = for {
    _      <- ZIO.log("Starting HTTP server")
    config <- ZIO.service[AppConfig]
    server <- EmberServerBuilder
                .default[Task]
                .withHost(config.server.bindAddress)
                .withPort(config.server.port)
                .withHttpApp(TestRoutes.routes.orNotFound)
                .build
                .useForever
  } yield server

}

private object TestRoutes extends Http4sDsl[Task] {

  val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case GET -> Root / "test" => Ok("Hello world")
  }

}
