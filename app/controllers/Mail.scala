package controllers

import java.io.{File, FileOutputStream}
import javax.inject.Inject

import data.ImagePath
import logic.imageProcessing.TuImage
import org.apache.commons.mail.EmailAttachment
import play.api.libs.json.Json
import play.api.libs.mailer._
import play.api.mvc.{Action, Controller}

import scala.util.Random

/**
  * Created by bedux on 17/08/16.
  */
class Mail @Inject()(mailerClient: MailerClient) extends Controller {

  def sendEmail(mail:String,image:Array[Byte])= {
    val email = Email(
      "Simple email",
      "bedulm@usi.ch",
      Seq(mail),
      // adds attachment

     attachments = Seq(
        AttachmentData("attachment.png",image,"image/png", Some("Simple data"), Some(EmailAttachment.INLINE))
//        //        // adds inline attachment from byte array
//        //        AttachmentData("data.txt", "data".getBytes, "text/plain", Some("Simple data"), Some(EmailAttachment.INLINE)),
//        //        // adds cid attachment
//        //        AttachmentFile("image.jpg", new File("/some/path/image.jpg"), contentId = Some(cid))
      ),
      // sends text, HTML or both...
      bodyText = Some("Ciao!")
    )
    println(mailerClient.send(email))


  }


  def sendImageByMail = Action(parse.json(maxLength = 1024 * 1024)){
    request => {
      val json = (request.body)
      val img64: String =(json \ "image").get.as[String]
      val mail: String =(json \ "mail").get.as[String]

      println(img64,mail)
      val btDataFile: Array[Byte] = new sun.misc.BASE64Decoder().decodeBuffer(img64.replace("data:image/png;base64,", ""))
      sendEmail(mail,btDataFile)
      Ok("Done")
    }}


//  def sendImage() = Action {
//    println(mailerClient)
//    sendEmail()
//    Ok("Sent")
//
//  }
}
