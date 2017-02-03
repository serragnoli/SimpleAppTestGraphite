import akka.actor.ActorSystem
import akka.pattern.after
import kamon.spray.KamonTraceDirectives
import spray.routing.SimpleRoutingApp

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

object SimpleSprayApplication extends App with SimpleRoutingApp with KamonTraceDirectives {
  MetricsUtils.startReport()
  implicit val system = ActorSystem("simple-spray-application")

  val movies = Map("123" -> Movie("123", "Independence Day"), "321" -> Movie("321", "Garfield"))

  import system.dispatcher

  startServer("localhost", 9090, serviceActorName = "routing-actor") {
    get {
      path("resource-a" / Segment) {
        uuid =>
          complete {
            MetricsUtils incrementEndpoint "resourceA"
            MetricsUtils incrementTopHitsMovies movies(uuid)
            println(s"Processed $uuid")

            after(5 milliseconds, system.scheduler)(Future.successful("ok"))
          }
      }

    } ~
      path("resource-b") {
        complete {
          MetricsUtils incrementEndpoint "resourceB"
          after(3 milliseconds, system.scheduler)(Future.successful("ok"))
        }
      }
  } onComplete { _ => "Test" }

  case class Movie(uuid: String, name: String)
}
