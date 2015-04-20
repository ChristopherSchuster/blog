package com.notempo1320

import spray.httpx.SprayJsonSupport._
import spray.httpx.SprayJsonSupport
import spray.json._
import DefaultJsonProtocol._

case class User(var id: Option[Long], username: String, email: String)

object ApiJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  //jsonFormatX depends of number of parameters that the object receives
  implicit val userFormat = jsonFormat3(User)

}
