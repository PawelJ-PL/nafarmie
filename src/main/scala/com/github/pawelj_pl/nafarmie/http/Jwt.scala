package com.github.pawelj_pl.nafarmie.http

import com.github.pawelj_pl.nafarmie.config.SecurityConfig
import io.circe.parser.parse
import io.circe.syntax._
import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtHeader, Jwt => JwtLib}
import zio.macros.accessible
import zio.{Task, ZIO, ZLayer}

@accessible
trait Jwt {

  def decode(token: String): Task[Session]

  def encode(session: Session): Task[String]

}

object Jwt {

  val live: ZLayer[SecurityConfig, Nothing, Jwt] = ZLayer {
    ZIO.service[SecurityConfig].map(JwtLive)
  }

}

private case class JwtLive(securityConfig: SecurityConfig) extends Jwt {

  private val jwtAlgorithm = JwtAlgorithm.HS256

  override def decode(token: String): Task[Session] =
    for {
      claims      <- ZIO.fromTry(JwtLib.decode(token, securityConfig.jwtKey.value, Seq(jwtAlgorithm)))
      contentJson <- ZIO.fromEither(parse(claims.content))
      session     <- ZIO.fromEither(contentJson.as[Session])
    } yield session

  override def encode(session: Session): Task[String] =
    for {
      now   <- zio.Clock.instant
      claims = JwtClaim( session.asJson.noSpaces).issuedAt(now.getEpochSecond)
      token <- ZIO.attempt(JwtLib.encode(JwtHeader(jwtAlgorithm), claims, securityConfig.jwtKey.value))
    } yield token

}
