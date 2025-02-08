package com.example.mp3

// 곡 정보를 담는 데이터 클래스
data class Song(
    val title: String,           // 곡 제목
    val artist: String,          // 아티스트 이름
    val albumArtResId: Int,      // 앨범 아트 이미지 리소스 ID
    val songResId: Int           // 실제 노래 파일 리소스 ID
)
