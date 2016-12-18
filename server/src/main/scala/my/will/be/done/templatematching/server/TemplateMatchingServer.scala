package my.will.be.done.templatematching.server

import akka.stream.scaladsl.FileIO
import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import io.circe.{Decoder, Json}
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.CirceSupport._
import com.typesafe.config.ConfigFactory
import example._ // TODO: figure out twirl file/package structure

object TemplateMatchingServer extends App {
  implicit val system = ActorSystem("template-matching")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val config = ConfigFactory.load()
  val interface = config.getString("http.interface")
  val port = config.getInt("http.port")

  case class FullPart(full: String, part: String)

  def doesImageContainPart(full: String, part: String): Json = {
    Json.fromBoolean(ImageTester.doesImageContainPart(full, part))
  }

  def handleFilePart(filePart: BodyPart) = {
    val name = filePart.name
    val tmpFile = File.createTempFile(name, filePart.filename.orNull)
    filePart.entity.dataBytes.runWith(FileIO.toPath(tmpFile.toPath)).map{ _ =>
      name -> tmpFile
    }
  }

  val route =
    pathSingleSlash {
      get {
        complete {
          example.html.index.render()
        }
      }
    } ~
      pathPrefix("assets" / Remaining) { file =>
        // optionally compresses the response with Gzip or Deflate
        // if the client accepts compressed responses
        encodeResponse {
          getFromResource("public/" + file)
        }
      } ~
    path("api") {
      get {
        parameters("full", "part") { (full, part) ⇒
          complete {
            doesImageContainPart(full, part)
          }
        }
      } ~ post {
        entity(as[Multipart.FormData]) { formData ⇒
          onSuccess(
            formData.parts.mapAsync[(String, File)](1) {
              case full: BodyPart if full.name == "full" ⇒
                handleFilePart(full)
              case part: BodyPart if part.name == "part" ⇒
                handleFilePart(part)
            }.runFold(Map.empty[String, File])((map, tuple) => map + tuple)
          ) { fileMap ⇒
            complete {
              Json.fromBoolean(ImageTester.doesImageContainPart(fileMap("full"), fileMap("part")))
            }
          }
        } ~ entity(as[FullPart]) { fullPart ⇒
          complete {
            doesImageContainPart(fullPart.full, fullPart.part)
          }
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, interface, port)

  println(s"Server online at http://$interface:$port")
}
