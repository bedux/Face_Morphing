package controllers


import java.io.{ File, FileOutputStream}

import play.api.libs.json._
import data.{ ImagePath}
import play.api.mvc._
import logic.imageProcessing.{CacheLandMarc, Tools, TuImage}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.Random

class Application extends Controller {

  CacheLandMarc
  CacheLandMarc.runCacheOverAllImages(new File("/Users/bedux/Desktop/TU/Tu1/public/img")).onComplete(_=>println("Done"))


  def index = Action {implicit request =>

    Ok(views.html.index("Your new application is ready."))
  }







  def uppImage = Action(parse.text(maxLength = 1024 * 1024)){
    request => {

      val img64: String = request.body
      val btDataFile: Array[Byte] = new sun.misc.BASE64Decoder().decodeBuffer(img64.replace("data:image/png;base64,", ""))
      val of: File = File.createTempFile(Random.alphanumeric.take(30).mkString , ".png")
      val osf: FileOutputStream = new FileOutputStream(of)
      osf.write(btDataFile)
      osf.flush()

      import data.ImageDataInpl.locationImagePath
      Ok(Json.toJson(ImagePath(of.getCanonicalPath)))
    }
  }



}