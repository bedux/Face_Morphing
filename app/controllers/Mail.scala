package controllers

import org.apache.commons.mail.EmailAttachment

import play.api.mvc.{Action, Controller}

import play.api.libs.mailer._
import javax.inject.Inject
/**
  * Created by bedux on 17/08/16.
  */
class Mail @Inject() (mailerClient: MailerClient) extends Controller {



  def sendEmail(mail:String,image:Array[Byte])= {
    val email = Email(
      "Simple email",
      "bedulm@usi.ch",
      Seq(mail),
     attachments = Seq(
        AttachmentData("attachment.png",image,"image/png", Some("Simple data"), Some(EmailAttachment.INLINE))
      ),
      bodyText = Some("Ciao!")
    )
    mailerClient.send(email)


  }


  def sendImageByMail = Action(parse.json(maxLength = 1024 * 1024)){
    request => {
      val json = (request.body)
      val img64: String =(json \ "image").get.as[String]
      val mail: String =(json \ "mail").get.as[String]

      val btDataFile: Array[Byte] = new sun.misc.BASE64Decoder().decodeBuffer(img64.replace("data:image/png;base64,", ""))
      sendEmail(mail,btDataFile)
      Ok("Done")
    }}



}
