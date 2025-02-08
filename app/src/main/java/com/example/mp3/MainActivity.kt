package com.example.mp3

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.mp3.ui.theme.MusicPlayerTheme
import androidx.navigation.navArgument
import android.net.Uri


class MainActivity : ComponentActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // MediaPlayer 초기화
        mediaPlayer = MediaPlayer()

        // 플레이리스트 정의
        val playlist = listOf(
            Song("when you leave", "TuneTrove", R.drawable.album_art1, R.raw.song1),
            Song("Dark Trap | Shadows of the Past", "TurtleBeats", R.drawable.album_art2, R.raw.song2),
            Song("I Will Thrive", "TuneTrove", R.drawable.album_art3, R.raw.song3),
            Song("Jazz | Autumn Leaves", "TurtleBeats", R.drawable.album_art4, R.raw.song4),
            Song("Uplifting Pop Rock | Chasing The Sun", "TurtleBeats", R.drawable.album_art5, R.raw.song5),
            Song("Musica no copyright base rap semi triste aggressiva gratis", "Robertothenice", R.drawable.album_art6, R.raw.song6)
        )

        setContent {
            MusicPlayerTheme {
                val navController = rememberNavController()
                var currentSong by remember { mutableStateOf<Song?>(null) }
                var isPlaying by remember { mutableStateOf(false) }

                NavHost(navController = navController, startDestination = "song_list") {
                    // 🔥 SongListScreen 구성
                    composable("song_list") {
                        SongListScreen(
                            navController = navController,
                            playlist = playlist,
                            currentSong = currentSong,  // 현재 재생 중인 곡 전달
                            isPlaying = isPlaying,
                            onPlayPause = {
                                if (mediaPlayer.isPlaying) {
                                    mediaPlayer.pause()
                                    isPlaying = false
                                } else {
                                    mediaPlayer.start()
                                    isPlaying = true
                                }
                            },
                            onNext = {
                                currentSong = playNextSong(playlist, currentSong, mediaPlayer)
                                isPlaying = true
                            },
                            onPrevious = {
                                currentSong = playPreviousSong(playlist, currentSong, mediaPlayer)
                                isPlaying = true
                            }
                        )
                    }

                    // 🔥 MusicPlayerScreen 구성
                    composable(
                        "player/{songIndex}",
                        arguments = listOf(navArgument("songIndex") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val songIndex = backStackEntry.arguments?.getInt("songIndex") ?: 0
                        currentSong = playlist[songIndex]  // 선택한 곡으로 상태 업데이트

                        MusicPlayerScreen(
                            playlist = playlist,
                            mediaPlayer = mediaPlayer,
                            initialSongIndex = songIndex,
                            onSongChange = { song, playing ->
                                currentSong = song  // 🔥 곡 변경 시 상태 업데이트
                                isPlaying = playing
                            }
                        )
                    }
                }
            }
        }
    }

    // 🔥 다음 곡 재생 함수
    private fun playNextSong(playlist: List<Song>, currentSong: Song?, mediaPlayer: MediaPlayer): Song {
        val currentIndex = playlist.indexOf(currentSong).takeIf { it != -1 } ?: 0
        val nextIndex = (currentIndex + 1) % playlist.size
        val nextSong = playlist[nextIndex]
        mediaPlayer.reset()
        mediaPlayer.setDataSource(this, Uri.parse("android.resource://$packageName/${nextSong.songResId}"))
        mediaPlayer.prepare()
        mediaPlayer.start()
        return nextSong
    }

    // 🔥 이전 곡 재생 함수
    private fun playPreviousSong(playlist: List<Song>, currentSong: Song?, mediaPlayer: MediaPlayer): Song {
        val currentIndex = playlist.indexOf(currentSong).takeIf { it != -1 } ?: 0
        val previousIndex = if (currentIndex - 1 < 0) playlist.size - 1 else currentIndex - 1
        val previousSong = playlist[previousIndex]
        mediaPlayer.reset()
        mediaPlayer.setDataSource(this, Uri.parse("android.resource://$packageName/${previousSong.songResId}"))
        mediaPlayer.prepare()
        mediaPlayer.start()
        return previousSong
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
