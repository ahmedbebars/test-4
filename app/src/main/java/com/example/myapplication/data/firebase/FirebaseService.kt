package com.example.myapplication.data.firebase

import com.example.myapplication.data.model.ChatMessage
import com.example.myapplication.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object FirebaseService {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")
    private val chatsCollection = db.collection("chats")

    // Save User Profile
    suspend fun saveUserProfile(user: User) {
        val userMap = hashMapOf(
            "id" to user.id,
            "firstName" to user.firstName,
            "age" to user.age,
            "gender" to user.gender.name,
            "socialStatus" to user.socialStatus.name,
            "profession" to user.profession,
            "bio" to user.bio,
            "location" to user.location,
            "verificationLevel" to user.verificationLevel.name,
            "prayerFrequency" to user.prayerFrequency.name,
            "travelWillingness" to user.travelWillingness.name,
            "smoke" to user.smoke,
            "wantChildren" to user.wantChildren,
            "profileImageUrl" to user.profileImageUrl
        )
        usersCollection.document(user.id).set(userMap).await()
    }

    // Send Message
    suspend fun sendMessage(chatRoomId: String, message: ChatMessage) {
        val messageMap = hashMapOf(
            "senderId" to message.senderId,
            "text" to message.text,
            "timestamp" to message.timestamp
        )
        chatsCollection.document(chatRoomId).collection("messages").add(messageMap).await()
    }

    // Real-time Messages Flow
    fun getMessages(chatRoomId: String): Flow<List<ChatMessage>> = callbackFlow {
        val subscription = chatsCollection.document(chatRoomId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        ChatMessage(
                            id = doc.id,
                            senderId = doc.getString("senderId") ?: "",
                            text = doc.getString("text") ?: "",
                            timestamp = doc.getLong("timestamp") ?: 0L
                        )
                    }
                    trySend(messages)
                }
            }
        awaitClose { subscription.remove() }
    }
}
