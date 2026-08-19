package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Server
import com.example.ui.theme.*

@Composable
fun ServerRail(
  servers: List<Server>,
  selectedServerId: String,
  onSelectServer: (String) -> Unit,
  onAddServerClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .width(72.dp)
      .fillMaxHeight()
      .background(DiscordImmersiveRail)
      .padding(vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Direct Messages / Brand icon (Immersive Discord 'D' emblem)
    val isHomeSelected = selectedServerId == "home"
    ServerRailItem(
      isSelected = isHomeSelected,
      onClick = { onSelectServer(servers.firstOrNull()?.id ?: "s_dev") },
      content = {
        Text(
          text = "D",
          color = DiscordWhite,
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp
        )
      },
      badgeCount = 0,
      backgroundColor = if (isHomeSelected) DiscordBlurple else DiscordDarkSidebarBg
    )

    Spacer(modifier = Modifier.height(8.dp))
    // Divider
    Box(
      modifier = Modifier
        .width(32.dp)
        .height(2.dp)
        .background(DiscordImmersiveBorder, RoundedCornerShape(1.dp))
    )
    Spacer(modifier = Modifier.height(8.dp))

    // Server List
    LazyColumn(
      modifier = Modifier.weight(1f),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(servers, key = { it.id }) { server ->
        val isSelected = server.id == selectedServerId
        ServerRailItem(
          isSelected = isSelected,
          onClick = { onSelectServer(server.id) },
          content = {
            Text(
              text = server.initials,
              color = DiscordWhite,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          },
          badgeCount = server.mentionCount,
          hasUnread = server.unreadCount > 0,
          backgroundColor = if (isSelected) server.iconColor else DiscordDarkSidebarBg
        )
      }

      item {
        // Add Server Button
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(DiscordDarkSidebarBg)
            .clickable { onAddServerClick() }
            .testTag("add_server_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a Server",
            tint = DiscordGreen,
            modifier = Modifier.size(24.dp)
          )
        }
      }

      item {
        // Explore Discover Button
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(DiscordDarkSidebarBg)
            .clickable { /* Explore servers */ },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Explore,
            contentDescription = "Discover Public Servers",
            tint = DiscordGreen,
            modifier = Modifier.size(24.dp)
          )
        }
      }
    }

    // Bottom Status Pill Indicator
    Box(
      modifier = Modifier
        .size(44.dp)
        .clip(RoundedCornerShape(22.dp))
        .background(DiscordDarkInnerCard)
        .border(1.dp, DiscordImmersiveBorderSubtle, RoundedCornerShape(22.dp)),
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .size(10.dp)
          .clip(CircleShape)
          .background(DiscordRed)
      )
    }
  }
}

@Composable
private fun ServerRailItem(
  isSelected: Boolean,
  onClick: () -> Unit,
  content: @Composable () -> Unit,
  badgeCount: Int = 0,
  hasUnread: Boolean = false,
  backgroundColor: Color = DiscordDarkSidebarBg
) {
  val cornerRadius by animateDpAsState(
    targetValue = if (isSelected) 16.dp else 24.dp,
    label = "corner_radius"
  )
  val pillHeight by animateDpAsState(
    targetValue = if (isSelected) 40.dp else if (hasUnread) 8.dp else 0.dp,
    label = "pill_height"
  )

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(48.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // White indicator pill on left edge
    Box(
      modifier = Modifier
        .width(4.dp)
        .height(pillHeight)
        .background(
          color = DiscordWhite,
          shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
        )
    )

    Spacer(modifier = Modifier.width(6.dp))

    // Server Icon Circle / Rounded Squircle
    Box(
      modifier = Modifier
        .size(48.dp)
        .clip(RoundedCornerShape(cornerRadius))
        .background(backgroundColor)
        .clickable { onClick() }
        .testTag("server_item"),
      contentAlignment = Alignment.Center
    ) {
      content()

      // Mention Badge
      if (badgeCount > 0) {
        Box(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = 4.dp, y = 4.dp)
            .background(DiscordRed, CircleShape)
            .padding(horizontal = 5.dp, vertical = 2.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = badgeCount.toString(),
            color = DiscordWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

