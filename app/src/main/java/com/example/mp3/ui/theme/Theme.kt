package com.example.mp3.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.sp
import com.example.mp3.R


private val CustomFontFamily = FontFamily(Font(R.font.custom_font))  // 폰트 리소스 연결

private val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontSize = 30.sp
    ),
    headlineSmall = TextStyle(  // 리스트 제목
        fontFamily = CustomFontFamily,
        fontSize = 30.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = CustomFontFamily,
        fontSize = 30.sp
    ),
    bodyMedium = TextStyle( // 아티스트 폰트
        fontFamily = CustomFontFamily,
        fontSize = 25.sp
    )
)
// 라이트 테마 설정
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC5),
    background = Color(0xFFFFFFFF),
    onPrimary = Color.White
)

// 다크 테마 설정
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC5),
    background = Color(0xFF121212),
    onPrimary = Color.Black
)

@Composable
fun MusicPlayerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,  // 커스텀 폰트 적용
        content = content
    )
}
