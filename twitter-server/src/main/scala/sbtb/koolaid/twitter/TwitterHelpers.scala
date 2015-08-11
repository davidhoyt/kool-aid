package sbtb.koolaid.twitter

import twitter4j.{Status, Paging, User}

import scala.collection.mutable
import scala.util.Try
import scala.collection.JavaConversions._

object TwitterHelpers {
    // Lookup user profiles in batches of 100
    def lookupUsersById(ids: Long*): Seq[User] = {
      val client = TwitterClient()
      val res = client.lookupUsers(ids:_*)
      res.toList
    }

    // Lookup user profiles in batches of 100
    def lookupUsersByScreenName(screenNames: String*): Seq[User] = {
      val client = TwitterClient()
      val res = client.lookupUsers(screenNames:_*)
      res.toSeq
    }

    // Fetch the IDs of a user's followers in batches of 10
    def getFollowers(screenNames: String*): Try[Map[String, Seq[Long]]] = {
      Try({
        val map = mutable.Map[String, Seq[Long]]()
        for (screenName <- screenNames) {
          val followerIds = mutable.Set[Long]()
          var cursor = -1L
          do {
            val client = TwitterClient()
            val res = client.friendsFollowers().getFollowersIDs(screenName, cursor, 10)
            res.getIDs.toList.foreach(x => followerIds.add(x))
            if (res.hasNext) {
              cursor = res.getNextCursor
            }
            else {
              cursor = -1 // Exit the loop
            }
          } while (cursor > 0)
          map += screenName -> followerIds.toSet.toSeq
        }
        map.toMap
      })
    }

    def getTweets(screenNames: String*): Try[Map[String, Seq[Status]]] = {
      Try({
        val map = mutable.Map[String, Seq[Status]]()
        val client = TwitterClient()
        for (screenName <- screenNames) {
          val timeline = client.getUserTimeline(screenName).toList.toSeq
          map += screenName -> timeline
        }
        map.toMap
      })
    }
}
