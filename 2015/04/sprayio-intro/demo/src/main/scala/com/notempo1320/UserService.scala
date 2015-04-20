package com.notempo1320

import scala.collection.mutable.ListBuffer

import akka.actor.Actor
import spray.routing._
import spray.http._
import spray.json._
import MediaTypes._

import com.notempo1320.ApiJsonProtocol._


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class UserActor extends Actor with UserService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(userRoute)
}


// this trait defines our service behavior independently from the service actor
trait UserService extends HttpService {
  var userList = new ListBuffer[User]()

  val userRoute =
    path("user") {
      get {
        respondWithMediaType(`application/json`) {

          userList.append(User(Option(util.Random.nextInt(10000).toLong), "user1", "email1"))
          userList.append(User(Option(util.Random.nextInt(10000).toLong), "user2", "email2"))
          complete(userList.toList.toJson.compactPrint)
        }
      } ~
      post {
        entity(as[User]) { user =>
          val user2 = User(Option(util.Random.nextInt(10000).toLong), user.username, user.email)
          userList += user2
          respondWithMediaType(`application/json`) {
            complete(StatusCodes.Created, user2.toJson.compactPrint)
          }
        }
      }
    }
}
