package com.example.myapplication.data.firebase

import com.example.myapplication.data.model.ChatMessage
import com.example.myapplication.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object FirebaseService {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")
    private val chatsCollection = db.collection("chats")

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // Check if user is logged in
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    // Save User Profile
    suspend fun saveUserProfile(user: User) {
        val uid = auth.currentUser?.uid ?: user.id
        val userMap = hashMapOf(
            "id" to uid,
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
        usersCollection.document(uid).set(userMap).await()
    }

    // Send Message
    suspend fun sendMessage(chatRoomId: String, text: String) {
        val uid = auth.currentUser?.uid ?: return
        val messageMap = hashMapOf(
            "senderId" to uid,
            "text" to text,
            "timestamp" to System.currentTimeMillis()
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
