package my.will.be.done.templatematching

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import io.circe.{Decoder, Json}
import io.circe.generic.auto._
import de.heikoseeberger.akkahttpcirce.CirceSupport._

object WebServer extends App {
  implicit val system = ActorSystem("template-matching")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val port = args(0).toInt

  case class FullPart(full: String, part: String)

  def doesImageContainPart(full: String, part: String): Json = {
    Json.fromBoolean(ImageTester.doesImageContainPart(full, part))
  }

  val route =
    path("") {
      get {
        parameters("full", "part") { (full, part) ⇒
          complete {
            doesImageContainPart(full, part)
          }
        }
      } ~ post {
        entity(as[FullPart]) { fullPart ⇒
          complete {
            doesImageContainPart(fullPart.full, fullPart.part)
          }
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", port)

  println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
