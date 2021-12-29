package ru.rumigor.cookbook.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager


import androidx.core.app.NotificationCompat
import ru.rumigor.cookbook.R


class RecipeReceiver : BroadcastReceiver() {

    private val NAME_MSG = "MSG"
    private var messageId = 0

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(NAME_MSG)
        message?.let{msg ->
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "2")
                .setSmallIcon(R.drawable.cookbook)
                .setContentTitle("Broadcast Receiver")
                .setContentText(msg)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(messageId++, builder.build())
        }
    }
}