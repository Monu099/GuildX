package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Channel
import com.example.ui.theme.*

@Composable
fun VoiceConnectedBar(
  channel: Channel,
  serverName: String,
  pingMs: Int,
  onClick: () -> Unit,
  onDisconnect: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(52.dp)
      .background(DiscordDarkCardBg)
      .clickable { onClick() }
      .padding(horizontal = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f)
    ) {
      // Pulsing Green Audio Waveform / Signal Icon
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(DiscordGreen.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.GraphicEq,
          contentDescription = "Voice Connected",
          tint = DiscordGreen,
          modifier = Modifier.size(18.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Voice Connected",
            color = DiscordGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = Icons.Default.SignalCellularAlt,
            contentDescription = "RTC Signal",
            tint = DiscordGreen,
            modifier = Modifier.size(12.dp)
          )
          Text(
            text = "${pingMs}ms",
            color = DiscordTextMuted,
            fontSize = 10.sp
          )
        }
        Text(
          text = "${channel.name} / $serverName",
          color = DiscordTextNormal,
          fontSize = 12.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }

    // Red Disconnect Call Button
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(DiscordRed.copy(alpha = 0.2f))
        .clickable { onDisconnect() }
        .testTag("disconnect_voice_button"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.CallEnd,
        contentDescription = "Disconnect from voice",
        tint = DiscordRed,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
