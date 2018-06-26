const functions = require('firebase-functions');
const admin = require("firebase-admin");


admin.initializeApp({
  credential: admin.credential.cert({
    projectId: 'water-app-af594',
    clientEmail: 'firebase-adminsdk-tl3pb@water-app-af594.iam.gserviceaccount.com',
    privateKey: '-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC1BjMGfI5YFi1V\nNyHoat+kc0lif95zqWXMbUA5oW2d7bohd+cBF57whzJ6I3a0eGYmDp4aHIjGZg33\nBLrC/ItiLZPyEMRSnfI5wiAl0kYjWWNqVYH22oWN9gPTlEhQ9EIfx5rEHm3ZBfhH\n4FoHlSM/KaT+g5g1FzT9ng2jHmPEliHpiuiJwUzY5J07bJ687d7TEEf25vVI5Hl/\ndWj0Ci+rZY6mAj4bFxrMHRyiWOEg+ub5QOj5MpkPxza9k6VLpyTLEMvl/ZdR10mx\ncG8mfA6ii3FtC5O91FLHzUqtu9g98xl8DKID7P6Qz7KfD8xHIo5PshflkJxQNUaZ\nRBLyv4L3AgMBAAECggEAMIyblbNFF9HG0bJqGSPEnB3BQjYKCpoVb7ijP3GQbQkr\nYLuADFZx7Y9M1d36AZfCyZUlCNq7Qf5v41502kOR/EA+C+88P+4VXzE1nv8giW15\nULTjGLntlK+0wgNkS7XeoBhP5SZGDqzftCMy8hKo89Eu64WOhjqVnylK/gLx/fBT\nxhma0kkvHKeKCLAyk2WzvNYFi5f9+Vggg29Hggf3ixqq8lBHihCdBixhBHBM8jY+\nfICVrISZUfcRtDa3eMlBkHhnmbaSKs8LSb8/HQDh5NrQctLETiRAqr7oT/lfb557\ncuPYTEbHwMzl/ZOhTofwCcLKUJBNblIyBeAmheB8aQKBgQDvNmgxeIc/mE+Is04O\ngO4nwDzxkuF0Q49ONO9aC5EpSl3klkBzb3ES2az92CT9cF7mUTcnPLO3ZG0wAgAa\nX55f5a0TMvuiPvfHxXKYDUgOb5sH9KN9EmX+/PnOQstB0Qsf2BjB0bZrdbCn0dHL\nj/k5kBZ0DlMErezHI0OF2C6xTwKBgQDBume/IWxATfrXqWO4k+iZQjVrU3moeOV8\nzXysB5zlrgYzJYzSPxvSycnq2qRPbv3iEuR5sITxYGyUW5KsYKbwWN4pCWABL6ow\n90OovBb7UPgKIj6qAnDba7HCl9kUxqu71g03SQNt9to54DfwLpUynNvN+M2/nrwb\n7GIWJt2Z2QKBgQCngt0Bz5o+UWVYn99ITsb3NSo4LJgOd4TxyiXWU4VKedY4TXNC\nosZ24dG/TM3SYA9Ej5jktCBRi1TeqnfyAAWCt+40JJ6LpjaTQVQW4C4WVdclgg2+\ndtTP88zmwGVfaIi2uvfTOgzZQWf8jPSx7NcklR70FFfUTgsiBDKzlohUBwKBgDWb\ndk/uWL0mlu4Mgnq1+xxFtPCBBd43rhEt73zPEnx//lky39fh/LoEeqpRXDxa00g2\nwCBoVzFP5FvdRyYKDMffX4JthLJvVSH9mixb9+QNUMXFGiADUuV01JirWjekjisJ\nHVkaDCfmshZn1DLTQRfjTfOeEJkgvJ7hd3b3xERpAoGAN0C2U9yUnzalXLLZtXpY\nEyJ3+wfcdZbQInB1uR1wrFdxLJG1eFfv+ji9dI8SyF9xOXDLzbw8+jVp+baHFiQD\n4yCLM9Y/lDsEo/MBqB1HMTnsOBBB2X9r1SOysPhv9ZLFMiTsK4c2COoTCVtuMDkd\nZeSLJQYXJeHNB3LqrM7Du34=\n-----END PRIVATE KEY-----\n'
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
