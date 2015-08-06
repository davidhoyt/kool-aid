package sbtb.koolaid.twitter.client

import java.io.IOException

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{Unmarshaller, Unmarshal}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source, Flow}
import prickle.{Unpickle, Unpickler}

import scala.concurrent.{Future, ExecutionContext}

/**
 * Provides Scala.JS and JVM agnostic version of making requests to HTTP endpoints.
 */
trait ClientContext {
  implicit val executionContext: ExecutionContext
  def get[T : Unpickler](url: String): Future[T]
}

object ClientContext {
  /**
   * Produces a [[ClientContext]] for use with Scala.JS applications.
   */
  implicit def js(implicit ec: ExecutionContext): ClientContext = new ClientContext {
    override implicit val executionContext = ec
    override def get[T : Unpickler](url: String): Future[T] = {
      import org.scalajs.dom
      println(s"Calling $url")
      dom.ext.Ajax.get(url = url)
        .map(_.responseText)(ec)
        .flatMap { s =>
          val p = Unpickle[T].fromString(s)
          Future.fromTry(p)
        }(ec)
    }
  }

  /**
   * Produces a [[ClientContext]] for use with JVM applications.
   */
  implicit def jvm(implicit ec: ExecutionContext, system: ActorSystem): ClientContext = new ClientContext {
    override implicit val executionContext = ec
    implicit val materializer = ActorMaterializer()

    lazy val connectionFlow: Flow[HttpRequest, HttpResponse, Any] =
      Http(system).outgoingConnection("localhost", 23456)

    override def get[T : Unpickler](url: String): Future[T] = {
      val request = HttpRequest(method = HttpMethods.GET, Uri(url))
      val f = Source.single(request).via(connectionFlow).runWith(Sink.head)
      f.flatMap { response =>
        response.status match {
          case StatusCodes.OK =>
            Unmarshal(response.entity).to[String](implicitly[Unmarshaller[ResponseEntity, String]], ec).flatMap { s =>
              val p = Unpickle[T].fromString(s)
              Future.fromTry(p)
            }(ec)
          case _ =>
            Future.failed(new IOException("Request failed"))
        }
      }(ec)
    }
  }
}

