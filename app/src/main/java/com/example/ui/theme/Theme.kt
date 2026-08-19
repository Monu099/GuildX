package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DiscordDarkColorScheme = darkColorScheme(
  primary = DiscordBlurple,
  onPrimary = DiscordWhite,
  secondary = DiscordGreen,
  onSecondary = Color.Black,
  tertiary = DiscordFuchsia,
  onTertiary = DiscordWhite,
  background = DiscordDarkChatBg,
  onBackground = DiscordTextNormal,
  surface = DiscordDarkSidebarBg,
  onSurface = DiscordTextNormal,
  surfaceVariant = DiscordDarkInputBg,
  onSurfaceVariant = DiscordTextMuted,
  outline = DiscordDarkBorder,
  error = DiscordRed,
  onError = DiscordWhite
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to Discord's iconic dark aesthetic
  dynamicColor: Boolean = false, // Keep Discord branding coherent
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DiscordDarkColorScheme,
    typography = Typography,
    content = content
  )
}

