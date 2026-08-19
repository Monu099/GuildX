package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Channel
import com.example.model.Member
import com.example.ui.theme.*

@Composable
fun VoiceStageOverlay(
  channel: Channel,
  serverName: String,
  participants: List<Member>,
  isMuted: Boolean,
  isDeafened: Boolean,
  isScreenSharing: Boolean,
  isVideoOn: Boolean,
  pingMs: Int,
  onToggleMute: () -> Unit,
  onToggleDeafen: () -> Unit,
  onToggleScreenShare: () -> Unit,
  onToggleVideo: () -> Unit,
  onDisconnect: () -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(DiscordDarkChatBg)
      .statusBarsPadding()
      .navigationBarsPadding()
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top Stage Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
          .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        IconButton(
          onClick = onClose,
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Minimize voice stage",
            tint = DiscordWhite,
            modifier = Modifier.size(28.dp)
          )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = channel.name,
            color = DiscordWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .background(DiscordGreen, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "LiveKit SFU Connected (${pingMs}ms)",
              color = DiscordTextMuted,
              fontSize = 11.sp
            )
          }
        }

        IconButton(
          onClick = { /* Open voice text chat */ },
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "Voice Chat",
            tint = DiscordWhite,
            modifier = Modifier.size(22.dp)
          )
        }
      }

      // Live Audio Waveform Simulation Bar
      AudioSpectrumVisualizer(
        isSpeaking = participants.any { it.voiceState.isSpeaking }
      )

      // Screen Share Tile if Active
      AnimatedVisibility(visible = isScreenSharing) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DiscordDarkCardBg)
            .border(1.dp, DiscordBlurple, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.ScreenShare,
              contentDescription = null,
              tint = DiscordBlurple,
              modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Screen Sharing (1080p 60fps WebRTC Stream)",
              color = DiscordWhite,
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp
            )
          }
        }
      }

      // Participants Grid
      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
          .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(participants, key = { it.user.id }) { member ->
          VoiceParticipantCard(member = member)
        }
      }

      // Bottom Floating Voice Control Dock
      VoiceControlsDock(
        isMuted = isMuted,
        isDeafened = isDeafened,
        isScreenSharing = isScreenSharing,
        isVideoOn = isVideoOn,
        onToggleMute = onToggleMute,
        onToggleDeafen = onToggleDeafen,
        onToggleScreenShare = onToggleScreenShare,
        onToggleVideo = onToggleVideo,
        onDisconnect = onDisconnect
      )
    }
  }
}

@Composable
private fun AudioSpectrumVisualizer(isSpeaking: Boolean) {
  val infiniteTransition = rememberInfiniteTransition(label = "audio_anim")
  val heightFactor by infiniteTransition.animateFloat(
    initialValue = 0.3f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(400, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "height_factor"
  )

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(24.dp)
      .background(DiscordDarkCardBg.copy(alpha = 0.5f))
      .padding(horizontal = 24.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    if (isSpeaking) {
      for (i in 0..15) {
        val barHeight = (8 + (i % 5) * 3 * heightFactor).dp
        Box(
          modifier = Modifier
            .width(3.dp)
            .height(barHeight)
            .clip(RoundedCornerShape(2.dp))
            .background(DiscordGreen)
        )
        Spacer(modifier = Modifier.width(3.dp))
      }
    } else {
      Text(
        text = "Ready to speak • Noise Suppression Krisp Enabled",
        color = DiscordTextMuted,
        fontSize = 11.sp
      )
    }
  }
}

@Composable
private fun VoiceParticipantCard(member: Member) {
  val isSpeaking = member.voiceState.isSpeaking

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(130.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(DiscordDarkCardBg)
      .border(
        width = if (isSpeaking) 2.5.dp else 1.dp,
        color = if (isSpeaking) DiscordGreen else DiscordDarkBorder,
        shape = RoundedCornerShape(12.dp)
      )
      .padding(8.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(54.dp)
          .clip(CircleShape)
          .background(member.user.avatarColor),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = member.user.avatarInitials,
          color = DiscordWhite,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = member.displayName,
        color = if (isSpeaking) DiscordGreen else DiscordWhite,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }

    // Status Icons top right
    Row(
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(4.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      if (member.voiceState.isMuted) {
        Box(
          modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(DiscordRed),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.MicOff,
            contentDescription = "Muted",
            tint = DiscordWhite,
            modifier = Modifier.size(12.dp)
          )
        }
      }
      if (member.voiceState.isDeafened) {
        Box(
          modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(DiscordRed),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.HeadsetOff,
            contentDescription = "Deafened",
            tint = DiscordWhite,
            modifier = Modifier.size(12.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun VoiceControlsDock(
  isMuted: Boolean,
  isDeafened: Boolean,
  isScreenSharing: Boolean,
  isVideoOn: Boolean,
  onToggleMute: () -> Unit,
  onToggleDeafen: () -> Unit,
  onToggleScreenShare: () -> Unit,
  onToggleVideo: () -> Unit,
  onDisconnect: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 14.dp),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Mic Toggle
    VoiceActionButton(
      icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
      isActive = isMuted,
      activeColor = DiscordRed,
      onClick = onToggleMute,
      contentDescription = "Toggle Mute"
    )

    // Deafen Toggle
    VoiceActionButton(
      icon = if (isDeafened) Icons.Default.HeadsetOff else Icons.Default.Headset,
      isActive = isDeafened,
      activeColor = DiscordRed,
      onClick = onToggleDeafen,
      contentDescription = "Toggle Deafen"
    )

    // Video Camera Toggle
    VoiceActionButton(
      icon = if (isVideoOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
      isActive = isVideoOn,
      activeColor = DiscordGreen,
      onClick = onToggleVideo,
      contentDescription = "Toggle Video"
    )

    // Screen Share Toggle
    VoiceActionButton(
      icon = Icons.Default.ScreenShare,
      isActive = isScreenSharing,
      activeColor = DiscordBlurple,
      onClick = onToggleScreenShare,
      contentDescription = "Share Screen"
    )

    // Disconnect Button
    Box(
      modifier = Modifier
        .size(48.dp)
        .clip(CircleShape)
        .background(DiscordRed)
        .clickable { onDisconnect() }
        .testTag("voice_stage_disconnect"),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.CallEnd,
        contentDescription = "Disconnect",
        tint = DiscordWhite,
        modifier = Modifier.size(24.dp)
      )
    }
  }
}

@Composable
private fun VoiceActionButton(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isActive: Boolean,
  activeColor: Color,
  onClick: () -> Unit,
  contentDescription: String
) {
  Box(
    modifier = Modifier
      .size(48.dp)
      .clip(CircleShape)
      .background(if (isActive) activeColor else DiscordDarkCardBg)
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = icon,
      contentDescription = contentDescription,
      tint = if (isActive) DiscordWhite else DiscordTextNormal,
      modifier = Modifier.size(22.dp)
    )
  }
}
