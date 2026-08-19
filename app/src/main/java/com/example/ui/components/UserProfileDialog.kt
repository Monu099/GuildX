package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Member
import com.example.model.UserStatus
import com.example.ui.theme.*

@Composable
fun UserProfileDialog(
  member: Member,
  onDismiss: () -> Unit,
  onSendMessage: (String) -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = DiscordDarkCardBg),
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // Banner Area
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(DiscordBlurple)
        ) {
          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = DiscordWhite
            )
          }
        }

        // Avatar + Status Offset
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-36).dp)
        ) {
          Box(
            modifier = Modifier
              .size(72.dp)
              .clip(CircleShape)
              .background(DiscordDarkCardBg)
              .padding(4.dp)
          ) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(member.user.avatarColor),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = member.user.avatarInitials,
                color = DiscordWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Profile Details
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-24).dp)
        ) {
          Text(
            text = member.displayName,
            color = DiscordWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          )
          Text(
            text = "${member.user.username}#${member.user.tag}",
            color = DiscordTextMuted,
            fontSize = 13.sp
          )

          if (member.user.customStatus != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = member.user.customStatus,
              color = DiscordTextNormal,
              fontSize = 13.sp,
              modifier = Modifier
                .fillMaxWidth()
                .background(DiscordDarkInputBg, RoundedCornerShape(8.dp))
                .padding(8.dp)
            )
          }

          if (member.user.bio.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "ABOUT ME",
              color = DiscordTextMuted,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = member.user.bio,
              color = DiscordTextNormal,
              fontSize = 13.sp
            )
          }

          // Roles List
          if (member.roles.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "ROLES",
              color = DiscordTextMuted,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              member.roles.forEach { role ->
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(role.color.copy(alpha = 0.15f))
                    .border(1.dp, role.color.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                  Text(
                    text = role.name,
                    color = role.color,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Quick Message Button
          Button(
            onClick = {
              onSendMessage("Hey @${member.user.username}!")
              onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = DiscordBlurple),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(
              imageVector = Icons.Default.Send,
              contentDescription = null,
              tint = DiscordWhite,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Send Message", color = DiscordWhite, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
