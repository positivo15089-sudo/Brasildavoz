package br.com.vozdobrasil.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
val BlackBg = Color(0xFF0A0A0A); val Graphite = Color(0xFF1A1A1A); val CardBg = Color(0xFF1E1E1E); val Gold = Color(0xFFFFD700); val GoldDark = Color(0xFFC9A227); val Yellow = Color(0xFFFFE14D); val White = Color(0xFFFFFFFF); val GrayText = Color(0xFFB0B0B0)
@Composable fun VozDoBrasilTheme(content: @Composable ()->Unit){
    MaterialTheme(colorScheme = darkColorScheme(primary=Gold, secondary=Yellow, background=BlackBg, surface=Graphite, onBackground=White, onSurface=White), typography = Typography(), content=content)
}
