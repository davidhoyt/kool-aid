package sbtb.koolaid.twitter

// Courtesy http://blog.abhinav.ca/blog/2015/02/19/scaling-with-akka-streams/

import akka.actor.ActorSystem
import akka.event.{LoggingAdapter, Logging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.LoggingMagnet
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.Source
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import sbtb.koolaid.twitter.client.{User, Tweet, Tweets}

import scala.concurrent.{ExecutionContextExecutor, Future}

trait Service {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

  def tweets(screenNames: Seq[String]): Future[Seq[Tweets]] =
    Source(() => screenNames.toIterable.iterator)
      .grouped(10)
      .mapAsyncUnordered(8)(names => Future(TwitterHelpers.getTweets(names:_*).get))
      .map(_.map {
        case (screenName, tweets) => screenName -> tweets.take(2).map { t =>
          val u = t.getUser
          val msg =
            if (t.isRetweet)
              "RT " + t.getRetweetedStatus.getText
            else
              t.getText
          Tweet(t.getId, t.getCreatedAt.getTime, s"https://twitter.com/${u.getScreenName}/status/${t.getId}", msg.replaceAllLiterally("\n", " "), User(u.getScreenName, u.getName, s"https://twitter.com/${u.getScreenName}", u.getProfileImageURL))
        }
      })
      .mapConcat(identity)
      .runFold(Map[String, Seq[Tweet]]()) {
        case (map, entry) => map + entry
      }
      .map { m =>
        m.toSeq.map {
          case (screenName, tweets) => Tweets(screenName, tweets)
        }
      }

  val routes = {
    pathPrefix("tweets") {
      (get & path(Segment)) { screenNames =>
        logRequest("tweets") {
          complete {
            import prickle._

            tweets(screenNames.split(',').toSeq) map (x => Pickle.intoString(x))
          }
        }
      }
    } ~
    getFromBrowseableDirectory(".")
  }
}

object Main extends App with Service {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}
