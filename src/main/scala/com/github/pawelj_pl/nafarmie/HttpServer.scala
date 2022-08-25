package com.github.pawelj_pl.nafarmie

import com.comcast.ip4s.IpLiteralSyntax
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.Server
import zio.Task
import zio.interop.catz._
import zio.managed.ZManaged

object HttpServer {

  val live: ZManaged[Any, Throwable, Server] = ZManaged.log("Starting HTTP server") *> EmberServerBuilder
    .default[Task]
    .withHost(ipv4"0.0.0.0")
    .withPort(port"8080")
    .withHttpApp(TestRoutes.routes.orNotFound)
    .build
    .toManagedZIO

}

private object TestRoutes extends Http4sDsl[Task] {

  val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {
    case GET -> Root / "test" => Ok("Hello world")
  }

}
