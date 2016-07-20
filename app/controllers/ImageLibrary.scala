package controllers

import java.io.File

import data.{FS, FSDirectory, FSImage}
import logic.imageProcessing.Tools
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import views.html.ContainerImage

import scala.collection.immutable._

/**
  * Created by bedux on 20/07/16.
  */
class ImageLibrary extends Controller {


  def getListOfFile(remain:List[File]):List[FS] = {
    if(remain.length==0)return List[FS]()

    val head:File = remain.head
    val tail:List[File] = remain.tail

    if (head.getName.contains("jpg") || head.getName.contains("png")|| head.getName.contains("jpeg")) {
      return getListOfFile(tail) :+ FSImage(head.getPath)
    }else if(head.isDirectory){

      val fileInside:List[FS] = getListOfFile(head.listFiles().toList)
      val directory:FSDirectory =  FSDirectory(head.getPath,fileInside)
      val listRemain:List[FS] =  getListOfFile(tail)

      return listRemain:+directory
    }

    getListOfFile(tail)
  }

  def getImage = Action {
    val files:List[FS] = getListOfFile( new File("./public/img").listFiles.toList)

    import data.FSImplicit._
    Ok(ContainerImage(files))
  }

}
