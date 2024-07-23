const functions = require('firebase-functions');
const admin = require("firebase-admin");


admin.initializeApp({
  credential: admin.credential.cert({
    projectId: 'water-app-af594',
    clientEmail: '',
    privateKey: ''
  }),
  databaseURL: 'https://water-app-af594.firebaseio.com/'
});
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
/*exports.helpResponse = functions.database.ref('Distance')
    .onUpdate(event => {
      // Grab the current value of what was written to the Realtime Database.
var dist =0;
var db = admin.database();
var ref = db.ref("/Distance");
var ref1=db.ref("/");
// Attach an asynchronous callback to read the data at our posts reference
ref.once("value", function(snapshot) {
  console.log(snapshot.val());
  dist = snapshot.val();


  var dateUTC = new Date();
  dateUTC = dateUTC.getTime()
  var currentdate = new Date(dateUTC);
  //date shifting for IST timezone (+5 hours and 30 minutes)
  currentdate.setHours(currentdate.getHours() + 5);
  currentdate.setMinutes(currentdate.getMinutes() + 30);


  var date= currentdate.getDate() + "-"+ (currentdate.getMonth()+1)  + "-"+ currentdate.getFullYear();
var time= String(currentdate.getHours() +":"+('0'+currentdate.getMinutes()).slice(-2));
  ref1.child(date).update({
[time]: dist,
  });
var datetime = "Last Sync: " + currentdate.getUTCDate() + "/"
                + (currentdate.getUTCMonth()+1)  + "/"
                + currentdate.getUTCFullYear() + " @ "
                + currentdate.getUTCHours() + ":"
                + currentdate.getUTCMinutes() + ":"
                + currentdate.getUTCSeconds();
  console.log(dist+" when "+datetime)
}, function (errorObject) {
  console.log("The read failed: " + errorObject.code);
});

//  return snapshot.ref.child('datetime').set(snapshot.value);
return true;
});*/
