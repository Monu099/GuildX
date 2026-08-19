package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ChannelType
import com.example.ui.theme.*

@Composable
fun CreateServerDialog(
  onDismiss: () -> Unit,
  onCreate: (name: String, description: String) -> Unit
) {
  var serverName by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = DiscordDarkCardBg),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text(
          text = "Create Your Server",
          color = DiscordWhite,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Your server is where you and your friends hang out. Make yours and start talking.",
          color = DiscordTextMuted,
          fontSize = 13.sp
        )

        OutlinedTextField(
          value = serverName,
          onValueChange = { serverName = it },
          label = { Text("SERVER NAME", color = DiscordTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = DiscordWhite,
            unfocusedTextColor = DiscordWhite,
            focusedContainerColor = DiscordDarkInputBg,
            unfocusedContainerColor = DiscordDarkInputBg,
            focusedBorderColor = DiscordBlurple,
            unfocusedBorderColor = DiscordDarkBorder
          ),
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("DESCRIPTION (OPTIONAL)", color = DiscordTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = DiscordWhite,
            unfocusedTextColor = DiscordWhite,
            focusedContainerColor = DiscordDarkInputBg,
            unfocusedContainerColor = DiscordDarkInputBg,
            focusedBorderColor = DiscordBlurple,
            unfocusedBorderColor = DiscordDarkBorder
          ),
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text("Cancel", color = DiscordTextMuted)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = { onCreate(serverName, description) },
            enabled = serverName.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = DiscordBlurple),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Create", color = DiscordWhite, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun CreateChannelDialog(
  onDismiss: () -> Unit,
  onCreate: (name: String, type: ChannelType, category: String) -> Unit
) {
  var channelName by remember { mutableStateOf("") }
  var selectedType by remember { mutableStateOf(ChannelType.TEXT) }
  var categoryName by remember { mutableStateOf("TEXT CHANNELS") }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = DiscordDarkCardBg),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text(
          text = "Create Channel",
          color = DiscordWhite,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )

        // Channel Type Radio
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "CHANNEL TYPE",
            color = DiscordTextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            FilterChip(
              selected = selectedType == ChannelType.TEXT,
              onClick = {
                selectedType = ChannelType.TEXT
                categoryName = "TEXT CHANNELS"
              },
              label = { Text("# Text", color = if (selectedType == ChannelType.TEXT) DiscordWhite else DiscordTextNormal) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = DiscordBlurple,
                containerColor = DiscordDarkInputBg
              )
            )

            FilterChip(
              selected = selectedType == ChannelType.VOICE,
              onClick = {
                selectedType = ChannelType.VOICE
                categoryName = "VOICE CHANNELS"
              },
              label = { Text("🔊 Voice", color = if (selectedType == ChannelType.VOICE) DiscordWhite else DiscordTextNormal) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = DiscordGreen,
                containerColor = DiscordDarkInputBg
              )
            )

            FilterChip(
              selected = selectedType == ChannelType.STAGE,
              onClick = {
                selectedType = ChannelType.STAGE
                categoryName = "VOICE CHANNELS"
              },
              label = { Text("📡 Stage", color = if (selectedType == ChannelType.STAGE) DiscordWhite else DiscordTextNormal) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = DiscordFuchsia,
                containerColor = DiscordDarkInputBg
              )
            )
          }
        }

        OutlinedTextField(
          value = channelName,
          onValueChange = { channelName = it },
          label = { Text("CHANNEL NAME", color = DiscordTextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
          placeholder = { Text(if (selectedType == ChannelType.TEXT) "new-channel" else "General Voice", color = DiscordTextMuted) },
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = DiscordWhite,
            unfocusedTextColor = DiscordWhite,
            focusedContainerColor = DiscordDarkInputBg,
            unfocusedContainerColor = DiscordDarkInputBg,
            focusedBorderColor = DiscordBlurple,
            unfocusedBorderColor = DiscordDarkBorder
          ),
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(onClick = onDismiss) {
            Text("Cancel", color = DiscordTextMuted)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = { onCreate(channelName, selectedType, categoryName) },
            enabled = channelName.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = DiscordBlurple),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("Create Channel", color = DiscordWhite, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
