package com.github.pawelj_pl.nafarmie.config

import cats.syntax.parallel._
import com.github.pawelj_pl.nafarmie.config.instances._
import ciris.{Secret, env}
import com.comcast.ip4s.{Host, IpLiteralSyntax, Port}
import zio.{Tag, Task, ZEnvironment, ZLayer}
import zio.interop.catz._

object AppConfig {

  private val serverConfig = (
    env("HTTP_ADDRESS").as[Host].default(host"0.0.0.0"),
    env("HTTP_PORT").or(env("PORT")).as[Port].default(port"8080")
  ).parMapN(ServerConfig)

  private val securityConfig = env("JWT_KEY").as[String].secret.map(SecurityConfig)

  private val appConfig = (serverConfig, securityConfig).parMapN(AppConfig.apply)

  val live: ZLayer[Any, Throwable, AppConfig] = ZLayer.fromZIO(appConfig.load[Task])

  def narrow[A: Tag](extract: AppConfig => A): ZLayer[Any, Throwable, A] = live.map(configEnv => ZEnvironment(extract(configEnv.get)))

}

final case class AppConfig(server: ServerConfig, security: SecurityConfig)

final case class ServerConfig(bindAddress: Host, port: Port)

final case class SecurityConfig(jwtKey: Secret[String])
