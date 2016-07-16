package controllers


import java.awt.image.{BufferedImage, DataBufferByte, DataBufferInt}
import java.io.{ByteArrayInputStream, File, FileOutputStream}

import akka.util.ByteString
import org.opencv.core._
import org.opencv.highgui.Highgui
import org.opencv.core.Core._
import org.opencv.core.CvType._
import org.opencv.core.Point._
import org.opencv.imgproc.Imgproc._
import org.opencv.highgui.Highgui._
import play.api._
import play.api.http.HttpEntity
import play.api.mvc._
import logic.imageProcessing.LandmarcFacePoint._
import logic.imageProcessing.{Tools, TuImage}

class Application extends Controller {

  def index = Action {

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
    Ok(img.getByte()).as("image/png")
  }

}