package com.github.pawelj_pl.nafarmie.config

import cats.syntax.parallel._
import com.github.pawelj_pl.nafarmie.config.instances._
import ciris.env
import com.comcast.ip4s.{Host, IpLiteralSyntax, Port}
import zio.{Task, ZLayer}
import zio.interop.catz._

object AppConfig {

  private val serverConfig = (
    env("HTTP_ADDRESS").as[Host].default(host"0.0.0.0"),
    env("HTTP_PORT").or(env("PORT")).as[Port].default(port"8080")
  ).parMapN(ServerConfig)

  private val appConfig = serverConfig.map(AppConfig.apply)

  val live: ZLayer[Any, Throwable, AppConfig] = ZLayer.fromZIO(appConfig.load[Task])

}

final case class AppConfig(server: ServerConfig)

final case class ServerConfig(bindAddress: Host, port: Port)
