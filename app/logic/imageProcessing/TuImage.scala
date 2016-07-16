package logic.imageProcessing

import java.util

import org.opencv.core._
import org.opencv.highgui.Highgui._
import org.opencv.imgproc.Imgproc._

import logic.imageProcessing.LandmarcFacePoint._
import org.opencv.core.Core._
import org.opencv.core.CvType._

import org.opencv.highgui.Highgui

/**
  * Created by bedux on 15/07/16.
  */

/**
  * A TuImage is compose by the image itself of type @See Mat and a Array of point that represent the key point of the face.
  * It can be construct using a path to an image or by pass an image and the points array
  */
case class TuImage(image:Mat,points:Array[Array[Point]]) {

  import scala.collection.JavaConversions._

  /*
    Create a debug image
   */

  val debugImage:Mat = image.clone()

  var i = 0

  //start drawing all found points
  points.foreach(_.foreach(p=>
                                {
                                  circle(debugImage,p,3,new Scalar(255,255,0))
                                  putText(debugImage,i.toString,p,2,0.4,new Scalar(255,0,255))
                                  i+=1
                                }))


  for(i<-0 until Tools.triangleList.length by 3){
    if(Tools.triangleList(i)<points(0).length && Tools.triangleList(i+1)<points(0).length && Tools.triangleList(i+2)<points(0).length) {
        val c: List[MatOfPoint] = List(
          new MatOfPoint(points(0)(Tools.triangleList(i)),points(0)(Tools.triangleList(i + 1)),points(0)(Tools.triangleList(i + 2))))

      polylines(debugImage,c,true, new Scalar(0,0,255))
   //  fillPoly(debugImage,c,new Scalar(255,255,255,2))

    }


  }



  def +(im1:TuImage):TuImage = {

    val alpha = 0.5
    val sizeW = if  (image.size().width >im1.image.size().width)
                                                        image.size().width
                 else im1.image.size().width
    val sizeH = if  (image.size().height >im1.image.size().height)
                        image.size().height
                else im1.image.size().height

    val imgMorph:Mat = Mat.zeros(new Size(sizeW,sizeH), CV_32FC3)

    val pt:Array[Point]  = weightedAverage(points(0),im1.points(0),alpha)

    image.convertTo(image, CV_32F)
    im1.image.convertTo(im1.image, CV_32F)
        for(i<-0 until Tools.triangleList.length by 3) {

          val t1:MatOfPoint  =new MatOfPoint( points(0)(Tools.triangleList(i)),
                                              points(0)(Tools.triangleList(i+1)),
                                              points(0)(Tools.triangleList(i+2)))

          val t2:MatOfPoint  =new MatOfPoint( im1.points(0)(Tools.triangleList(i)),
                                              im1.points(0)(Tools.triangleList(i+1)),
                                              im1.points(0)(Tools.triangleList(i+2)))

          val t:MatOfPoint  =new MatOfPoint(pt(Tools.triangleList(i)),
                                            pt(Tools.triangleList(i+1)),
                                            pt(Tools.triangleList(i+2)))

          morphTriangle(image, im1.image, imgMorph, t1, t2, t, alpha);

        }

      val result:Array[Array[Point]] = new Array[Array[Point]](1)
      result(0) = pt
      new TuImage(imgMorph,result)
    }


  /**
    * Construct a TuImage usin another TuImage
    *
    * @param tuImg
    */
  def this(tuImg:TuImage) {
    this(tuImg.image,tuImg.points)
  }

  /**
    * Get the bytes array of the image
    *
    * @param format the format of the image, default is png
    * @return the bytes array
    */
  def getByte(format: String = ".png"):Array[Byte] = Tools.getByte(image)(format)

  /**
    * Get the debug image bytes
    *
    * @param format the format of the image, default is png
    * @return the bytes array
    */
  def getByteDebugImage(format: String = ".png"):Array[Byte] = Tools.getByte(debugImage)(format)


  //compute weighted average point coordinates
  def  weightedAverage(p1:Array[Point],p2:Array[Point],alpha:Double):Array[Point] ={
    val result = new Array[Point](p1.length)
    for(i<-0 until p1.length){
      val x = (1.0 - alpha) * p1(i).x + alpha * p2(i).x
      val y =  ( 1.0 - alpha ) * p1(i).y + alpha * p2(i).y
      result(i)= new Point(x,y)
    }
    result
  }


  // Apply affine transform calculated using srcTri and dstTri to src
  def applyAffineTransform( warpImage:Mat, src:Mat, srcTri:MatOfPoint2f, dstTri:MatOfPoint2f)=
  {

    // Given a pair of triangles, find the affine transform.
    val warpMat:Mat = getAffineTransform( srcTri, dstTri )

    // Apply the Affine Transform just found to the src image
    warpAffine( src, warpImage, warpMat, warpImage.size())
  }


  def morphTriangle(img1:Mat,img2:Mat,img:Mat,t1: MatOfPoint,t2: MatOfPoint,t: MatOfPoint ,alpha:Double): Unit ={
    val r:Rect = boundingRect(t)
    val r1:Rect = boundingRect(t1)
    val r2:Rect = boundingRect(t2)

    var t1Rect, t2Rect, tRect , tRectInt:List[Point] = List[Point]()

    // Offset points by left top corner of the respective rectangles
    for(i<- 0 until 3){
      tRect = tRect :+ (new Point(t.toArray()(i).x - r.x, t.toArray()(i).y -  r.y))
      tRectInt =  tRectInt :+ new Point(t.toArray()(i).x - r.x, t.toArray()(i).y - r.y)
      t1Rect=t1Rect:+new Point( t1.toArray()(i).x - r1.x, t1.toArray()(i).y -  r1.y)
      t2Rect=t2Rect:+new Point( t2.toArray()(i).x - r2.x, t2.toArray()(i).y - r2.y)
    }

    val t1RecMat:MatOfPoint2f = new MatOfPoint2f()
    t1RecMat.fromList(t1Rect)

    val t2RecMat:MatOfPoint2f = new MatOfPoint2f()
    t2RecMat.fromList(t2Rect)

    val tRectIntMat:MatOfPoint = new MatOfPoint()
    tRectIntMat.fromList(tRectInt)



    val mask:Mat = Mat.zeros(r.height, r.width, CV_8UC1)
    fillConvexPoly(mask, tRectIntMat, new Scalar(1.0, 1.0, 1.0), 8, 0);

    val tRectIntMat2df:MatOfPoint2f = new MatOfPoint2f()
    tRectIntMat2df.fromList(tRectInt)



    // Apply warpImage to small rectangular patches
    val img1Rect, img2Rect:Mat = new Mat()

    img1.submat(r1).copyTo(img1Rect)
    img2.submat(r2).copyTo(img2Rect)




    val warpImage1:Mat = Mat.zeros(r.height, r.width, img1Rect.`type`())
    val warpImage2:Mat = Mat.zeros(r.height, r.width, img2Rect.`type`())

    applyAffineTransform(warpImage1, img1Rect, t1RecMat, tRectIntMat2df);
    applyAffineTransform(warpImage2, img2Rect, t2RecMat, tRectIntMat2df);






    val p1:Mat = new Mat()
     Core.multiply(warpImage1, new Scalar(1.0 - alpha,1.0 - alpha,1.0 - alpha),p1)




    //
    val p2:Mat = new Mat()
    Core.multiply(warpImage2, new Scalar(alpha,alpha,alpha),p2)


    //
    val imgRect:Mat = new Mat()
    Core.add(p1,p2,imgRect)


    imgRect.copyTo(img.submat(r),mask)



  }



}


object Tools {
  /**
    * Get the bytes array of the image
    *
    * @param image  the image to be transform
    * @param format the format of the image, default is png
    * @return the bytes array
    */
  def getByte(image:Mat)(format: String = ".png"):Array[Byte] = {
    val matOfByte:MatOfByte = new MatOfByte()
    Highgui.imencode(format, image, matOfByte)
    matOfByte.toArray
  }


  def getKeyPoint(src:String,mat:Mat): Array[Array[Point]] = {
    val resPoint:Array[Array[Point]] = getFaceKeyPoint(src)
    val plus8:Array[Array[Point]]  = new Array(resPoint.length)

    plus8(0) = new Array[Point](resPoint(0).length+16)
    for(i<-0 until resPoint(0).length){
      plus8(0)(i) =resPoint(0)(i)
    }

    val offset =resPoint(0).length
    plus8(0)(68) = new Point(0,0)
    plus8(0)(70) =new Point(mat.width()/2,0)
    plus8(0)(72) =new Point(mat.width()-1,0)
    plus8(0)(74) =new Point(mat.width()-1,mat.height()/2)
    plus8(0)(76) =new Point(mat.width()-1,mat.height()-1)
    plus8(0)(78) =new Point(mat.width()/2,mat.height()-1)
    plus8(0)(80) =new Point(0,mat.height()-1)
    plus8(0)(82) =new Point(0,mat.height()/2)

    plus8(0)(69) = new Point(mat.width()/4,0)
    plus8(0)(71) =new Point(mat.width()/2 + mat.width()/4,0)
    plus8(0)(73) =new Point(mat.width()-1,mat.height()/4)
    plus8(0)(75) =new Point(mat.width()-1,mat.height()/2+mat.height()/4)
    plus8(0)(77) =new Point(mat.width()/2+mat.height()/4,mat.height()-1)
    plus8(0)(79) =new Point(mat.height()/4,mat.height()-1)
    plus8(0)(81) =new Point(0,mat.height()/4+mat.height()/2)
    plus8(0)(83) =new Point(0,mat.height()/4)

    plus8
  }


  /**
    * Construct a TuImage usin a file path
    *
    * @param path path to the image
    *
    */
  def TuImage(path:String):TuImage ={
    val m : Mat = imread(path)
    val r:Array[Array[Point]]= Tools.getKeyPoint(path,m)
    //load the image and get the point
    new TuImage(m, r)
  }


  val triangleList:Array[Int]=Array(
  38, 40, 37,
  35, 30, 29,
  38, 37, 20,
  18, 37, 36,
  33, 32, 30,
  54, 64, 53,
  30, 32, 31,
  59, 48, 60,
  40, 31, 41,
  36, 37, 41,
  21, 39, 38,
  35, 34, 30,
  51, 33, 52,
  40, 29, 31,
  57, 58, 66,
  36, 17, 18,
  35, 52, 34,
  65, 66, 62,
  58, 67, 66,
  53, 63, 52,
  61, 67, 49,
  53, 65, 63,
  56, 66, 65,
  55, 10, 9,
  64, 54, 55,
  43, 42, 22,
  46, 54, 35,
  1 ,0 ,36,
  19, 37, 18,
  1 ,36 ,41,
  0 ,17 ,36,
  37, 19, 20,
  21, 38, 20,
  39, 40, 38,
  28, 29, 39,
  41, 31, 2,
  59, 67, 58,
  29, 30, 31,
  34, 33, 30,
  21, 27, 39,
  28, 42, 29,
  52, 33, 34,
  62, 66, 67,
  48, 4 , 3,
  41, 2 , 1,
    83, 2 , 1,

    31, 3 , 2,
  37, 40, 41,
  39, 29, 40,
  57, 7 , 58,
  31, 48, 3,
  5 , 4 , 48,
  32, 49, 31,
  60, 49, 59,
  59, 5 , 48,
  7 , 6 , 58,
  31, 49, 48,
  49, 67, 59,
  6 , 59, 58,
  6 , 5 , 59,
  8 , 7 , 57,
  48, 49, 60,
  32, 33, 50,
  49, 50, 61,
  49, 32, 50,
  51, 61, 50,
  62, 67, 61,
  33, 51, 50,
  63, 65, 62,
  51, 62, 61,
  51, 52, 63,
  51, 63, 62,
  52, 35, 53,
  47, 46, 35,
  54, 10, 55,
  56, 57, 66,
  56, 8 , 57,
  53, 55, 65,
  9 , 8 , 56,
  56, 55, 9,
  65, 55, 56,
  10, 54, 11,
  53, 64, 55,
  53, 35, 54,
  12, 54, 13,
  12, 11, 54,
  54, 14, 13,
  35, 42, 47,
  45, 16, 15,
  22, 42, 27,
  42, 35, 29,
  27, 42, 28,
  44, 25, 45,
  44, 47, 43,
  46, 14, 54,
  45, 46, 44,
  45, 14, 46,
  39, 27, 28,
  22, 23, 43,
  21, 22, 27,
  21, 20, 23,
  43, 23, 24,
  22, 21, 23,
  44, 43, 24,
  47, 42, 43,
  25, 44, 24,
  46, 47, 44,
  16, 45, 26,
  15, 14, 45,
  45, 25, 26,
    20,70,23,
    19,70,20,
    19,70,69,
    19,18,69,
    18,69,17,
    17,69,68,
    17,68,83,
    83,17,0,
    83,1,0,
    83,2,82,
    82,2,3,
    82,3,4,
    82,4,5,
    81,82,5,
    81,5,6,
    81,6,7,
    81,7,8,
    81,8,80,
    80,8,79,
    79,8,78,
    78,8,9,
    78,9,10,
    78,10,11,
    78,11,77,
    11,12,77,
    12,77,76,
    76,12,13,
    75,76,13,
    13,14,75,
    14,15,75,
    15,75,74,
    15,16,74,
    16,26,73,
    74,73,16,
    26,25,73,
    73,72,25,
    24,25,70,
    71,70,25,
    23,24,70,
    25,71,72


























  )
}