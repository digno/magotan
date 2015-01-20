// ---- gen test data

function genTestData() {
    for (i = 0; i < 1000000; i++) {
        var mobile = "139" + Math.floor(Math.random() * 100000000)
        var aid = Math.floor(Math.random() * 1000000);
        var activity = {
            "title": "mobile " + mobile + " created activity",
            "aid": aid.toString(),
            "owner": mobile.toString(),
            "ctime": new Date(),
            "content": "owner " + mobile + " activity detail.",
            "members": [{
                "mobile": mobile.toString()
            }]
        }
        var user = {
            "nickname": "user_" + mobile,
            "mobile": mobile.toString(),
            "password": "1234",
            "ctime": new Date(),
            "picture": "http://" + mobile
        }
        //console.log(user);
        //console.log(activity);
        //console.log(1 .toString());
        //console.log((1).toString());

        db.User.insert(user);
        db.Activity.insert(activity)
    }
}


db.User.ensureIndex({mobile: 1, password: 1})

db.Activity.ensureIndex({"members.mobile": 1});
db.Activity.ensureIndex({"aid": 1});


db.UndeliverMsgs.ensureIndex({"to": 1, "content.type": 1});

db.Location.ensureIndex({"aid": 1, "mobile": 1})

db.ActivityTarget.ensureIndex({"aid": 1, "utime": 1});