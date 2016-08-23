import java.io.File

import logic.imageProcessing.CacheLandMarc
import play._
import play.api.Play

import scala.concurrent.ExecutionContext.Implicits.global

class Global extends GlobalSettings {

  override def beforeStart(app: Application) {

    PlayNativeLibHack.load(app.getWrappedApplication.getFile("libs/libopencv_java2413.dylib").getPath)
    import play.api.Play.current
    CacheLandMarc.->.runCacheOverAllImages(Play.getFile("imageDataset")).onComplete(_=>println("Done"))

  }

}