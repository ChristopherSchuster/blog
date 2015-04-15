package com.notempo1320.demo

import akka.actor._
import akka.routing.RoundRobinRouter
import scala.concurrent.duration._

import java.nio.file.{Paths, Files}
import java.io._

import scala.collection.mutable.ArrayBuffer
import scala.io.{Source, BufferedSource}


object Indexer extends App {

  calculate(nrOfWorkers = 4)


  sealed trait WordIndexMessage

  case object Calculate extends WordIndexMessage

  case class Work(line: String) extends WordIndexMessage

  case class Result(words: Array[String]) extends WordIndexMessage

  case class Index(words: ArrayBuffer[String], duration: Duration)

  class Worker extends Actor {

      def splitLine(line: String): Array[String] = {
        line.split(",");
      }

      // count words
      def receive = {
        // ! means “fire-and-forget”. Also known as tell.
        case Work(line) => sender ! Result(splitLine(line)) // perform the work
      }

  }


  class Master(nrOfWorkers: Int, itLines: Iterator[String], listener: ActorRef)
    extends Actor {
    val messagesPercall = 100
    var words = ArrayBuffer.empty[String]
    val start: Long = System.currentTimeMillis
    var nrOfResults: Int = _
    var nrOfLines: Int = 0

    // create a round-robin router to make it easier to spread out the work between the workers
    val workerRouter = context.actorOf(
      Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")


    def receive = {
      case Calculate =>
        //sent to the workers all lines in groups of nrOfLines items
        while (itLines.nonEmpty) {
          for (line <- itLines.take(messagesPercall)) {
            nrOfLines += 1
            workerRouter ! Work(line)
          }
        }

      case Result(value) =>
        words.appendAll(value)
        nrOfResults += 1
        if (nrOfResults == nrOfLines) {
          // Send the result to the listener
          listener !Index(words, duration = (System.currentTimeMillis - start).millis)
          // Stops this actor and all its supervised children
          context.stop(self)
        }
    }
  }



  class Listener extends Actor {
    val rootDir = "/tmp/wordcount"
    var nrOfWords: Int = 1;
    var src: Source = _;

    def indexWord(word: String) {
      val filePath = Paths.get(rootDir, word)
      def computeValue() {
        def wordExist(): Boolean = {
          return Files.exists(filePath)
        }

        //If word file does not exists, create it
        if (!wordExist()) {
          Files.createFile(filePath)
          src = Source.fromFile(filePath.toString())
        } else {
          src = Source.fromFile(filePath.toString())
          nrOfWords = src.getLines().toList{0}.toInt
        }

        src.close()
      }

      def updateValue() {
        val file = new File(filePath.toString())
        val bw = new BufferedWriter(new FileWriter(file))
        bw.write(nrOfWords.toString())
        bw.close()
      }

      computeValue()
      updateValue()
    }

    def receive = {
      case Index(words, duration) =>
        words.foreach(indexWord)
        println("\nCalculation time: %s".format(duration))
        context.system.shutdown()
    }
  }

  def calculate(nrOfWorkers: Int) {

    //Open file with words
    val wordLines = Source.fromInputStream(getClass.getResourceAsStream("/words.txt")).getLines()

    // Create an Akka system
    val system = ActorSystem("WordCountSystem")

    // create the result listener, which will print the result and will shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")

    // create the master
    val master = system.actorOf(Props(new Master(
      nrOfWorkers, wordLines, listener)),
      name = "master")

    // start the calculation
    master ! Calculate
  }

}
