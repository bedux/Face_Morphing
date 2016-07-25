package data

import org.opencv.core.Point
import play.api.libs.json.{JsResult, _}

/**
  * Created by bedux on 25/07/16.
  */
case class LandmarkPointData(points:Array[Array[Array[Int]]],fileName:String) {
  def toListOfPoint():Array[Array[Point]] = {
    points.map(x => x.map(y => new Point(y(0),y(1))))
  }
}

object LandmarkPointData{
  implicit val landmarkPointData: Writes[LandmarkPointData] =
  new Writes[LandmarkPointData] {
  def writes(resident: LandmarkPointData) = Json.obj(
    "points" -> Json.toJson(resident.points),
    "fileName" -> Json.toJson(resident.fileName)
  )
}

  implicit val landmarkPointDataReader: Reads[LandmarkPointData] = new Reads[LandmarkPointData] {
    override def reads(json: JsValue): JsResult[LandmarkPointData] = {
       val js =  LandmarkPointData((json \ "points").as[Array[Array[Array[Int]]]],(json \ "fileName").as[String] )
       JsSuccess(js)
    }
  }

}