package edu.knoldus



import com.example.hello.api.HellolagomService
import com.knoldus.ExternalService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class ExternalServiceLoader extends LagomApplicationLoader{
  override def load(context: LagomApplicationContext): LagomApplication =
    new UserApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new UserApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[ExternalService])

}

abstract class UserApplication(context: LagomApplicationContext)
  extends LagomApplication(context) with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[ExternalService](wire[ExternalServiceImpl])
   lazy val externalService = serviceClient.implement[HellolagomService]
}
