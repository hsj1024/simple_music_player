package com.example.mp3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent  // setContent import 추가
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mp3.ui.theme.MusicPlayerTheme
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily



class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Compose 화면 설정
        setContent {
            MusicPlayerTheme {
                SplashScreen()
            }
        }

        // 2초 후 MainActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()  // SplashActivity 종료
        }, 2000)
    }
}

@Composable
fun SplashScreen() {
    val customFont = FontFamily(Font(R.font.custom_font))  // 커스텀 폰트 불러오기

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎵 SeoJeong's Music Player 🎵",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineLarge,  // 커스텀 폰트 스타일 적용
                fontFamily = customFont,  // 폰트 적용
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
