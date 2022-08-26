package com.github.pawelj_pl.nafarmie.http

import io.chrisdavenport.fuuid.FUUID
import io.chrisdavenport.fuuid.circe._
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

import java.time.Instant

final case class Session(sessionId: FUUID, createdAt: Instant)

object Session {

  implicit val codec: Codec[Session] = deriveCodec

}
