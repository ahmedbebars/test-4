package com.example.myapplication.data.model

import java.util.Date

data class ChatMessage(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = Date().time,
    val isRead: Boolean = false
)

data class ChatRoom(
    val id: String = "",
    val participantId: String = "",
    val participantName: String = "",
    val participantImageUrl: String = "",
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = Date().time,
    val unreadCount: Int = 0
)
