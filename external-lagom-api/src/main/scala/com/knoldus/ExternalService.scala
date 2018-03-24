package com.knoldus
import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

import scala.collection.mutable.ListBuffer


trait ExternalService extends Service {
  def getUser(id: Int):ServiceCall[NotUsed,ListBuffer[ExternalUserData]]

  def postUser: ServiceCall[ExternalUserData, String]
  override final def descriptor: Descriptor = {
    import Service._
    named("external-lagom")
      .withCalls(
        restCall(Method.GET,"/api/retrieve/:id",getUser _),
        restCall(Method.POST, "/api/post", postUser _)
      ).withAutoAcl(true)
  }
}

case class ExternalUserData(id: Int, name: String, age :Int)

object ExternalUserData {
  implicit val format:Format[ExternalUserData] = Json.format[ExternalUserData]
}
