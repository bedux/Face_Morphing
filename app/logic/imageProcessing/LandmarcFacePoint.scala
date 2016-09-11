package logic.imageProcessing
import data.LandmarkPointData
import play.api.Play
import play.api.libs.json.{JsValue, Json}
import play.api.Play.current

import scala.reflect.io.File

/**
  * This i a wrapp that get the list of point. It run a command line command.
  */
object  LandmarcFacePoint {
  import scala.sys.process._

  def default(string: String):String = string;
  var func: String => String = default

  val scriptPath:String =Play.getFile("libs/win/dtest.exe").getAbsolutePath
  Play.getFile("libs"+File.separator+"dtest").setExecutable(true)
  val dataSetPath:String = Play.getFile("libs"+File.separator+"shape_predictor_68_face_landmarks.dat").getAbsolutePath

  /**
    *
    * @param fileName the file that should  be analyzed
    * @return the array of faces with the point for each face
    */
  def getFaceKeyPoint(fileName:String):LandmarkPointData = {
    val name = {
      if(fileName.indexOf("imageDataset")==0){
        println(fileName,"quii")
          Play.getFile(fileName).getAbsolutePath
      }else{
        fileName
      }

    }
    println(scriptPath + " "+ dataSetPath +"  " + name)





    val res: String = func(name)
    println(res)

    val json:JsValue = Json.parse(res)
    val result = json.as[Array[Array[Array[Int]]]]
    LandmarkPointData(result,name)
  }




}
