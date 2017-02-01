import akka.actor.ActorSystem
import akka.pattern.after
import kamon.Kamon
import kamon.spray.KamonTraceDirectives
import spray.routing.SimpleRoutingApp

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

object SimpleSprayApplication extends App with SimpleRoutingApp with KamonTraceDirectives {
  Kamon.start()
  implicit val system = ActorSystem("simple-spray-application")

  import system.dispatcher

  val someCounter = Kamon.metrics.counter("fabs-some-counter")
  val someFailture = Kamon.metrics.instrumentFactory("TEst")

  startServer("localhost", 9090, serviceActorName = "routing-actor") {
    get {
      path("resource-a") {
        traceName("CoolResourceA") {
          complete {
            someCounter.increment(5)

            for(_ <- 1 to 100000 ){
              List[Double](Math.random, Math.random, Math.random)
            }

            after(5 milliseconds, system.scheduler)(Future.successful("ok"))
          }
        }
      }
    } ~
      path("resource-b") {
        traceName("SadResourceB") {
          complete {
            for(_ <- 1 to 100000 ){
              List[Double](Math.random, Math.random, Math.random)
            }

            after(3 milliseconds, system.scheduler)(Future.successful("ok"))
          }
        }
      }
  } onComplete { _ => "Test" }
}
