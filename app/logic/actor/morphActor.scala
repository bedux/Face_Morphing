package logic.actor
import akka.actor._
import data.ImageData
import logic.imageProcessing.{Tools, TuImage}
import play.api.libs.json.{JsArray, JsValue, Json}

import scala.collection.parallel.ParSeq

object MorphActorObj {
  def props(out: ActorRef) = Props(new MorphActor(out))
}

class MorphActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: String =>{
      val msgJson:JsValue = Json.parse(msg)
       sumImages( (msgJson \ "paths").get.as[Array[String]])

    }



  }

  def sumImages(images:Array[String]) ={

    val imageSeq:ParSeq[TuImage] = images.toSeq.map(x=>Tools.TuImage(x,out)).par
    val result:TuImage = imageSeq.reduce(_ + _)
    import data.ImageDataInpl.locationImageData
    out ! (Json.stringify(Json.toJson(ImageData(result.getByte(),true))))

  }
  override def postStop() = {

println("Cloase")
  }
}