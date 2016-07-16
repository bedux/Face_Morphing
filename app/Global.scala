import play._

class Global extends GlobalSettings {

  override def beforeStart(app: Application) {
    PlayNativeLibHack.load(app.getFile("lib/libopencv_java2413.dylib").getPath)

  }

}