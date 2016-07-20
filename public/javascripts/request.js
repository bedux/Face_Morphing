/**
 * Created by bedux on 19/07/16.
 */


function postImage(image,callback){
    $.ajax({
        type: "POST",
        url: "/uppImage",
        contentType:"text/plain",
        data: image,
        success:function(data) {
            callback(data)
        }
    });

}


function getListFile(callback){
    $.ajax({
        type: "GET",
        url: "/listImages",
        contentType:"text/html",
        success:function(data) {
            callback(data)
        }
    });

}