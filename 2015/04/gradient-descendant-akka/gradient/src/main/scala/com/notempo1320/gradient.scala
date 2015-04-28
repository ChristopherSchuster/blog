package com.notempo1320

import akka.actor._
import akka.event.Logging
import akka.routing.RoundRobinRouter

import org.clapper.argot._
import ArgotConverters._

import scala.concurrent.duration._
import scala.collection.mutable.ArrayBuffer
import scala.io.{Source, BufferedSource}

import java.nio.file.{Paths, Files}
import java.io._

case class Point(x: Double, y: Double)
case class Intercept(theta0: Double, theta1: Double, cost: Double)

object InterceptApp extends App {

  sealed trait InterceptMessage

  case object ComputeIntercept extends InterceptMessage

  case class CalculateCost(intercept: Intercept,
        numberList: List[Point]) extends InterceptMessage

  case class CostResult(intercept: Intercept) extends InterceptMessage

  case class CalculateGradient(
    intercept: Intercept, numberList: List[Point], learningRate: Double,
    nrOfIterations: Int) extends InterceptMessage

  case class GradientResult(intercept: Intercept) extends InterceptMessage

  case class InterceptResult(interceptResult: Intercept, duration: Duration)


  class Worker extends Actor {
      val log = Logging(context.system, this)

      //y = mx + b
      //hθ(x)= θ0 + θ1x
      def calculateCost(intercept: Intercept, numberList: List[Point]): Intercept = {
        // Calculte error for a particular set of θ0  and θ1
        var totalCost: Double = 0.0
        for (i <- 0 to numberList.size -1) {
          totalCost += math.pow( (numberList{i}.y - (intercept.theta1 *  numberList{i}.x + intercept.theta0)), 2)
        }
        val result = Intercept(intercept.theta0, intercept.theta1, totalCost / numberList.size)
        log.info("CalculateCost for intercept {} {} = {}", result.theta0, result.theta1, result.cost)
        return result
      }

      def stepGradient(
        intercept: Intercept, numberList: List[Point], learningRate: Double): Intercept = {

        var theta0Gradient: Double = 0.0
        var theta1Gradient: Double = 0.0
        var x: Double = 0.0
        var y: Double = 0.0
        val N = numberList.size.toDouble

        for (i <- 0 to numberList.size - 1) {
          x = numberList{i}.x
          y = numberList{i}.y
          //b gradient
          theta0Gradient += -(2/N) * (y - ((intercept.theta1 * x) + intercept.theta0))
          //m gradient
          theta1Gradient += -(2/N) * x * (y - ((intercept.theta1 * x) + intercept.theta0))
        }
        return Intercept(
          intercept.theta0 - (learningRate * theta0Gradient),
          intercept.theta1 - (learningRate * theta1Gradient), 0.0)
      }

      def calculateGradient(
        intercept: Intercept, numberList: List[Point],
        learningRate: Double, nrOfIterations: Int): Intercept = {

        log.info("Calculating gradient with starting values {} {}", intercept.theta0, intercept.theta1)
        var result: Intercept = intercept
        for (i <- 0 to nrOfIterations) {
          result = stepGradient(result, numberList, learningRate)
        }
        log.info("Gradient result {} {}", result.theta0, result.theta1)
        return result;
      }

      def receive = {
        // ! means “fire-and-forget”. Also known as tell.
        case CalculateGradient(intercept: Intercept, numberList: List[Point],
          learningRate: Double, nrOfIterations: Int) =>
          sender ! GradientResult(calculateGradient(intercept, numberList, learningRate, nrOfIterations))

        case CalculateCost(intercept: Intercept, numberList: List[Point]) =>
          sender ! CostResult(calculateCost(intercept, numberList))
      }

  }


  class Master(nrOfWorkers: Int, nrOfIterations: Int,
      learningRate: Double, numberList: List[Point], listener: ActorRef)
    extends Actor {
    val log = Logging(context.system, this)
    var initialIntercept: Intercept = _
    var partialResults = ArrayBuffer.empty[Intercept]
    // Every worker will perform nrOfIterations / nrOfWorkers
    var nrOfCalculations: Int = nrOfIterations / nrOfWorkers
    var nrOfResults: Int = 0
    var theta1: Double = 0.0
    val start: Long = System.currentTimeMillis

    override def preStart() = {
      log.info("Starting master")
      super.preStart()
    }

    // create a round-robin router to make it easier to spread out the work between the workers
    val workerRouter = context.actorOf(
      Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")

    def generate_random_intercept_point(): Double = {
      // Semi random generation based on number of x,y points
      return (
        numberList.size.toDouble /
        ((util.Random.nextInt(numberList.size) + 1) * (numberList.size + util.Random.nextInt(numberList.size))))
    }


    def receive = {
      case ComputeIntercept =>
        for(i <- 1 to nrOfWorkers + 1) {
          //get random initial intercept
          initialIntercept = Intercept(
            generate_random_intercept_point(), generate_random_intercept_point(), 0.0)

          if (i == 1) {
            // First execution loop computes initial value of cost function
            log.info("CalculateCost call for initial intercept theta0:{} theta1:{}",
              initialIntercept.theta0, initialIntercept.theta1)
            workerRouter ! CalculateCost(initialIntercept, numberList)
          }

          workerRouter ! CalculateGradient(
            initialIntercept, numberList, learningRate, nrOfIterations)
        }

      case GradientResult(intercept) =>
        // calculate cost for every result
        log.info("CalculateCost call for intercept theta0:{} theta1:{}", intercept.theta0, intercept.theta1)
        workerRouter ! CalculateCost(intercept, numberList)

      case CostResult(result) =>
        log.info("CostResult ...")
        partialResults.append(result)
        nrOfResults += 1
        if (nrOfResults == nrOfWorkers + 1) {
          // Find the point with minimum error
          var error: Double = partialResults{0}.cost
          var position: Int = 0
          for (i <-0 to partialResults.size -1) {
            log.info("partial cost {} i {}", partialResults{i}.cost, i)
            if (partialResults{i}.cost < error) {
              error = partialResults{i}.cost
              position = i
            }
          }

          // Send the result to the listener
          listener ! InterceptResult(
            partialResults{position}, duration = (System.currentTimeMillis - start).millis)

          // Stops this actor and all its supervised children
          context.stop(self)
        }
    }
  }

  class Listener extends Actor {
    val log = Logging(context.system, this)

    override def preStart() = {
      log.info("Starting listener")
      super.preStart()
    }

    def receive = {
      case InterceptResult(intercept: Intercept, duration: Duration) =>
        log.info("total duration {}", duration)
        log.info(
          "GradientDescent final result theta0: {} theta1: {} cost: {}",
          intercept.theta0, intercept.theta1, intercept.cost)
        context.system.shutdown()
    }
  }


  def calculate(nrOfWorkers: Int, nrOfIterations: Int, learningRate: Double, fileName: String) {
    val numberList = Source.fromFile(fileName).getLines().map(
      s => s.split(",")).map(x => Point( x{0}.toDouble, x{1}.toDouble)).toList

    // Create an Akka system
    val system = ActorSystem("GradientDescentSystem")

    // create the result listener, which will log the result and will shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")

   // create the master
    val master = system.actorOf(Props(new Master(
      nrOfWorkers, nrOfIterations, learningRate, numberList, listener)),
      name = "master")

    // start the calculation
    master ! ComputeIntercept

  }

   // Main program
  override def main(args: Array[String]) {
    val parser = new ArgotParser("gradient", preUsage=Some("Version 1.0"))
    val fileName = parser.option[String](List("f", "filename"), "filename", "filename")
    val nrOfWorkers = parser.option[Int](List("w", "workers"), "workers", "workers")
    val nrOfIterations = parser.option[Int](List("i", "iterations"), "itetarions", "iterations")
    val learningRate = parser.option[Double](List("r", "rate"), "rate", "rate")
    parser.parse(args)
    calculate(
      nrOfWorkers=nrOfWorkers.value.get, nrOfIterations=nrOfIterations.value.get,
      learningRate=learningRate.value.get, fileName=fileName.value.get)
  }
}
