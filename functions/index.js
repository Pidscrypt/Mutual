'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

const db = admin.firestore();

function getUser(user_id){
    return db.collection('users').document(user_id).get();
}

exports.sendNotification = functions.firestore.document('users/{user_id}/notifications/{notification_id}').onWrite(event => {
    const user_id = event.params.user_id;
    const notification_id = event.params.notification_id;

    return db.collection('users').document(user_id).collection('notifications').document(notification_id).get().then(queryResult => {
        const from_user_id = queryResult.data().from;
        const message = queryResult.data().message;
        const from_user_data = getUser(from_user_id);//db.collection('users').document(from_user_id).get();
        const to_user_data = getUser(user_id);//db.collection('users').document(user_id).get();

        return Promise.all([from_user_data,to_user_data]).then(result => {
            const from_name = result[0].data().name;
            const to_name = result[1].data().name;
            const to_device_token = result[1].data().device_token;

            console.log("Notification from: "+from_name+" __ to : "+to_name);

            const payload = {
                notification : {
                    title : "Mutual: "+from_name,
                    body : message,
                    icon : "default",
                    click_action : "pidscrypt.world.mutual.mutal.CHATACTIVITYNOTIFY"
                }
                };

                return admin.messaging().sendToDevice(to_device_token, payload).then(result => {
                    console.log("Notification sent");
                    return;
                });

        });
    });
});

/*
exports.sendNotification = functions.firestore.document('/notifications/{user_id}/{notification}').onWrite((change, context) => {
    const user_id =  change.after.user_id;
    const notification_id = change.after.notification_id;

    console.log("The user Id is : ", user_id);

    if(!change.data.val()){
        return console.log("notification deleted from db", notification_id);
    }

    const fromUser = admin.firestore.document(`/users/${user_id}/${notification_id}`).once('value');
    return fromUser.then(fromUserResult => {
        const from_user_id = fromUserResult.val().from;

        const deviceToken = admin.firestore.document(`/users/${user_id}/device_token`).once('value');

    return deviceToken.then(result => {
        const token_id = result.val();
        const payload = {
        notification : {
            title : "Test Notification",
            body : "this is test notification",
            icon : "default"
        }
        };

        return admin.messaging().sendToDevice(token_id, payload).then(response => {
            console.log("notification ends here");
            return;
        });
    });

    
    });

    

});
*/

