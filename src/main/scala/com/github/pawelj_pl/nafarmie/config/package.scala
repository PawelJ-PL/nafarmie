package com.github.pawelj_pl.nafarmie

import ciris.ConfigDecoder
import com.comcast.ip4s.{Host, Port}

package object config {

  object instances {

    implicit val hostDecoder: ConfigDecoder[String, Host] = ConfigDecoder[String, String].mapOption("Host")(Host.fromString)

    implicit val portDecoder: ConfigDecoder[String, Port] = ConfigDecoder[String, String].mapOption("Port")(Port.fromString)

  }

}
