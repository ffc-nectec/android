package ffc.app.messaging

import android.app.Application

interface Messaging {

    fun subscripbe(token: String?)

    fun unsubscribe()
}

fun messagingModule(app: Application): Messaging = FirebaseMessaging(app)