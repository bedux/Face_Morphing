package data
import com.google.common.io.BaseEncoding
import play.api.libs.json._
import play.api.libs.functional.syntax._
/**
  * Created by bedux on 18/07/16.
  */
case class ImageData(image:Array[Byte],isLast: Boolean = false){

}

case class ImagePath(path:String){}

object ImageDataInpl {


  implicit val locationImageData: Writes[ImageData] =
  new Writes[ImageData] {
    def writes(resident: ImageData) = Json.obj(
      "image" -> BaseEncoding.base64().encode(resident.image),
      "isLast" -> resident.isLast

    )
  }
  implicit val locationImagePath: Writes[ImagePath] =
    new Writes[ImagePath] {
      def writes(resident: ImagePath) = Json.obj(
        "path" -> resident.path

      )
    }



}