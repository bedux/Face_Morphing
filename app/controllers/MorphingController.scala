package controllers

import javax.inject.Inject

import akka.actor.ActorSystem
import akka.stream.Materializer
import logic.actor.MorphActorObj
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket

/**
  * Created by bedux on 18/07/16.
  */
class MorphingController @Inject() (implicit system: ActorSystem, materializer: Materializer) {

  def socket = WebSocket.accept[String, String] { request =>
    println("New User")
    ActorFlow.actorRef(out => MorphActorObj.props(out))
  }
}