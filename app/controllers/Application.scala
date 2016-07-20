package controllers


import java.awt.image.{BufferedImage, DataBufferByte, DataBufferInt}
import java.io.{ByteArrayInputStream, File, FileOutputStream}

import play.api.libs.json._
import data.{ImageData, ImagePath}
import play.api.mvc._
import logic.imageProcessing.{Tools, TuImage}

import scala.util.Random

class Application extends Controller {

  def index = Action {implicit request =>

  Ok(views.html.index("Your new application is ready."))
  }

  def getImage = Action{

    var img:TuImage =null

    for (file <- new File("/Users/bedux/Desktop/img/").listFiles) {
      if(file.getName.contains("jpg")){
        println(file.getAbsolutePath)
        if(img==null){
          img =  Tools.TuImage(file.getAbsolutePath)
        }else{
          img = img + Tools.TuImage(file.getAbsolutePath)

        }

      }

    }
    import data.ImageDataInpl.locationImageData
    val json  = Json.toJson(ImageData(img.getByte()))
    Ok(json).as("text/json")
  }





  def uppImage = Action(parse.text(maxLength = 1024 * 1024)){
    request => {
      println(request.cookies.toList)

      val img64: String = request.body
      val btDataFile: Array[Byte] = new sun.misc.BASE64Decoder().decodeBuffer(img64.replace("data:image/png;base64,", ""))
      val of: File = new File("photo/" + Random.alphanumeric.take(30).mkString + ".png")
      val osf: FileOutputStream = new FileOutputStream(of)
      osf.write(btDataFile)
      osf.flush()
      import data.ImageDataInpl.locationImagePath

      Ok(Json.toJson(ImagePath(of.getCanonicalPath)))
    }
  }



}