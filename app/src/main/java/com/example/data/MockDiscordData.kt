package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.*
import com.example.ui.theme.*

object MockDiscordData {

  val currentUser = User(
    id = "u_me",
    username = "CyberKnight",
    tag = "1337",
    avatarInitials = "CK",
    avatarColor = Color(0xFF5865F2),
    status = UserStatus.ONLINE,
    customStatus = "Coding a WebRTC voice engine ⚡",
    bio = "Full-stack architect & open-source enthusiast. Building real-time decentralized systems.",
    isNitro = true,
    badges = listOf("Nitro Booster", "Early Supporter", "HypeSquad Bravery")
  )

  val alex = User(
    id = "u_alex",
    username = "AlexDev",
    tag = "0001",
    avatarInitials = "AD",
    avatarColor = Color(0xFF57F287),
    status = UserStatus.ONLINE,
    customStatus = "Refactoring Prisma schema 🚀",
    bio = "Backend engineer. TypeScript, Rust & PostgreSQL nerd.",
    badges = listOf("Server Owner", "Nitro")
  )

  val sophia = User(
    id = "u_sophia",
    username = "SophiaUI",
    tag = "4040",
    avatarInitials = "SU",
    avatarColor = Color(0xFFEB459E),
    status = UserStatus.IDLE,
    customStatus = "In Figma designing dark themes ✨",
    bio = "Product Designer & Flutter developer.",
    badges = listOf("HypeSquad Brilliance")
  )

  val marcus = User(
    id = "u_marcus",
    username = "MarcusVoice",
    tag = "8899",
    avatarInitials = "MV",
    avatarColor = Color(0xFFFEE75C),
    status = UserStatus.DND,
    customStatus = "LiveKit audio session (Do Not Disturb)",
    bio = "WebRTC & Audio Systems Lead.",
    badges = listOf("Staff Moderator")
  )

  val clydeBot = User(
    id = "u_clyde",
    username = "Clyde Bot",
    tag = "BOT",
    avatarInitials = "CB",
    avatarColor = Color(0xFF5865F2),
    status = UserStatus.ONLINE,
    customStatus = "Managing Socket.io Gateway v2.4",
    bio = "Official system bot for server alerts and real-time events.",
    isBot = true
  )

  val lofiBot = User(
    id = "u_lofi",
    username = "GrooveBot",
    tag = "BOT",
    avatarInitials = "GB",
    avatarColor = Color(0xFFFAA61A),
    status = UserStatus.ONLINE,
    customStatus = "Playing: Chillhop Radio 24/7 🎧",
    bio = "High fidelity music streaming for voice channels.",
    isBot = true
  )

  val roles = listOf(
    Role("r_owner", "👑 Server Owner", Color(0xFFFEE75C)),
    Role("r_admin", "🛡️ Admin", Color(0xFFED4245)),
    Role("r_moderator", "⚡ Moderator", Color(0xFF57F287)),
    Role("r_nitro", "💎 Nitro Booster", Color(0xFFEB459E)),
    Role("r_member", "👤 Member", Color(0xFF949BA4))
  )

  val serverMembers = listOf(
    Member(alex, nickname = "Alex (Server Lead)", roles = listOf(roles[0], roles[1])),
    Member(currentUser, nickname = null, roles = listOf(roles[1], roles[3])),
    Member(marcus, nickname = "Marcus (Audio Eng)", roles = listOf(roles[2])),
    Member(sophia, nickname = "Sophia 🎨", roles = listOf(roles[3], roles[4])),
    Member(clydeBot, nickname = "Clyde [BOT]", roles = listOf(roles[2])),
    Member(lofiBot, nickname = "GrooveBot [BOT]", roles = listOf(roles[4]))
  )

  val devServerChannels = listOf(
    Channel("c_welcome", "s_dev", "welcome-rules", ChannelType.ANNOUNCEMENT, "Welcome to the TypeScript & LiveKit developer guild! Read guidelines.", "INFORMATION"),
    Channel("c_announcements", "s_dev", "announcements", ChannelType.ANNOUNCEMENT, "Official server news and release updates.", "INFORMATION"),
    Channel("c_general", "s_dev", "general-chat", ChannelType.TEXT, "General discussions around TypeScript, Node.js & Flutter.", "TEXT CHANNELS", unreadCount = 0),
    Channel("c_backend", "s_dev", "backend-architecture", ChannelType.TEXT, "Express, Socket.io, Prisma ORM, and PostgreSQL scaling.", "TEXT CHANNELS", unreadCount = 3),
    Channel("c_webrtc", "s_dev", "webrtc-voice", ChannelType.TEXT, "LiveKit room tokens, SFU architecture, and Flutter audio drivers.", "TEXT CHANNELS"),
    Channel("c_showcase", "s_dev", "project-showcase", ChannelType.TEXT, "Show off what you are building this week!", "TEXT CHANNELS"),
    Channel("c_voice_lounge", "s_dev", "🔊 Developer Lounge", ChannelType.VOICE, "Casual voice and screen share for developers", "VOICE CHANNELS", voiceMembers = listOf(
      Member(alex, voiceState = VoiceState(isSpeaking = true)),
      Member(marcus, voiceState = VoiceState(isMuted = true))
    )),
    Channel("c_voice_standup", "s_dev", "🔊 Daily Standup", ChannelType.VOICE, "Morning engineering check-ins", "VOICE CHANNELS"),
    Channel("c_stage", "s_dev", "📡 Tech Stage Talk", ChannelType.STAGE, "Keynote on Distributed LiveKit SFU Clusters", "VOICE CHANNELS", voiceMembers = listOf(
      Member(sophia, voiceState = VoiceState(isSpeaking = false))
    ))
  )

  val gamingServerChannels = listOf(
    Channel("c_game_gen", "s_gaming", "gaming-lounge", ChannelType.TEXT, "Talk about games, setups, and tournaments.", "TEXT CHANNELS"),
    Channel("c_game_clips", "s_gaming", "clips-and-highlights", ChannelType.TEXT, "Post epic 1v4 clutches and highlights.", "TEXT CHANNELS", unreadCount = 2),
    Channel("c_game_voice1", "s_gaming", "🔊 Squad 1 [Ranked]", ChannelType.VOICE, "5-stack competitive lobby", "VOICE CHANNELS", voiceMembers = listOf(
      Member(alex, voiceState = VoiceState(isSpeaking = false)),
      Member(sophia, voiceState = VoiceState(isSpeaking = true))
    )),
    Channel("c_game_voice2", "s_gaming", "🔊 Squad 2 [Casual]", ChannelType.VOICE, "Chill gaming and streaming", "VOICE CHANNELS")
  )

  val lofiServerChannels = listOf(
    Channel("c_lofi_chat", "s_lofi", "chill-chat", ChannelType.TEXT, "Relaxed vibes and study sessions.", "TEXT CHANNELS"),
    Channel("c_lofi_stage", "s_lofi", "📻 24/7 Lo-Fi Stage", ChannelType.STAGE, "Listening party powered by GrooveBot", "VOICE CHANNELS", voiceMembers = listOf(
      Member(lofiBot, voiceState = VoiceState(isSpeaking = true)),
      Member(marcus, voiceState = VoiceState(isMuted = true))
    ))
  )

  val sampleServers = listOf(
    Server(
      id = "s_dev",
      name = "TypeScript & LiveKit Guild",
      initials = "TS",
      iconColor = Color(0xFF5865F2),
      bannerColor = Color(0xFF3B44AC),
      description = "The premier developer community for full-stack Node.js, Prisma, WebRTC voice, and Flutter mobile apps.",
      memberCount = 1420,
      boostLevel = 3,
      mentionCount = 2,
      channels = devServerChannels,
      members = serverMembers,
      roles = roles
    ),
    Server(
      id = "s_gaming",
      name = "Cyberpunk Gaming Lounge",
      initials = "CG",
      iconColor = Color(0xFFED4245),
      bannerColor = Color(0xFF9E2B2E),
      description = "Casual and competitive squad voice lobbies, clip reviews, and gaming gear showcases.",
      memberCount = 890,
      boostLevel = 2,
      mentionCount = 1,
      channels = gamingServerChannels,
      members = serverMembers.take(4),
      roles = roles
    ),
    Server(
      id = "s_lofi",
      name = "Lo-Fi Beats & Deep Work",
      initials = "LF",
      iconColor = Color(0xFF57F287),
      bannerColor = Color(0xFF2C7D47),
      description = "Co-working rooms, ambient music streams, and distraction-free study stages.",
      memberCount = 2340,
      boostLevel = 1,
      unreadCount = 5,
      channels = lofiServerChannels,
      members = serverMembers,
      roles = roles
    ),
    Server(
      id = "s_design",
      name = "UI/UX Creative Collective",
      initials = "UX",
      iconColor = Color(0xFFEB459E),
      bannerColor = Color(0xFF8B2359),
      description = "Design critique, component architecture, dark mode aesthetics & design systems.",
      memberCount = 610,
      boostLevel = 1,
      channels = listOf(
        Channel("c_ux_feed", "s_design", "design-critique", ChannelType.TEXT, "Share your prototypes for feedback", "TEXT CHANNELS"),
        Channel("c_ux_voice", "s_design", "🔊 Figma Co-Design", ChannelType.VOICE, "Live design sync session", "VOICE CHANNELS")
      ),
      members = serverMembers.take(3),
      roles = roles
    )
  )

  val initialMessages = mapOf(
    "c_general" to listOf(
      Message(
        id = "m1",
        channelId = "c_general",
        author = alex,
        authorRoleColor = Color(0xFFFEE75C),
        content = "Hey everyone! 👋 Welcome to our Discord clone architecture workspace. The Express + Socket.io gateway and LiveKit voice SFU are running smoothly.",
        timestamp = "Today at 2:15 PM",
        reactions = listOf(Reaction("🔥", 5, true), Reaction("🚀", 8, true), Reaction("❤️", 3, false))
      ),
      Message(
        id = "m2",
        channelId = "c_general",
        author = clydeBot,
        authorRoleColor = Color(0xFF57F287),
        content = "🤖 [SYSTEM EVENT] Socket.io cluster initialized with 10,000 max concurrent room subscribers. PostgreSQL + Prisma connection pool is healthy.",
        timestamp = "Today at 2:16 PM",
        isSystemMessage = true
      ),
      Message(
        id = "m3",
        channelId = "c_general",
        author = sophia,
        authorRoleColor = Color(0xFFEB459E),
        content = "The Dark Theme UI layout is super clean! Left server rail, nested collapsible category drawers, and responsive voice connected status bar. Love the blurple `#5865F2` accents!",
        timestamp = "Today at 2:18 PM",
        reactions = listOf(Reaction("✨", 4, true), Reaction("👍", 6, false))
      ),
      Message(
        id = "m4",
        channelId = "c_general",
        author = marcus,
        authorRoleColor = Color(0xFF57F287),
        content = "WebRTC voice engine tests look solid. Mute/Deafen states broadcast with sub-50ms latency across the Socket.io mesh. Try joining `🔊 Developer Lounge` on the left!",
        timestamp = "Today at 2:20 PM",
        replyToAuthor = "AlexDev",
        replyToContent = "Hey everyone! 👋 Welcome to our Discord clone...",
        reactions = listOf(Reaction("🎧", 7, true), Reaction("⚡", 4, false))
      )
    ),
    "c_backend" to listOf(
      Message(
        id = "mb1",
        channelId = "c_backend",
        author = alex,
        authorRoleColor = Color(0xFFFEE75C),
        content = "Here is our Prisma schema snippet for Server, Channel, and VoiceState relations:\n\n```prisma\nmodel VoiceState {\n  id        String   @id @default(uuid())\n  channelId String\n  userId    String   @unique\n  isMuted   Boolean  @default(false)\n  isDeafen  Boolean  @default(false)\n}\n```",
        timestamp = "Today at 1:40 PM",
        reactions = listOf(Reaction("💎", 4, true))
      ),
      Message(
        id = "mb2",
        channelId = "c_backend",
        author = marcus,
        authorRoleColor = Color(0xFF57F287),
        content = "For LiveKit token generation, make sure to pass `roomJoin: true` and set the room identity to `channel.id`. That keeps voice channel permissions locked down.",
        timestamp = "Today at 1:45 PM",
        reactions = listOf(Reaction("✅", 3, true))
      )
    ),
    "c_webrtc" to listOf(
      Message(
        id = "mw1",
        channelId = "c_webrtc",
        author = marcus,
        authorRoleColor = Color(0xFF57F287),
        content = "We have configured automatic Krisp-style noise cancellation and dynamic audio bitrates up to 384kbps for stage events!",
        timestamp = "Today at 11:20 AM",
        reactions = listOf(Reaction("🔊", 5, true))
      )
    )
  )
}
