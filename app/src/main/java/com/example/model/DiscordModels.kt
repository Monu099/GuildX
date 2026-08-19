package com.example.model

import androidx.compose.ui.graphics.Color

enum class UserStatus {
  ONLINE, IDLE, DND, OFFLINE
}

data class User(
  val id: String,
  val username: String,
  val tag: String,
  val avatarInitials: String,
  val avatarColor: Color,
  val status: UserStatus = UserStatus.ONLINE,
  val customStatus: String? = null,
  val bio: String = "",
  val isBot: Boolean = false,
  val isNitro: Boolean = false,
  val badges: List<String> = emptyList()
)

data class Role(
  val id: String,
  val name: String,
  val color: Color,
  val icon: String? = null
)

data class VoiceState(
  val isMuted: Boolean = false,
  val isDeafened: Boolean = false,
  val isSpeaking: Boolean = false,
  val isScreenSharing: Boolean = false,
  val isVideoOn: Boolean = false
)

data class Member(
  val user: User,
  val nickname: String? = null,
  val roles: List<Role> = emptyList(),
  val joinedAt: String = "2024-01-15",
  val voiceState: VoiceState = VoiceState()
) {
  val displayName: String
    get() = nickname ?: user.username

  val highestRoleColor: Color
    get() = roles.firstOrNull()?.color ?: Color.White
}

enum class ChannelType {
  TEXT, VOICE, ANNOUNCEMENT, STAGE
}

data class Channel(
  val id: String,
  val serverId: String,
  val name: String,
  val type: ChannelType = ChannelType.TEXT,
  val topic: String = "",
  val category: String = "TEXT CHANNELS",
  val unreadCount: Int = 0,
  val mentionCount: Int = 0,
  val isNsfw: Boolean = false,
  val voiceMembers: List<Member> = emptyList()
)

data class Reaction(
  val emoji: String,
  val count: Int,
  val reactedByMe: Boolean = false
)

data class Attachment(
  val id: String,
  val name: String,
  val type: String,
  val size: String,
  val previewColor: Color = Color(0xFF2C2F33)
)

data class Message(
  val id: String,
  val channelId: String,
  val author: User,
  val content: String,
  val timestamp: String,
  val authorRoleColor: Color = Color.White,
  val attachments: List<Attachment> = emptyList(),
  val reactions: List<Reaction> = emptyList(),
  val replyToAuthor: String? = null,
  val replyToContent: String? = null,
  val isPinned: Boolean = false,
  val isSystemMessage: Boolean = false
)

data class Server(
  val id: String,
  val name: String,
  val initials: String,
  val iconColor: Color,
  val bannerColor: Color = Color(0xFF5865F2),
  val description: String = "",
  val memberCount: Int = 128,
  val boostLevel: Int = 1,
  val unreadCount: Int = 0,
  val mentionCount: Int = 0,
  val channels: List<Channel> = emptyList(),
  val members: List<Member> = emptyList(),
  val roles: List<Role> = emptyList()
)
