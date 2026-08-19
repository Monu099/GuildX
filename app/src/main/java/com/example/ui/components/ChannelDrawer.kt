package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun ChannelDrawer(
  server: Server,
  selectedChannelId: String,
  currentUser: User,
  isMuted: Boolean,
  isDeafened: Boolean,
  onSelectChannel: (String) -> Unit,
  onToggleMute: () -> Unit,
  onToggleDeafen: () -> Unit,
  onAddChannelClick: () -> Unit,
  onUserClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .width(230.dp)
      .fillMaxHeight()
      .background(DiscordDarkSidebarBg)
  ) {
    // Server Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .background(DiscordDarkSidebarBg)
        .border(1.dp, DiscordImmersiveBorderSubtle)
        .clickable { /* Server settings menu */ }
        .padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        if (server.boostLevel > 0) {
          Icon(
            imageVector = Icons.Default.Diamond,
            contentDescription = "Boost Level",
            tint = DiscordFuchsia,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
          text = server.name,
          color = DiscordWhite,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
      Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "Server Menu",
        tint = DiscordTextMuted,
        modifier = Modifier.size(20.dp)
      )
    }

    // Channel Categories & List
    val channelsByCategory = remember(server.channels) {
      server.channels.groupBy { it.category }
    }

    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      channelsByCategory.forEach { (category, channels) ->
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 10.dp, bottom = 4.dp, start = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = DiscordTextMuted,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = category.uppercase(),
                color = DiscordTextMuted,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              )
            }
            IconButton(
              onClick = { onAddChannelClick() },
              modifier = Modifier.size(20.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Channel",
                tint = DiscordTextMuted,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }

        items(channels, key = { it.id }) { channel ->
          val isSelected = channel.id == selectedChannelId
          ChannelItem(
            channel = channel,
            isSelected = isSelected,
            onClick = { onSelectChannel(channel.id) }
          )
        }
      }
    }

    // User Profile Footer Bar (Immersive Dark Inner Card)
    UserProfileFooter(
      user = currentUser,
      isMuted = isMuted,
      isDeafened = isDeafened,
      onToggleMute = onToggleMute,
      onToggleDeafen = onToggleDeafen,
      onUserClick = onUserClick
    )
  }
}

@Composable
private fun ChannelItem(
  channel: Channel,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val bgColor = if (isSelected) DiscordDarkInputBg else Color.Transparent
  val textColor = if (isSelected) DiscordWhite else if (channel.unreadCount > 0) DiscordWhite else DiscordTextMuted

  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(bgColor)
        .clickable { onClick() }
        .padding(horizontal = 8.dp, vertical = 6.dp)
        .testTag("channel_item_${channel.id}"),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Channel icon prefix
      when (channel.type) {
        ChannelType.TEXT -> {
          Text(
            text = "#",
            color = if (isSelected) DiscordWhite else DiscordTextMuted,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
        }
        ChannelType.ANNOUNCEMENT -> {
          Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = "Announcement",
            tint = if (isSelected) DiscordWhite else DiscordTextMuted,
            modifier = Modifier.size(17.dp)
          )
        }
        ChannelType.VOICE -> {
          Icon(
            imageVector = Icons.Default.VolumeUp,
            contentDescription = "Voice Channel",
            tint = if (isSelected) DiscordLiveGreen else DiscordTextMuted,
            modifier = Modifier.size(17.dp)
          )
        }
        ChannelType.STAGE -> {
          Icon(
            imageVector = Icons.Default.Podcasts,
            contentDescription = "Stage Channel",
            tint = if (isSelected) DiscordFuchsia else DiscordTextMuted,
            modifier = Modifier.size(17.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      Text(
        text = channel.name.removePrefix("🔊 "),
        color = textColor,
        fontWeight = if (isSelected || channel.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
        fontSize = 13.5.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f)
      )

      if (channel.unreadCount > 0) {
        Box(
          modifier = Modifier
            .background(DiscordRed, CircleShape)
            .padding(horizontal = 6.dp, vertical = 1.dp)
        ) {
          Text(
            text = channel.unreadCount.toString(),
            color = DiscordWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // If channel has active voice members, display them indented
    if ((channel.type == ChannelType.VOICE || channel.type == ChannelType.STAGE) && channel.voiceMembers.isNotEmpty()) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 28.dp, top = 2.dp, bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
      ) {
        channel.voiceMembers.forEach { member ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(18.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(member.user.avatarColor),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = member.user.avatarInitials,
                color = DiscordWhite,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = member.displayName,
              color = if (member.voiceState.isSpeaking) DiscordLiveGreen else DiscordTextMuted,
              fontSize = 12.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.weight(1f)
            )
            if (member.voiceState.isMuted) {
              Icon(
                imageVector = Icons.Default.MicOff,
                contentDescription = "Muted",
                tint = DiscordRed,
                modifier = Modifier.size(12.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun UserProfileFooter(
  user: User,
  isMuted: Boolean,
  isDeafened: Boolean,
  onToggleMute: () -> Unit,
  onToggleDeafen: () -> Unit,
  onUserClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(56.dp)
      .background(DiscordDarkInnerCard)
      .border(1.dp, DiscordImmersiveBorderSubtle)
      .padding(horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // User Avatar with Status dot
    Row(
      modifier = Modifier
        .weight(1f)
        .clip(RoundedCornerShape(8.dp))
        .clickable { onUserClick() }
        .padding(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(modifier = Modifier.size(34.dp)) {
        Box(
          modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(user.avatarColor),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = user.avatarInitials,
            color = DiscordWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
        // Online status dot
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(StatusOnline)
            .align(Alignment.BottomEnd)
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Column {
        Text(
          text = user.username,
          color = DiscordWhite,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = "#${user.tag}",
          color = DiscordTextMuted,
          fontSize = 11.sp
        )
      }
    }

    // Mute Button
    IconButton(
      onClick = onToggleMute,
      modifier = Modifier.size(30.dp).testTag("mute_button")
    ) {
      Icon(
        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
        contentDescription = if (isMuted) "Unmute" else "Mute",
        tint = if (isMuted) DiscordRed else DiscordTextNormal,
        modifier = Modifier.size(18.dp)
      )
    }

    // Deafen Button
    IconButton(
      onClick = onToggleDeafen,
      modifier = Modifier.size(30.dp).testTag("deafen_button")
    ) {
      Icon(
        imageVector = if (isDeafened) Icons.Default.HeadsetOff else Icons.Default.Headset,
        contentDescription = if (isDeafened) "Undeafen" else "Deafen",
        tint = if (isDeafened) DiscordRed else DiscordTextNormal,
        modifier = Modifier.size(18.dp)
      )
    }

    // Settings Cog
    IconButton(
      onClick = onUserClick,
      modifier = Modifier.size(30.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Settings,
        contentDescription = "User Settings",
        tint = DiscordTextNormal,
        modifier = Modifier.size(18.dp)
      )
    }
  }
}

