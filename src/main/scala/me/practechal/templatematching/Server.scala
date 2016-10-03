package me.practechal.templatematching

import rapture.mime.MimeTypes
import rapture.http._, httpBackends.jetty._
import rapture.io._
import rapture.codec._, encodings.`UTF-8`._
import rapture.json._, jsonBackends.circe._

object Server extends App {

  case class FullPart(full: String, part: String)

  HttpServer.listen(args(0).toInt) { request ⇒
    val fullPart = Json.parse(request.body).as[FullPart]
    val boolean = ImageTester.doesImageContainPart(fullPart.full, fullPart.part)
    StreamResponse(
      200, Nil, MimeTypes.`application/json`, { os ⇒
        boolean.toString.input > os
        os.close()
      }
    )
  }
}
