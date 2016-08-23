package data

import com.google.common.io.BaseEncoding
import play.api.Play
import play.api.libs.json.{Json, Writes}

import scala.collection.immutable.List
import scala.reflect.io.File

/**
  * Created by bedux on 20/07/16.
  */
sealed trait  FS {
    def name:String
  val globalPath:String
}

case class FSDirectory( override val name:String,val files:List[FS],val backImg:Option[String]) extends FS{
  val globalPath = name.substring(name.lastIndexOf("imageDataset"))

}

case class FSImage( override val name:String) extends FS{
  println(name)
  val globalPath = name.substring(name.lastIndexOf("imageDataset"))
  println(globalPath)
}


object FSImplicit {




  implicit val FSWriter: Writes[FS] =
    new Writes[FS] {
      def writes(fs: FS) =
        fs match {
          case x:FSImage => Json.obj(
            "name" -> x.name,
            "type" -> "Image")
          case x:FSDirectory => Json.obj(
            "images" -> Json.toJson(x.files),
            "name" -> x.name,
            "type" -> "Directory"
          )
        }

    }

}