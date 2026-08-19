package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Message
import com.example.model.Reaction
import com.example.ui.theme.*

@Composable
fun ChatMessageItem(
  message: Message,
  onReactionClick: (String) -> Unit,
  onReplyClick: (Message) -> Unit,
  onAuthorClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 6.dp)
      .testTag("chat_message_${message.id}")
  ) {
    // Reply Banner
    if (message.replyToAuthor != null) {
      Row(
        modifier = Modifier
          .padding(start = 28.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Reply,
          contentDescription = "Reply",
          tint = DiscordTextMuted,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "@${message.replyToAuthor}",
          color = DiscordBlurple,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = message.replyToContent ?: "",
          color = DiscordTextMuted,
          fontSize = 12.sp,
          maxLines = 1
        )
      }
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.Top
    ) {
      // Author Avatar (Immersive squircle with gradient)
      val avatarBrush = Brush.linearGradient(
        colors = listOf(
          message.author.avatarColor,
          message.author.avatarColor.copy(alpha = 0.8f)
        )
      )

      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(avatarBrush)
          .border(1.dp, DiscordImmersiveBorderSubtle, RoundedCornerShape(14.dp))
          .clickable { onAuthorClick() },
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = message.author.avatarInitials,
          color = DiscordWhite,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        // Author Header (Name + Bot badge + Timestamp)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = message.author.username,
            color = DiscordWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onAuthorClick() }
          )

          if (message.author.isBot) {
            Box(
              modifier = Modifier
                .background(DiscordBlurple, RoundedCornerShape(4.dp))
                .padding(horizontal = 5.dp, vertical = 1.5.dp)
            ) {
              Text(
                text = "BOT",
                color = DiscordWhite,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Text(
            text = message.timestamp,
            color = DiscordTextMuted,
            fontSize = 10.sp
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Immersive message container if bot/system or plain text
        if (message.author.isBot || message.isSystemMessage) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
              .background(DiscordDarkCardBg)
              .border(1.dp, DiscordImmersiveBorderSubtle, RoundedCornerShape(topStart = 0.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
              .padding(10.dp)
          ) {
            if (message.content.contains("```")) {
              FormattedContentWithCode(message.content)
            } else {
              Text(
                text = message.content,
                color = DiscordTextNormal,
                fontSize = 13.5.sp,
                lineHeight = 19.sp
              )
            }
          }
        } else {
          // Message Content or Code Block
          if (message.content.contains("```")) {
            FormattedContentWithCode(message.content)
          } else {
            Text(
              text = message.content,
              color = DiscordTextNormal,
              fontSize = 14.sp,
              lineHeight = 20.sp
            )
          }
        }

        // Reactions Flow / Row
        if (message.reactions.isNotEmpty()) {
          Spacer(modifier = Modifier.height(6.dp))
          Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            message.reactions.forEach { reaction ->
              ReactionPill(
                reaction = reaction,
                onClick = { onReactionClick(reaction.emoji) }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun FormattedContentWithCode(content: String) {
  val parts = content.split("```")
  Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
    parts.forEachIndexed { index, part ->
      if (index % 2 == 1) {
        // Monospace Code Block with Immersive styling
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(DiscordDarkInnerCard)
            .border(1.dp, DiscordImmersiveBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
        ) {
          Text(
            text = part.trim(),
            color = DiscordFuchsia,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            lineHeight = 16.sp
          )
        }
      } else {
        if (part.isNotBlank()) {
          Text(
            text = part.trim(),
            color = DiscordTextNormal,
            fontSize = 13.5.sp,
            lineHeight = 19.sp
          )
        }
      }
    }
  }
}

@Composable
private fun ReactionPill(
  reaction: Reaction,
  onClick: () -> Unit
) {
  val bgColor = if (reaction.reactedByMe) DiscordBlurple.copy(alpha = 0.25f) else DiscordDarkCardBg
  val borderColor = if (reaction.reactedByMe) DiscordBlurple else DiscordImmersiveBorder

  Row(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bgColor)
      .border(1.dp, borderColor, RoundedCornerShape(8.dp))
      .clickable { onClick() }
      .padding(horizontal = 8.dp, vertical = 3.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Text(text = reaction.emoji, fontSize = 13.sp)
    Text(
      text = reaction.count.toString(),
      color = if (reaction.reactedByMe) DiscordBlurple else DiscordTextNormal,
      fontWeight = FontWeight.Bold,
      fontSize = 12.sp
    )
  }
}

