package com.example.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockDiscordData
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class DiscordUiState(
  val servers: List<Server> = MockDiscordData.sampleServers,
  val selectedServerId: String = MockDiscordData.sampleServers[0].id,
  val selectedChannelId: String = "c_general",
  val messages: Map<String, List<Message>> = MockDiscordData.initialMessages,
  val currentUser: User = MockDiscordData.currentUser,
  
  // Navigation & Drawer states
  val isServerDrawerOpen: Boolean = true,
  val isMemberListOpen: Boolean = false,
  val isVoiceStageOpen: Boolean = false,
  val selectedUserProfile: Member? = null,
  val isCreateServerDialogOpen: Boolean = false,
  val isCreateChannelDialogOpen: Boolean = false,
  
  // Voice connection state
  val connectedVoiceChannel: Channel? = null,
  val connectedVoiceServerId: String? = null,
  val isMuted: Boolean = false,
  val isDeafened: Boolean = false,
  val isSpeaking: Boolean = false,
  val isScreenSharing: Boolean = false,
  val isVideoOn: Boolean = false,
  val voicePingMs: Int = 24,
  val voiceParticipants: List<Member> = emptyList(),
  
  // Chat input and typing indicators
  val currentInputText: String = "",
  val typingUsers: List<String> = emptyList(),
  val replyingTo: Message? = null,
  val searchQuery: String = "",
  val isSearching: Boolean = false
) {
  val selectedServer: Server
    get() = servers.find { it.id == selectedServerId } ?: servers.first()

  val selectedChannel: Channel
    get() = selectedServer.channels.find { it.id == selectedChannelId } 
      ?: selectedServer.channels.firstOrNull { it.type == ChannelType.TEXT }
      ?: selectedServer.channels.first()

  val currentMessages: List<Message>
    get() = messages[selectedChannelId] ?: emptyList()
}

class DiscordViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(DiscordUiState())
  val uiState: StateFlow<DiscordUiState> = _uiState.asStateFlow()

  init {
    startVoiceSimulation()
    startTypingSimulation()
  }

  fun selectServer(serverId: String) {
    _uiState.update { state ->
      val server = state.servers.find { it.id == serverId } ?: state.servers.first()
      val defaultChannel = server.channels.firstOrNull { it.type == ChannelType.TEXT } ?: server.channels.first()
      state.copy(
        selectedServerId = serverId,
        selectedChannelId = defaultChannel.id,
        isServerDrawerOpen = true
      )
    }
  }

  fun selectChannel(channelId: String) {
    _uiState.update { state ->
      val channel = state.selectedServer.channels.find { it.id == channelId }
      if (channel != null && (channel.type == ChannelType.VOICE || channel.type == ChannelType.STAGE)) {
        // If tapping a voice channel, connect to it
        connectVoice(channel, state.selectedServerId)
        state.copy(
          selectedChannelId = channelId,
          isServerDrawerOpen = false,
          isVoiceStageOpen = true
        )
      } else {
        state.copy(
          selectedChannelId = channelId,
          isServerDrawerOpen = false
        )
      }
    }
  }

  fun onInputChange(newText: String) {
    _uiState.update { it.copy(currentInputText = newText) }
  }

  fun setReplyingTo(message: Message?) {
    _uiState.update { it.copy(replyingTo = message) }
  }

  fun sendMessage() {
    val text = _uiState.value.currentInputText.trim()
    if (text.isEmpty()) return

    val currentChannelId = _uiState.value.selectedChannelId
    val replyMessage = _uiState.value.replyingTo
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val currentTimeStr = "Today at " + timeFormat.format(Date())

    val newMessage = Message(
      id = "msg_" + System.currentTimeMillis(),
      channelId = currentChannelId,
      author = _uiState.value.currentUser,
      authorRoleColor = Color(0xFF5865F2),
      content = text,
      timestamp = currentTimeStr,
      replyToAuthor = replyMessage?.author?.username,
      replyToContent = replyMessage?.content?.take(40)?.let { if (replyMessage.content.length > 40) "$it..." else it }
    )

    _uiState.update { state ->
      val updatedList = (state.messages[currentChannelId] ?: emptyList()) + newMessage
      val updatedMap = state.messages.toMutableMap().apply { put(currentChannelId, updatedList) }
      state.copy(
        messages = updatedMap,
        currentInputText = "",
        replyingTo = null
      )
    }

    // Trigger an AI/Bot reply after a short delay for interactive feel
    simulateBotResponse(currentChannelId, text)
  }

  private fun simulateBotResponse(channelId: String, userMessage: String) {
    viewModelScope.launch {
      delay(1200)
      _uiState.update { it.copy(typingUsers = listOf("AlexDev")) }
      delay(1800)
      _uiState.update { it.copy(typingUsers = emptyList()) }

      val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
      val currentTimeStr = "Today at " + timeFormat.format(Date())

      val botReply = if (userMessage.contains("webrtc", ignoreCase = true) || userMessage.contains("voice", ignoreCase = true)) {
        "🔊 [LiveKit Engine]: SFU room token generated for room `$channelId`. Latency benchmark: 22ms. Audio codec: Opus 48kHz stereo."
      } else if (userMessage.contains("socket", ignoreCase = true) || userMessage.contains("prisma", ignoreCase = true)) {
        "⚡ [Express Socket.io]: Broadcasted `MESSAGE_CREATE` event across cluster rooms with 0 dropped packets."
      } else {
        "Awesome! Message received and persisted in PostgreSQL via Prisma. Ready for voice channel test in 🔊 Developer Lounge!"
      }

      val replyMsg = Message(
        id = "msg_reply_" + System.currentTimeMillis(),
        channelId = channelId,
        author = MockDiscordData.alex,
        authorRoleColor = Color(0xFFFEE75C),
        content = botReply,
        timestamp = currentTimeStr,
        reactions = listOf(Reaction("⚡", 1, false), Reaction("🚀", 1, false))
      )

      _uiState.update { state ->
        val updatedList = (state.messages[channelId] ?: emptyList()) + replyMsg
        val updatedMap = state.messages.toMutableMap().apply { put(channelId, updatedList) }
        state.copy(messages = updatedMap)
      }
    }
  }

  fun toggleReaction(messageId: String, emoji: String) {
    val channelId = _uiState.value.selectedChannelId
    _uiState.update { state ->
      val currentList = state.messages[channelId] ?: emptyList()
      val updatedList = currentList.map { msg ->
        if (msg.id == messageId) {
          val existingReaction = msg.reactions.find { it.emoji == emoji }
          val newReactions = if (existingReaction != null) {
            if (existingReaction.reactedByMe) {
              if (existingReaction.count <= 1) {
                msg.reactions.filterNot { it.emoji == emoji }
              } else {
                msg.reactions.map {
                  if (it.emoji == emoji) it.copy(count = it.count - 1, reactedByMe = false) else it
                }
              }
            } else {
              msg.reactions.map {
                if (it.emoji == emoji) it.copy(count = it.count + 1, reactedByMe = true) else it
              }
            }
          } else {
            msg.reactions + Reaction(emoji, 1, reactedByMe = true)
          }
          msg.copy(reactions = newReactions)
        } else {
          msg
        }
      }
      val updatedMap = state.messages.toMutableMap().apply { put(channelId, updatedList) }
      state.copy(messages = updatedMap)
    }
  }

  // Voice Channel Controls
  fun connectVoice(channel: Channel, serverId: String) {
    val initialParticipants = channel.voiceMembers.ifEmpty {
      listOf(
        Member(MockDiscordData.alex, voiceState = VoiceState(isSpeaking = true)),
        Member(MockDiscordData.marcus, voiceState = VoiceState(isMuted = false))
      )
    }

    val currentMember = Member(
      user = _uiState.value.currentUser,
      voiceState = VoiceState(isMuted = _uiState.value.isMuted, isDeafened = _uiState.value.isDeafened)
    )

    _uiState.update { state ->
      state.copy(
        connectedVoiceChannel = channel,
        connectedVoiceServerId = serverId,
        voiceParticipants = initialParticipants + currentMember
      )
    }
  }

  fun disconnectVoice() {
    _uiState.update { state ->
      state.copy(
        connectedVoiceChannel = null,
        connectedVoiceServerId = null,
        isVoiceStageOpen = false,
        voiceParticipants = emptyList(),
        isSpeaking = false
      )
    }
  }

  fun toggleMute() {
    _uiState.update { state ->
      val newMuted = !state.isMuted
      state.copy(
        isMuted = newMuted,
        isSpeaking = if (newMuted) false else state.isSpeaking
      )
    }
  }

  fun toggleDeafen() {
    _uiState.update { state ->
      val newDeafened = !state.isDeafened
      state.copy(
        isDeafened = newDeafened,
        isMuted = if (newDeafened) true else state.isMuted // Auto-mute on deafen
      )
    }
  }

  fun toggleScreenShare() {
    _uiState.update { it.copy(isScreenSharing = !it.isScreenSharing) }
  }

  fun toggleVideo() {
    _uiState.update { it.copy(isVideoOn = !it.isVideoOn) }
  }

  fun toggleServerDrawer() {
    _uiState.update { it.copy(isServerDrawerOpen = !it.isServerDrawerOpen) }
  }

  fun toggleMemberList() {
    _uiState.update { it.copy(isMemberListOpen = !it.isMemberListOpen) }
  }

  fun setVoiceStageOpen(open: Boolean) {
    _uiState.update { it.copy(isVoiceStageOpen = open) }
  }

  fun openUserProfile(member: Member?) {
    _uiState.update { it.copy(selectedUserProfile = member) }
  }

  fun setCreateServerDialogOpen(open: Boolean) {
    _uiState.update { it.copy(isCreateServerDialogOpen = open) }
  }

  fun setCreateChannelDialogOpen(open: Boolean) {
    _uiState.update { it.copy(isCreateChannelDialogOpen = open) }
  }

  fun createServer(name: String, description: String) {
    if (name.isBlank()) return
    val newId = "s_" + System.currentTimeMillis()
    val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase().ifEmpty { "SR" }
    val newServer = Server(
      id = newId,
      name = name,
      initials = initials,
      iconColor = Color(0xFF5865F2),
      description = description,
      channels = listOf(
        Channel("c_gen_$newId", newId, "general", ChannelType.TEXT, "General channel", "TEXT CHANNELS"),
        Channel("c_voice_$newId", newId, "🔊 General Voice", ChannelType.VOICE, "Voice room", "VOICE CHANNELS")
      ),
      members = listOf(Member(_uiState.value.currentUser, roles = listOf(MockDiscordData.roles[0])))
    )
    _uiState.update { state ->
      state.copy(
        servers = state.servers + newServer,
        selectedServerId = newId,
        selectedChannelId = "c_gen_$newId",
        isCreateServerDialogOpen = false
      )
    }
  }

  fun createChannel(name: String, type: ChannelType, category: String) {
    if (name.isBlank()) return
    val currentServerId = _uiState.value.selectedServerId
    val formattedName = if (type == ChannelType.VOICE) "🔊 $name" else name.lowercase().replace(" ", "-")
    val newChannel = Channel(
      id = "c_" + System.currentTimeMillis(),
      serverId = currentServerId,
      name = formattedName,
      type = type,
      category = category
    )
    _uiState.update { state ->
      val updatedServers = state.servers.map { s ->
        if (s.id == currentServerId) {
          s.copy(channels = s.channels + newChannel)
        } else s
      }
      state.copy(
        servers = updatedServers,
        selectedChannelId = newChannel.id,
        isCreateChannelDialogOpen = false
      )
    }
  }

  private fun startVoiceSimulation() {
    viewModelScope.launch {
      while (true) {
        delay(3500)
        _uiState.update { state ->
          if (state.connectedVoiceChannel != null && state.voiceParticipants.isNotEmpty()) {
            val updatedParticipants = state.voiceParticipants.map { m ->
              if (m.user.id == state.currentUser.id) {
                m.copy(voiceState = m.voiceState.copy(isMuted = state.isMuted, isDeafened = state.isDeafened))
              } else {
                // Randomly toggle speaking indicator for peers
                val shouldSpeak = (0..10).random() > 5 && !m.voiceState.isMuted
                m.copy(voiceState = m.voiceState.copy(isSpeaking = shouldSpeak))
              }
            }
            state.copy(
              voiceParticipants = updatedParticipants,
              voicePingMs = (18..32).random()
            )
          } else state
        }
      }
    }
  }

  private fun startTypingSimulation() {
    viewModelScope.launch {
      while (true) {
        delay(14000)
        _uiState.update { it.copy(typingUsers = listOf("SophiaUI")) }
        delay(3000)
        _uiState.update { it.copy(typingUsers = emptyList()) }
      }
    }
  }
}
