package edu.knoldus

import akka.NotUsed
import com.example.hello.api.{Employee, HellolagomService}
import com.knoldus.{ExternalService, ExternalUserData}
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}


class ExternalServiceImpl(externalCall: HellolagomService)(implicit ec: ExecutionContext) extends ExternalService {

  val list = ListBuffer(ExternalUserData)

  override def postUser: ServiceCall[ExternalUserData, String] = ServiceCall {
    request =>
      val obj = Employee(request.id, request.name, request.age)
      val res = externalCall.postEmployee().invoke(obj)
      val res1 = Await.result(res, Duration.Inf)
      Future.successful("done")
  }

  override def getUser(id: Int): ServiceCall[NotUsed, ListBuffer[ExternalUserData]] = ServiceCall {
    request =>
      val externalResult = externalCall.getEmployees(id).invoke()
      val result = Await.result(externalResult,Duration.Inf)
      val retrievedResult = result.map(x => ExternalUserData(x.id, x.name, x.age))
      Future.successful(retrievedResult)
  }

}
