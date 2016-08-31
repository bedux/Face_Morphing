package logic.imageProcessing

import java.io.File
import java.nio.file.{Files, Path, Paths}

import com.google.inject.Inject
import data.LandmarkPointData
import play.api.{Application, Play}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future
import scala.io.{Codec, Source}
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by bedux on 25/07/16.
  */
import play.api.Play.current

class CacheLandMarc(){



  private val pathCacheDir:String =  Play.getFile("cachePoint").getAbsolutePath
  private val dirPath:Path =  Paths.get(pathCacheDir)


  if(!Files.exists(dirPath)){
    Files.createDirectory(dirPath)

    println("create ")
  }else{
    println("Exist ")

  }


 private  def valToJson(f:File):LandmarkPointData = {
    import LandmarkPointData._
    val c:JsValue =  Json.parse(Source.fromFile(f).getLines().fold("")((x,y)=>x+" "+y))
    println("Load images ",f.getName,hashName(f.getAbsolutePath),c.as[LandmarkPointData].fileName)
    c.as[LandmarkPointData]

  }

  println(pathCacheDir)
    println(File.separator,"separator")

  private  val dirAsFile:File = new File(pathCacheDir)

  private var listOfAvailableFile:Map[String,LandmarkPointData] = dirAsFile.listFiles()
                                                        .toList
                                                        .map(t=> {
                                                          val js= valToJson(t)
                                                          hashName(js.fileName) -> js
                                                        })(collection.breakOut)


   def runCacheOverAllImages(f:File): Future[Any] ={
    val seq:List[Future[Any]]= f.listFiles().toList.map(
      x =>{
        if(x.isDirectory){
            runCacheOverAllImages(x)

        }else if(x.getName.contains(".png")||x.getName.contains(".jpg")){
          Future {
            getPointOf(x.getPath)
          }
        }else{
          Future {}

        }
      }
    )
    Future.sequence(seq)
  }


  def hashName(string: String):String = {
    println(File.separator,"separator")
    if(string.lastIndexOf("public" )== -1) return string.replace(File.separator,"_").replace(":","")
    string.substring(string.lastIndexOf("public")).replace(File.separator,"_").replace(":","")
  }
  def getPointOf(fileName:String):LandmarkPointData = {
    val fileKey:String = hashName(fileName)
    println(fileKey,"fileKey")
    listOfAvailableFile get fileKey match {
      case x:Some[LandmarkPointData] =>
        {
          x.get
        }
      case _ => {

        val res = LandmarcFacePoint.getFaceKeyPoint(fileName)
        listOfAvailableFile = listOfAvailableFile + (fileKey -> res)
        import LandmarkPointData._
        implicit val codec = Codec.UTF8
        val fiel = new java.io.PrintWriter(pathCacheDir+File.separator+fileKey+".json")
        fiel.print( Json.stringify(Json.toJson(res)))
        fiel.close()
        res
      }

    }
  }

}

object CacheLandMarc{
  lazy val -> =  new CacheLandMarc()
//  def -> : CacheLandMarc = cacheLandMarc


}
