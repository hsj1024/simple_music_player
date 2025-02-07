package com.example.mp3

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mp3.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // MediaPlayer 초기화
        mediaPlayer = MediaPlayer.create(this, R.raw.song1)
        mediaPlayer.setVolume(1.0f, 1.0f)  // 볼륨 최대 설정
        mediaPlayer.isLooping = false      // 반복 재생 설정 (필요 시)

        // MediaPlayer 에러 리스너 추가
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.e("MainActivity", "MediaPlayer Error - What: $what, Extra: $extra")
            true
        }

        // MediaPlayer 상태 확인 로그 추가
        if (mediaPlayer != null) {
            Log.d("MainActivity", "MediaPlayer initialized successfully")
        } else {
            Log.e("MainActivity", "MediaPlayer initialization failed")
        }

        // 재생 시작
        mediaPlayer.start()
        if (mediaPlayer.isPlaying) {
            Log.d("MainActivity", "Media is playing")
        } else {
            Log.e("MainActivity", "Media is NOT playing")
        }

        setContent {
            MusicPlayerTheme {
                MusicPlayerScreen(
                    playlist = listOf(R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4, R.raw.song5, R.raw.song6 ),  // 여러 곡 추가
                    mediaPlayer = mediaPlayer,
                    onNext = { /* 다음 곡 로직 */ },
                    onPrevious = { /* 이전 곡 로직 */ }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
