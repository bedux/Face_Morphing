/**
 * Created by bedux on 19/07/16.
 */
// Leap.loop(function(frameInstance){
//     console.log(frameInstance)
//     var my_controller = new Leap.Controller({enableGestures: true});
//
//     // see Controller documentation for option details
//     my_controller.on('frame', function(frame_instance){
//         if(frame_instance.valid && frame_instance.gestures.length > 0) {
//             frame.gestures.forEach(function (gesture) {
//                 switch (gesture.type) {
//                     case "circle":
//                         console.log("Circle Gesture");
//                         break;
//                     case "keyTap":
//                         console.log("Key Tap Gesture");
//                         break;
//                     case "screenTap":
//                         console.log("Screen Tap Gesture");
//                         break;
//                     case "swipe":
//                         console.log("Swipe Gesture");
//                         break;
//                 }
//             });
//         }
//
//
//     });
//     my_controller.connect();
// });
$(window).resize(function (e) {
window.location.replace (window.location.hash);
})

$(document).on('mousewheel', function (e) {
    e.preventDefault();

});

