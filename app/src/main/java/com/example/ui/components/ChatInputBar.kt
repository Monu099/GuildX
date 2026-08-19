package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Message
import com.example.ui.theme.*

@Composable
fun ChatInputBar(
  channelName: String,
  inputText: String,
  typingUsers: List<String>,
  replyingTo: Message?,
  onInputChange: (String) -> Unit,
  onSendMessage: () -> Unit,
  onCancelReply: () -> Unit,
  onEmojiQuickInsert: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showEmojiBar by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(DiscordDarkChatBg)
      .padding(horizontal = 14.dp, vertical = 6.dp)
  ) {
    // Replying Header
    AnimatedVisibility(visible = replyingTo != null) {
      if (replyingTo != null) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(DiscordDarkSidebarBg, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .border(1.dp, DiscordImmersiveBorderSubtle, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Reply,
              contentDescription = null,
              tint = DiscordBlurple,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Replying to ${replyingTo.author.username}",
              color = DiscordTextNormal,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }
          IconButton(
            onClick = onCancelReply,
            modifier = Modifier.size(20.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Cancel reply",
              tint = DiscordTextMuted,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }

    // Emoji Quick Strip
    AnimatedVisibility(visible = showEmojiBar) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(DiscordDarkSidebarBg, RoundedCornerShape(12.dp))
          .border(1.dp, DiscordImmersiveBorderSubtle, RoundedCornerShape(12.dp))
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        val emojis = listOf("🔥", "🚀", "❤️", "😂", "👍", "💎", "⚡", "✨", "🎁")
        emojis.forEach { emoji ->
          Text(
            text = emoji,
            fontSize = 20.sp,
            modifier = Modifier
              .clip(CircleShape)
              .clickable {
                onEmojiQuickInsert(emoji)
                showEmojiBar = false
              }
              .padding(4.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(4.dp))

    // Main Rounded Capsule Input Container (Immersive UI Style)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .clip(RoundedCornerShape(18.dp))
        .background(DiscordDarkInputBg)
        .border(1.dp, DiscordImmersiveBorderSubtle, RoundedCornerShape(18.dp))
        .padding(horizontal = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Attachment (+) Button with Immersive rounded background
      Box(
        modifier = Modifier
          .size(28.dp)
          .clip(CircleShape)
          .background(Color(0xFF4E5058))
          .clickable { /* open attachments */ },
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Add attachment",
          tint = DiscordWhite,
          modifier = Modifier.size(18.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Input TextField
      Box(modifier = Modifier.weight(1f)) {
        if (inputText.isEmpty()) {
          Text(
            text = "Message #$channelName",
            color = DiscordTextMuted,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Medium
          )
        }
        BasicTextField(
          value = inputText,
          onValueChange = onInputChange,
          textStyle = TextStyle(
            color = DiscordWhite,
            fontSize = 13.5.sp
          ),
          cursorBrush = SolidColor(DiscordBlurple),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("chat_input_field")
        )
      }

      // Quick Emoji Actions (Gift + Emoji)
      IconButton(
        onClick = { onEmojiQuickInsert("🎁") },
        modifier = Modifier.size(28.dp)
      ) {
        Text(text = "🎁", fontSize = 14.sp)
      }

      // Emoji Toggle
      IconButton(
        onClick = { showEmojiBar = !showEmojiBar },
        modifier = Modifier.size(28.dp)
      ) {
        Icon(
          imageVector = Icons.Default.EmojiEmotions,
          contentDescription = "Emoji picker",
          tint = if (showEmojiBar) DiscordBlurple else DiscordTextMuted,
          modifier = Modifier.size(18.dp)
        )
      }

      // Send Button
      if (inputText.isNotBlank()) {
        Spacer(modifier = Modifier.width(4.dp))
        Box(
          modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(DiscordBlurple)
            .clickable { onSendMessage() }
            .testTag("send_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = DiscordWhite,
            modifier = Modifier.size(15.dp)
          )
        }
      }
    }

    // Typing Users Indicator
    if (typingUsers.isNotEmpty()) {
      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = "${typingUsers.joinToString(", ")} is typing...",
        color = DiscordTextMuted,
        fontSize = 10.5.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = 12.dp)
      )
    }

    // Bottom Ambient Indicator Bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp),
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .width(96.dp)
          .height(3.5.dp)
          .clip(RoundedCornerShape(2.dp))
          .background(DiscordImmersiveBorder)
      )
    }
  }
}

