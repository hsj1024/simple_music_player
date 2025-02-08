package com.example.mp3

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MusicPlayerScreen(
    playlist: List<Song>,
    mediaPlayer: MediaPlayer,
    initialSongIndex: Int = 0,
    onSongChange: (Song, Boolean) -> Unit = { _, _ -> }
) {
    var currentSongIndex by remember { mutableStateOf(initialSongIndex) }
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0f) }

    val currentSong = playlist[currentSongIndex]
    val context = LocalContext.current

    // 다음 곡 재생 함수
    fun playNext() {
        currentSongIndex = (currentSongIndex + 1) % playlist.size
        onSongChange(playlist[currentSongIndex], true)
    }

    // 이전 곡 재생 함수
    fun playPrevious() {
        currentSongIndex = if (currentSongIndex - 1 < 0) playlist.size - 1 else currentSongIndex - 1
        onSongChange(playlist[currentSongIndex], true)
    }

    // MediaPlayer 초기화 및 재생
    LaunchedEffect(currentSong) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(
            context,
            Uri.parse("android.resource://${context.packageName}/${currentSong.songResId}")
        )
        mediaPlayer.prepare()
        mediaPlayer.start()
        isPlaying = true
        onSongChange(currentSong, isPlaying)
    }

    // SeekBar 자동 업데이트
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = mediaPlayer.currentPosition.toFloat()
            delay(1000)
        }
    }

    // UI 구성
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(  // 배경 그라디언트 적용
                        colors = listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0))
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 앨범 아트 (그림자 추가)
            Image(
                painter = painterResource(id = currentSong.albumArtResId),
                contentDescription = "Album Art",
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .shadow(10.dp)  // 그림자 효과 추가
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 곡 제목 및 아티스트 (애니메이션 효과 추가)
            AnimatedContent(targetState = currentSong) { song ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = song.title,
                        fontSize = 26.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis  // 긴 제목은 말줄임표 처리
                    )
                    Text(
                        text = song.artist,
                        fontSize = 18.sp,
                        color = Color.LightGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SeekBar (색상 및 스타일 커스터마이징)
            Slider(
                value = currentPosition,
                onValueChange = {
                    mediaPlayer.seekTo(it.toInt())
                    currentPosition = it
                },
                valueRange = 0f..mediaPlayer.duration.toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFF4081),  // 핑크색 손잡이
                    activeTrackColor = Color(0xFFFF80AB),  // 핑크색 진행 바
                    inactiveTrackColor = Color(0xFFB39DDB)  // 연보라색 미진행 바
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // 현재 시간 / 총 재생 시간 표시
            Text(
                text = "${formatTime(currentPosition.toInt())} / ${formatTime(mediaPlayer.duration)}",
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 재생 컨트롤 버튼 (크기 확대 및 색상 변경)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { playPrevious() }) {
                    Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous", tint = Color.White)
                }

                IconButton(
                    onClick = {
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.pause()
                            isPlaying = false
                            onSongChange(currentSong, false)
                        } else {
                            mediaPlayer.start()
                            isPlaying = true
                            onSongChange(currentSong, true)
                        }
                    },
                    modifier = Modifier.size(64.dp)  // 버튼 크기 키우기
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)  // 아이콘 크기 키우기
                    )
                }

                IconButton(onClick = { playNext() }) {
                    Icon(Icons.Filled.SkipNext, contentDescription = "Next", tint = Color.White)
                }
            }
        }
    }
}
