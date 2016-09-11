import java.io.File

import logic.imageProcessing.CacheLandMarc
import play._
import play.api.Play
import logic.imageProcessing._
import scala.concurrent.ExecutionContext.Implicits.global

class Global extends GlobalSettings {

  override def beforeStart(app: Application) {


    val s = app.getWrappedApplication.getFile("libs\\shape_predictor_68_face_landmarks.dat").getAbsolutePath
    val c= new DLIB();
    c.Java_DLIB_init(s);


    PlayNativeLibHack.load(app.getWrappedApplication.getFile("libs/win/x64/opencv_java2413.dll").getPath)
    import play.api.Play.current
    CacheLandMarc.->.runCacheOverAllImages(Play.getFile("imageDataset")).onComplete(_=>println("Done"))
    LandmarcFacePoint.func = c.Java_DLIB_getImage
  }

}