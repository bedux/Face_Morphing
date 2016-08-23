package controllers


import java.io.{File, FileOutputStream}
import javax.inject.Inject

import play.api.libs.json._
import data.ImagePath
import play.api.mvc._
import logic.imageProcessing.{CacheLandMarc, Tools, TuImage}
import play.api.Play

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import play.api.Play.current

/**
  * Created by bedux on 23/08/16.
  */
class StaticFile  @Inject()  extends Controller {

  def html(file: String) = Action {
    println("imageDataset"+File.separator+file)
    val f:File = Play.getFile("imageDataset"+File.separator+file)

    if (f.exists())
      Ok.sendFile(f)
    else
      NotFound
  }

}
