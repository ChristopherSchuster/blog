package io.ntdata

import java.io.File
import java.nio.file.{Paths, Files, Path}
import twitter4j.HashtagEntity
import twitter4j.Query
import twitter4j.Status
import twitter4j.TwitterFactory
import twitter4j.Twitter
import twitter4j.conf.ConfigurationBuilder


import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.io.SynchronousFileSink
import akka.stream.scaladsl._
import akka.util.ByteString
import org.clapper.argot._
import ArgotConverters._
import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success}


object RactiveTwitter extends App {


  def simpleSourceExample() {
    implicit val system = ActorSystem("Sys")
    import system.dispatcher

    implicit val materializer = ActorFlowMaterializer()

    val text =
      """I saw the best minds of my generation destroyed by madness,
      starving hysterical naked,
      dragging themselves through the negro streets at dawn looking for an angry fix,
      angel headed hipsters burning for the ancient heavenly connection to the starry dynamo in the machinery of night"""

    //build a source from an iterator
    Source(() => text.split(",").iterator)
      //transform every line
      .map(s => s.trim())
      .map(_.toUpperCase)
      //send every line to a sink
      .runForeach(s => println( s + "\n"))
      //shutdown when finish
      .onComplete(_ => system.shutdown())

  }


  def simpleGraphExample() {
    implicit val system = ActorSystem("Sys")
    import system.dispatcher

    implicit val materializer = ActorFlowMaterializer()

    val text =
      """I saw the best minds of my generation destroyed by madness,
      starving hysterical naked,
      dragging themselves through the negro streets at dawn looking for an angry fix,
      angel headed hipsters burning for the ancient heavenly connection to the starry dynamo in the machinery of night"""



    // take a ByteString as input and write to disk
    val fileSink = SynchronousFileSink(new File("/tmp/outscala.txt"), true)

    // take a String as input and print it to the console
    val consoleSink = Sink.foreach(println)

    //takes a string and convert it to ByteString
    val flowConverter = Flow[String].map(s => ByteString.fromString(s))

    val g = FlowGraph.closed(fileSink, consoleSink)((_,cons) => cons) { implicit builder =>

      (f, console) =>
      import FlowGraph.Implicits._

      //build a source from an iterator
      val in = Source(List(text))
        //transform every line
        .map(s => s.trim())
        .map(_.toUpperCase)


      //Broadcast that take 1 entry and send it to 2 outputs
      val bcast = builder.add(Broadcast[String](2))

      in ~> bcast
            bcast ~> flowConverter ~> f
            bcast ~> console
    }.run()

    g.onComplete {
      case Success(_) =>
        system.shutdown()
      case Failure(e) =>
        println(s"Failure: ${e.getMessage}")
        system.shutdown()
    }

  }

  def getClient(): Twitter = {
    val api_key = sys.env("REACTIVE_TWITTER_API_KEY")
    val api_secret = sys.env("REACTIVE_TWITTER_API_SECRET")
    val access_token = sys.env("REACTIVE_TWITTER_ACCESS_TOKEN")
    val access_token_secret = sys.env("REACTIVE_TWITTER_ACCESS_TOKEN_SECRET")
    val cb = new ConfigurationBuilder()
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey(api_key)
      .setOAuthConsumerSecret(api_secret)
      .setOAuthAccessToken(access_token)
      .setOAuthAccessTokenSecret(access_token_secret)
    val tf = new TwitterFactory(cb.build())
    tf.getInstance()
  }

  def reactiveTweets(dataDir: String, term: String) {
    implicit val system = ActorSystem("Sys")
    import system.dispatcher

    implicit val materializer = ActorFlowMaterializer()
    val client = getClient()
    val query = new Query(term);
    val tweets = Source( () => client.search(query).getTweets.asScala.iterator)

    val tweetPath = Paths.get(dataDir, "tweets.txt")
    val tweetSink = SynchronousFileSink(new File(tweetPath.toString), true)
    val authorPath = Paths.get(dataDir, "authors.txt")
    val authorSink = SynchronousFileSink(new File(authorPath.toString), true)
    val hashtagPath = Paths.get(dataDir, "hashtags.txt")
    val hashtagSink = SynchronousFileSink(new File(hashtagPath.toString), true)

    val tweetFlow = Flow[Status]

    val twitterGraph = FlowGraph.closed(tweets, tweetSink)( (tweets, tSink) => tSink) { implicit builder =>
      (in, tSink) =>
      import FlowGraph.Implicits._

    val DELIMITER = "<><><>"

      //Broadcast that take 1 entry and send it to 2 outputs
      val bcast = builder.add(Broadcast[Status](3))

      in ~> bcast
            //bcast ~> tweetToStrFlow ~> autSink
            bcast ~> tweetFlow
              .map(t => ByteString.fromString(
                t.getId + DELIMITER  + t.getText + DELIMITER + t.getSource + "\n")) ~> tSink

            bcast ~> tweetFlow
            .map(t => ByteString.fromString(
              t.getId + DELIMITER + t.getUser.getId + DELIMITER
              + DELIMITER + t.getUser.getScreenName + DELIMITER + t.getUser.getName + "\n")) ~> authorSink

            bcast ~> tweetFlow
              .filter(t => t.getHashtagEntities.length > 0 )
              .map(t => {
                var lineList = ArrayBuffer.empty[String]
                lineList.append(t.getId + DELIMITER)
                t.getHashtagEntities.foreach(h => lineList.append(h.getText + DELIMITER))
                lineList.append("\n")
                ByteString.fromString(lineList.mkString(""))
              }) ~> hashtagSink
    }.run()

    twitterGraph.onComplete {
      case Success(_) =>
        system.shutdown()
      case Failure(e) =>
        println(s"Failure: ${e.getMessage}")
        system.shutdown()
    }

  }


  def start(dataDir: String, term: String) = {
    reactiveTweets(dataDir=dataDir, term=term)
  }


  def calculate(args: Array[String]) {
    val parser = new ArgotParser(
      "ReactiveTwitter",
      preUsage=Some("Reactive Twitter: Version " + BuildInfo.version  ))

    val dataDir = parser.option[String](List("dataDir"), "s",
      "Directory where Reactive Twitter will write data")
    val term = parser.option[String](List("query"), "s", "")

    try {
      parser.parse(args)
      start(
        dataDir=dataDir.value.getOrElse(parser.usage),
        term=term.value.getOrElse(parser.usage)
      )
    } catch {
      case e: ArgotUsageException => println(e.message)
    }
   }

  calculate(args)

}
