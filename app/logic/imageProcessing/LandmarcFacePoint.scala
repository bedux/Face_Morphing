package logic.imageProcessing
import data.LandmarkPointData

import play.api.libs.json.{JsValue, Json}


/**
  * This i a wrapp that get the list of point. It run a command line command.
  */
object LandmarcFacePoint {
  import scala.sys.process._
  val scriptPath:String = "/Users/bedux/Desktop/TU/Tu1/lib/dtest"
  val dataSetPath:String = "/Users/bedux/Desktop/shape_predictor_68_face_landmarks.dat"

  /**
    *
    * @param fileName the file that should  be analyzed
    * @return the array of faces with the point for each face
    */
  def getFaceKeyPoint(fileName:String):LandmarkPointData = {
    val res: String = (scriptPath + " "+ dataSetPath +"  " + fileName).!!
    val json:JsValue = Json.parse(res)
    val result = json.as[Array[Array[Array[Int]]]]
    LandmarkPointData(result,fileName)
  }




}
