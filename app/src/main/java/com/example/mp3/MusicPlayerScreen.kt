package com.example.mp3

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log

@Composable
fun MusicPlayerScreen(
    playlist: List<Int>,
    mediaPlayer: MediaPlayer,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {}
) {
    var currentSongIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0f) }

    val context = LocalContext.current
    val retriever = remember { MediaMetadataRetriever() }

    // 현재 곡의 URI 설정
    val songUri = Uri.parse("android.resource://${context.packageName}/${playlist[currentSongIndex]}")

    // 메타데이터 로드 (앨범 아트, 제목, 아티스트)
    try {
        retriever.setDataSource(context, songUri)
    } catch (e: Exception) {
        Log.e("MusicPlayerScreen", "Failed to retrieve metadata: ${e.localizedMessage}")
    }

    val albumArt = retriever.embeddedPicture?.let {
        BitmapFactory.decodeByteArray(it, 0, it.size)?.asImageBitmap()
    }
    val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: "Unknown Title"
    val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown Artist"

    // 다음 곡 재생 함수
    fun playNext() {
        currentSongIndex = (currentSongIndex + 1) % playlist.size  // 다음 곡 인덱스
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, Uri.parse("android.resource://${context.packageName}/${playlist[currentSongIndex]}"))
        mediaPlayer.prepare()
        mediaPlayer.start()
        isPlaying = true
    }

    // 이전 곡 재생 함수
    fun playPrevious() {
        currentSongIndex = if (currentSongIndex - 1 < 0) playlist.size - 1 else currentSongIndex - 1
        mediaPlayer.reset()
        mediaPlayer.setDataSource(context, Uri.parse("android.resource://${context.packageName}/${playlist[currentSongIndex]}"))
        mediaPlayer.prepare()
        mediaPlayer.start()
        isPlaying = true
    }

    // 재생 중 SeekBar 업데이트
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = mediaPlayer.currentPosition.toFloat()
            delay(1000)
        }
    }

    // UI 구성
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 앨범 아트 표시
        if (albumArt != null) {
            Image(
                bitmap = albumArt,
                contentDescription = "Album Art",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
        } else {
            Text("No Album Art", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 곡 제목 및 아티스트 표시
        Text(text = title, fontSize = 24.sp)
        Text(text = artist, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)

        Spacer(modifier = Modifier.height(20.dp))

        // SeekBar (재생 위치 표시)
        Slider(
            value = currentPosition,
            onValueChange = {
                mediaPlayer.seekTo(it.toInt())
                currentPosition = it
            },
            valueRange = 0f..mediaPlayer.duration.toFloat(),
            modifier = Modifier.fillMaxWidth()
        )

        // 현재 시간 / 총 재생 시간 표시
        Text("${formatTime(currentPosition.toInt())} / ${formatTime(mediaPlayer.duration)}")

        Spacer(modifier = Modifier.height(20.dp))

        // 재생 컨트롤 버튼 (이전, 재생/일시정지, 다음)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { playPrevious() }) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous")
            }

            IconButton(onClick = {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    isPlaying = false
                } else {
                    mediaPlayer.start()
                    isPlaying = true
                }
            }) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play"
                )
            }

            IconButton(onClick = { playNext() }) {
                Icon(Icons.Filled.SkipNext, contentDescription = "Next")
            }
        }
    }
}
