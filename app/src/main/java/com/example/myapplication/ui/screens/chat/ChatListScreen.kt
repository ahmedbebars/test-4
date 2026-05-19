package com.example.myapplication.ui.screens.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.ChatRoom
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(onChatClick: (ChatRoom) -> Unit) {
    val sampleChats = listOf(
        ChatRoom("1", "101", "أحمد", "", "كيف حالك؟ أتمنى أن تكون بخير", Date().time, 2),
        ChatRoom("2", "102", "سارة", "", "موافق على طلب الاهتمام", Date().time - 3600000, 0)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("المحادثات الجادة", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(sampleChats) { chat ->
                ChatListItem(chat, onChatClick)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun ChatListItem(chat: ChatRoom, onClick: (ChatRoom) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(chat) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Placeholder
        Surface(
            modifier = Modifier.size(50.dp).clip(CircleShape),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(chat.participantName.take(1), fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(chat.participantName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(chat.lastMessageTimestamp)),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Text(
                chat.lastMessage,
                maxLines = 1,
                fontSize = 14.sp,
                color = if (chat.unreadCount > 0) MaterialTheme.colorScheme.onSurface else Color.Gray,
                fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
            )
        }

        if (chat.unreadCount > 0) {
            Spacer(modifier = Modifier.width(8.dp))
            Badge(containerColor = MaterialTheme.colorScheme.primary) {
                Text(chat.unreadCount.toString(), color = Color.White)
            }
        }
    }
}
