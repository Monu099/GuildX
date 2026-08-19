package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ChannelType
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.DiscordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscordMainScreen(
  viewModel: DiscordViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val listState = rememberLazyListState()

  // Auto scroll to bottom when messages update
  LaunchedEffect(uiState.currentMessages.size) {
    if (uiState.currentMessages.isNotEmpty()) {
      listState.animateScrollToItem(uiState.currentMessages.size - 1)
    }
  }

  // Pulsing animation for Live indicator
  val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(DiscordImmersiveBase)
      .statusBarsPadding()
      .navigationBarsPadding()
  ) {
    Row(modifier = Modifier.fillMaxSize()) {
      // Left Server Rail (Always visible or toggled with animation)
      AnimatedVisibility(
        visible = uiState.isServerDrawerOpen,
        enter = slideInHorizontally { -it } + fadeIn(),
        exit = slideOutHorizontally { -it } + fadeOut()
      ) {
        Row(modifier = Modifier.fillMaxHeight()) {
          ServerRail(
            servers = uiState.servers,
            selectedServerId = uiState.selectedServerId,
            onSelectServer = { viewModel.selectServer(it) },
            onAddServerClick = { viewModel.setCreateServerDialogOpen(true) }
          )

          ChannelDrawer(
            server = uiState.selectedServer,
            selectedChannelId = uiState.selectedChannelId,
            currentUser = uiState.currentUser,
            isMuted = uiState.isMuted,
            isDeafened = uiState.isDeafened,
            onSelectChannel = { viewModel.selectChannel(it) },
            onToggleMute = { viewModel.toggleMute() },
            onToggleDeafen = { viewModel.toggleDeafen() },
            onAddChannelClick = { viewModel.setCreateChannelDialogOpen(true) },
            onUserClick = { viewModel.openUserProfile(uiState.selectedServer.members.firstOrNull()) }
          )
        }
      }

      // Main Center Chat Stream Area (Floating Immersive Canvas with Rounded Top Edge)
      Column(
        modifier = Modifier
          .weight(1f)
          .fillMaxHeight()
          .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
          .background(DiscordDarkChatBg)
      ) {
        // Chat Top Bar (Immersive Header)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .background(DiscordDarkChatBg)
            .border(1.dp, DiscordImmersiveBorderSubtle)
            .padding(horizontal = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            IconButton(
              onClick = { viewModel.toggleServerDrawer() },
              modifier = Modifier.size(36.dp).testTag("toggle_server_drawer")
            ) {
              Icon(
                imageVector = if (uiState.isServerDrawerOpen) Icons.Default.MenuOpen else Icons.Default.Menu,
                contentDescription = "Toggle Sidebar",
                tint = DiscordTextSecondary
              )
            }

            Spacer(modifier = Modifier.width(4.dp))

            when (uiState.selectedChannel.type) {
              ChannelType.TEXT -> {
                Text(
                  text = "#",
                  color = DiscordTextMuted,
                  fontWeight = FontWeight.Bold,
                  fontSize = 20.sp
                )
              }
              ChannelType.ANNOUNCEMENT -> {
                Icon(
                  imageVector = Icons.Default.Campaign,
                  contentDescription = null,
                  tint = DiscordTextMuted,
                  modifier = Modifier.size(18.dp)
                )
              }
              ChannelType.VOICE -> {
                Icon(
                  imageVector = Icons.Default.VolumeUp,
                  contentDescription = null,
                  tint = DiscordLiveGreen,
                  modifier = Modifier.size(18.dp)
                )
              }
              ChannelType.STAGE -> {
                Icon(
                  imageVector = Icons.Default.Podcasts,
                  contentDescription = null,
                  tint = DiscordFuchsia,
                  modifier = Modifier.size(18.dp)
                )
              }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
              Text(
                text = uiState.selectedChannel.name.removePrefix("🔊 "),
                color = DiscordWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              if (uiState.selectedChannel.topic.isNotBlank()) {
                Text(
                  text = uiState.selectedChannel.topic,
                  color = DiscordTextMuted,
                  fontSize = 10.5.sp,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }
          }

          // Top action icons: Threads, Search, Members
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(
              onClick = { /* Threads / Chat */ },
              modifier = Modifier.size(36.dp)
            ) {
              Text(text = "💬", fontSize = 15.sp)
            }

            IconButton(
              onClick = { viewModel.toggleMemberList() },
              modifier = Modifier.size(36.dp).testTag("toggle_member_list")
            ) {
              Icon(
                imageVector = Icons.Default.People,
                contentDescription = "Members List",
                tint = if (uiState.isMemberListOpen) DiscordBlurple else DiscordTextSecondary
              )
            }
          }
        }

        // Chat Message List
        val filteredMessages = remember(uiState.currentMessages, uiState.searchQuery) {
          if (uiState.searchQuery.isBlank()) uiState.currentMessages
          else uiState.currentMessages.filter { it.content.contains(uiState.searchQuery, ignoreCase = true) }
        }

        LazyColumn(
          state = listState,
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
        ) {
          // Channel Welcome Header
          item {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(54.dp)
                  .clip(RoundedCornerShape(18.dp))
                  .background(DiscordDarkInputBg),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "#",
                  color = DiscordWhite,
                  fontSize = 28.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "Welcome to #${uiState.selectedChannel.name.removePrefix("🔊 ")}!",
                color = DiscordWhite,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (uiState.selectedChannel.topic.isNotBlank()) uiState.selectedChannel.topic else "This is the start of the #${uiState.selectedChannel.name} channel.",
                color = DiscordTextMuted,
                fontSize = 12.5.sp
              )
              Spacer(modifier = Modifier.height(14.dp))

              // Immersive UI System Status Card
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(DiscordDarkInnerCard)
                  .border(1.dp, DiscordImmersiveBorder, RoundedCornerShape(16.dp))
                  .padding(14.dp)
              ) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "SYSTEM STATUS",
                      color = DiscordTextMuted,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      letterSpacing = 1.sp
                    )

                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                      Box(
                        modifier = Modifier
                          .size(7.dp)
                          .clip(CircleShape)
                          .background(DiscordLiveGreen.copy(alpha = pulseAlpha))
                      )
                      Text(
                        text = "Live",
                        color = DiscordLiveGreen,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Medium
                      )
                    }
                  }

                  // 2-Column Metrics Grid
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                  ) {
                    // Socket Latency
                    Box(
                      modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DiscordDarkChatBg)
                        .border(1.dp, DiscordImmersiveBorderSubtle, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                    ) {
                      Column {
                        Text(
                          text = "Socket Latency",
                          color = DiscordTextMuted,
                          fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                          text = "${uiState.voicePingMs}ms",
                          color = DiscordWhite,
                          fontSize = 17.sp,
                          fontFamily = FontFamily.Monospace,
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }

                    // Active Connections
                    Box(
                      modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DiscordDarkChatBg)
                        .border(1.dp, DiscordImmersiveBorderSubtle, RoundedCornerShape(12.dp))
                        .padding(10.dp)
                    ) {
                      Column {
                        Text(
                          text = "Connections",
                          color = DiscordTextMuted,
                          fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                          text = "1,204",
                          color = DiscordWhite,
                          fontSize = 17.sp,
                          fontFamily = FontFamily.Monospace,
                          fontWeight = FontWeight.Bold
                        )
                      }
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(14.dp))
              Divider(color = DiscordImmersiveBorderSubtle, thickness = 1.dp)
            }
          }

          items(filteredMessages, key = { it.id }) { message ->
            ChatMessageItem(
              message = message,
              onReactionClick = { emoji -> viewModel.toggleReaction(message.id, emoji) },
              onReplyClick = { viewModel.setReplyingTo(it) },
              onAuthorClick = {
                val member = uiState.selectedServer.members.find { it.user.id == message.author.id }
                  ?: com.example.model.Member(message.author)
                viewModel.openUserProfile(member)
              }
            )
          }
        }

        // Voice Connected Bar (If connected to any voice room)
        if (uiState.connectedVoiceChannel != null) {
          VoiceConnectedBar(
            channel = uiState.connectedVoiceChannel!!,
            serverName = uiState.selectedServer.name,
            pingMs = uiState.voicePingMs,
            onClick = { viewModel.setVoiceStageOpen(true) },
            onDisconnect = { viewModel.disconnectVoice() }
          )
        }

        // Chat Input Bar
        ChatInputBar(
          channelName = uiState.selectedChannel.name.removePrefix("🔊 "),
          inputText = uiState.currentInputText,
          typingUsers = uiState.typingUsers,
          replyingTo = uiState.replyingTo,
          onInputChange = { viewModel.onInputChange(it) },
          onSendMessage = { viewModel.sendMessage() },
          onCancelReply = { viewModel.setReplyingTo(null) },
          onEmojiQuickInsert = { emoji ->
            viewModel.onInputChange(uiState.currentInputText + emoji)
          }
        )
      }

      // Right Member List Drawer (Toggleable)
      AnimatedVisibility(
        visible = uiState.isMemberListOpen,
        enter = slideInHorizontally { it } + fadeIn(),
        exit = slideOutHorizontally { it } + fadeOut()
      ) {
        MemberListDrawer(
          members = uiState.selectedServer.members,
          onMemberClick = { viewModel.openUserProfile(it) }
        )
      }
    }

    // Voice Stage Fullscreen Overlay
    AnimatedVisibility(
      visible = uiState.isVoiceStageOpen && uiState.connectedVoiceChannel != null,
      enter = slideInVertically { it } + fadeIn(),
      exit = slideOutVertically { it } + fadeOut()
    ) {
      if (uiState.connectedVoiceChannel != null) {
        VoiceStageOverlay(
          channel = uiState.connectedVoiceChannel!!,
          serverName = uiState.selectedServer.name,
          participants = uiState.voiceParticipants,
          isMuted = uiState.isMuted,
          isDeafened = uiState.isDeafened,
          isScreenSharing = uiState.isScreenSharing,
          isVideoOn = uiState.isVideoOn,
          pingMs = uiState.voicePingMs,
          onToggleMute = { viewModel.toggleMute() },
          onToggleDeafen = { viewModel.toggleDeafen() },
          onToggleScreenShare = { viewModel.toggleScreenShare() },
          onToggleVideo = { viewModel.toggleVideo() },
          onDisconnect = { viewModel.disconnectVoice() },
          onClose = { viewModel.setVoiceStageOpen(false) }
        )
      }
    }

    // User Profile Dialog
    if (uiState.selectedUserProfile != null) {
      UserProfileDialog(
        member = uiState.selectedUserProfile!!,
        onDismiss = { viewModel.openUserProfile(null) },
        onSendMessage = { text ->
          viewModel.onInputChange(text)
        }
      )
    }

    // Create Server Dialog
    if (uiState.isCreateServerDialogOpen) {
      CreateServerDialog(
        onDismiss = { viewModel.setCreateServerDialogOpen(false) },
        onCreate = { name, desc -> viewModel.createServer(name, desc) }
      )
    }

    // Create Channel Dialog
    if (uiState.isCreateChannelDialogOpen) {
      CreateChannelDialog(
        onDismiss = { viewModel.setCreateChannelDialogOpen(false) },
        onCreate = { name, type, category -> viewModel.createChannel(name, type, category) }
      )
    }
  }
}

