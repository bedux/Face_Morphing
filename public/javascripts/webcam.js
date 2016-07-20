/**
 * Created by bedux on 16/07/16.
 */

//video status
var stop = false;

var sessionUpdate = [];

//This function  capture and render an image on the elementName element of type Video
function openVideo(video) {

    navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia || navigator.oGetUserMedia;

    if (navigator.getUserMedia) {
        navigator.getUserMedia({video: true}, handleVideo, videoError);
    }

    function handleVideo(stream) {
        video.src = window.URL.createObjectURL(stream);
    }

    function videoError(e) {
        alert(e)
    }
}



//Update the canvas
function timerCallback(video,canvas,emotion) {
    if(!stop) computeFrame(video,canvas,emotion);


    setTimeout(function () {
        timerCallback(video,canvas,emotion);
    }, 0);
}


//compute the frame and draw in a canvas
function computeFrame (video,canvas,emotion){

    canvas.getContext('2d').scale(-1,1);
    canvas.getContext('2d').drawImage(video, 0, 0,canvas.width*-1 , canvas.height);


}

//It upload the current frame on the server and store the path on the array  array
function takeAndPut(array) {
    stop = !stop;
    data = canvas.toDataURL();
    postImage(data,function(respData){
        array.push(respData.path)
    })
}



function anotherPics() {
    stop=false;
    $("#Photo").css('background-image','url(/assets/images/photographer-camera.svg)')

}

function startCamera(video,canvas) {
    openVideo(video);
    timerCallback(video,canvas,{});
}








