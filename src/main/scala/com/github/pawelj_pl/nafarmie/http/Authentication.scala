package com.github.pawelj_pl.nafarmie.http

import cats.data.{Kleisli, OptionT}
import cats.instances.string._
import cats.syntax.eq._
import io.chrisdavenport.fuuid.FUUID
import org.http4s.{ContextRequest, Request, Response, ResponseCookie, Status}
import org.http4s.server.{AuthMiddleware, Middleware}
import zio.{Task, ZIO, ZLayer}
import zio.interop.catz._

trait Authentication {

  type SessionMiddleware = Middleware[OptionT[Task, *], ContextRequest[Task, Session], Response[Task], Request[Task], Response[Task]]

  def sessionMiddleware: ZIO[Any, Nothing, AuthMiddleware[Task, Session]]

}

object Authentication {

  val live: ZLayer[Jwt, Nothing, Authentication] = ZLayer {
    ZIO.service[Jwt].map(AuthenticationLive)
  }

}

private case class AuthenticationLive(jwt: Jwt) extends Authentication {

  private final val CookieName = "session"

  private final val YearSeconds: Long = 60 * 60 * 24 * 365

  private def getExistingSession(req: Request[Task]): ZIO[Any, String, Session] =
    for {
      cookie  <- ZIO.fromOption(req.cookies.find(_.name === CookieName)).orElseFail("Session cookie not found")
      session <- jwt.decode(cookie.content).mapError(err => err.getMessage)
    } yield session

  private def createSession(): Task[Session] =
    for {
      sessionId <- FUUID.randomFUUID[Task]
      now       <- zio.Clock.instant
    } yield Session(sessionId, now)

  private def responseWithCookie(resp: Response[Task], session: Session): Task[Response[Task]] =
    jwt
      .encode(session)
      .flatMap(token =>
        ZIO.succeed(resp.addCookie(ResponseCookie(name = CookieName, content = token, path = Some("/"), maxAge = Some(100 * YearSeconds))))
      )

  override def sessionMiddleware: ZIO[Any, Nothing, SessionMiddleware] = {
    val middleware: SessionMiddleware = { service =>
      Kleisli { (req: Request[Task]) =>
        val sessionService =
          Kleisli[Task, Request[Task], Session](req => getExistingSession(req).catchAll(error => ZIO.logWarning(error) *> createSession()))
        val resp =
          sessionService(req)
            .flatMap(session =>
              service(ContextRequest(session, req))
                .semiflatMap(resp => responseWithCookie(resp, session))
                .getOrElse(Response[Task](Status.NotFound))
            )
        OptionT.liftF(resp)
      }
    }
    ZIO.succeed(middleware)
  }

}
