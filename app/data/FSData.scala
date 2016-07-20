package data

import com.google.common.io.BaseEncoding
import play.api.libs.json.{Json, Writes}

import scala.collection.immutable.List

/**
  * Created by bedux on 20/07/16.
  */
sealed trait  FS {
    def name:String
}

case class FSDirectory( override val name:String,val files:List[FS]) extends FS{
}

case class FSImage( override val name:String) extends FS{

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