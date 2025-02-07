package com.example.mp3

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mp3.ui.theme.MusicPlayerTheme  // 테마 확인

class MainActivity : ComponentActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 음악 파일 res/raw/sample_music.mp3를 추가했는지 확인하세요!
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_music)

        setContent {
            MusicPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicPlayerScreen(mediaPlayer)  // MusicPlayerScreen을 호출해야 음악 플레이어 UI가 표시됩니다.
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}

@Composable
fun MusicPlayerScreen(mediaPlayer: MediaPlayer) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0f) }
    val duration = mediaPlayer.duration.toFloat()

    // 음악 재생 중 SeekBar 업데이트
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = mediaPlayer.currentPosition.toFloat()
            kotlinx.coroutines.delay(1000)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Play/Pause 버튼
        Button(onClick = {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                isPlaying = false
            } else {
                mediaPlayer.start()
                isPlaying = true
            }
        }) {
            Text(if (isPlaying) "Pause Music" else "Play Music")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SeekBar로 음악 재생 위치 조절
        Slider(
            value = currentPosition,
            onValueChange = {
                mediaPlayer.seekTo(it.toInt())
                currentPosition = it
            },
            valueRange = 0f..duration,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
