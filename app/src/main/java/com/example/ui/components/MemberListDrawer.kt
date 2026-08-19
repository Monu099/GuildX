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
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Member
import com.example.model.UserStatus
import com.example.ui.theme.*

@Composable
fun MemberListDrawer(
  members: List<Member>,
  onMemberClick: (Member) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .width(220.dp)
      .fillMaxHeight()
      .background(DiscordDarkSidebarBg)
      .border(1.dp, DiscordImmersiveBorderSubtle)
      .padding(8.dp)
  ) {
    Text(
      text = "MEMBERS — ${members.size}",
      color = DiscordTextMuted,
      fontSize = 10.5.sp,
      fontWeight = FontWeight.Bold,
      letterSpacing = 0.5.sp,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      items(members, key = { it.user.id }) { member ->
        MemberItem(
          member = member,
          onClick = { onMemberClick(member) }
        )
      }
    }
  }
}

@Composable
private fun MemberItem(
  member: Member,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() }
      .padding(horizontal = 8.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Avatar with Status Dot
    Box(modifier = Modifier.size(34.dp)) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(member.user.avatarColor),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = member.user.avatarInitials,
          color = DiscordWhite,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }

      val statusColor = when (member.user.status) {
        UserStatus.ONLINE -> StatusOnline
        UserStatus.IDLE -> StatusIdle
        UserStatus.DND -> StatusDnd
        UserStatus.OFFLINE -> StatusOffline
      }

      Box(
        modifier = Modifier
          .size(10.dp)
          .clip(CircleShape)
          .background(statusColor)
          .align(Alignment.BottomEnd)
      )
    }

    Spacer(modifier = Modifier.width(10.dp))

    Column(modifier = Modifier.weight(1f)) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = member.displayName,
          color = member.highestRoleColor,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        if (member.user.isNitro) {
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.Default.Diamond,
            contentDescription = "Nitro",
            tint = DiscordFuchsia,
            modifier = Modifier.size(12.dp)
          )
        }
      }

      if (member.user.customStatus != null) {
        Text(
          text = member.user.customStatus,
          color = DiscordTextMuted,
          fontSize = 10.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

