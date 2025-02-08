package com.example.mp3

// 시간 포맷 변환 함수: 밀리초(ms)를 분:초(MM:SS) 형식으로 변환
fun formatTime(ms: Int): String {
    val totalSeconds = ms / 1000            // 밀리초를 초로 변환
    val minutes = totalSeconds / 60         // 분 계산
    val seconds = totalSeconds % 60         // 초 계산
    return String.format("%02d:%02d", minutes, seconds)
}
