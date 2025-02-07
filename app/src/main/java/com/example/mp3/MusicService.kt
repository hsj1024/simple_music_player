package com.example.mp3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat  // Import 추가

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val CHANNEL_ID = "MusicPlayerChannel"

    override fun onCreate() {
        super.onCreate()

        // 미디어 플레이어 초기화
        mediaPlayer = MediaPlayer.create(this, R.raw.song1)
        mediaPlayer.isLooping = true

        // 알림 채널 생성 (안드로이드 8.0 이상)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // 알림(Notification) 생성
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Now Playing")
            .setContentText("Song Title")
            .setSmallIcon(R.drawable.ic_music_note)  // 정확한 리소스 ID 사용
            .build()  // 빌드 호출

        // Foreground 서비스 시작
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer.start()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
