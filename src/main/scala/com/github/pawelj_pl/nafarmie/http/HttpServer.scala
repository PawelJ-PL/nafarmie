package com.github.pawelj_pl.nafarmie.http

import cats.syntax.show._
import cats.instances.string._
import com.github.pawelj_pl.nafarmie.config.AppConfig
import org.http4s.{AuthedRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import zio.interop.catz._
import zio.{Task, ZIO}

object HttpServer {

  val live: ZIO[AppConfig with Authentication, Throwable, Nothing] = for {
    config            <- ZIO.service[AppConfig]
    authentication    <- ZIO.service[Authentication]
    sessionMiddleware <- authentication.sessionMiddleware
    server            <- EmberServerBuilder
                           .default[Task]
                           .withHost(config.server.bindAddress)
                           .withPort(config.server.port)
                           .withHttpApp(sessionMiddleware(TestRoutes.routes).orNotFound)
                           .build
                           .useForever
  } yield server

}

private object TestRoutes extends Http4sDsl[Task] {

  val routes: AuthedRoutes[Session, Task] = AuthedRoutes.of[Session, Task] {
    case authReq @ GET -> Root / "test" as session => Ok(show"Hello ${authReq.context.sessionId}")
  }

}
