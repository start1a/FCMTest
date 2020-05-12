package com.example.pushtest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

open class FirebasemessgingService : FirebaseMessagingService() {

    companion object {
        private val TAG = "FirebaseMsgService"
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.e(TAG, "onMessageReceived")
        sendNotification(p0.notification?.title?:"no title", p0.notification?.body?:"no body")
    }

    private fun sendNotification(title: String, msg: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)
        val mBuilder = NotificationCompat.Builder(this, "pushTest")
            .setContentTitle(title)
            .setContentText(msg)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 안드로이드 버전에 따라 분기되는 코드
        // OREO 이상의 버전 여부
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // mipmap 아이콘 사용 불가하여 별도 지정
            mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
            // NotificationChannel을 지정하는 코드가 필수적
            // 여러 NotificationChannel을 사용자에게 제공하여 채널별로 선택적 사용 가능
            val channel = NotificationChannel("pushTest", "푸시 테스트", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher)
        }
        // id가 다를 경우 여러 개의 notification 생성 가능
        notificationManager.notify(0, mBuilder.build())
    }
}