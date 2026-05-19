package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.screens.chat.ChatListScreen
import com.example.myapplication.ui.screens.chat.ChatRoomScreen
import com.example.myapplication.ui.screens.discovery.DiscoveryScreen
import com.example.myapplication.ui.screens.profile.ProfileSetupScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

enum class MithaqScreen {
    PROFILE_SETUP, DISCOVERY, CHAT_LIST, CHAT_ROOM
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf(MithaqScreen.PROFILE_SETUP) }
                    var selectedChatRoomName by remember { mutableStateOf("") }

                    when (currentScreen) {
                        MithaqScreen.PROFILE_SETUP -> {
                            ProfileSetupScreen { user ->
                                currentScreen = MithaqScreen.DISCOVERY
                                Toast.makeText(this, "أهلاً بك يا ${user.firstName}", Toast.LENGTH_SHORT).show()
                            }
                        }
                        MithaqScreen.DISCOVERY -> {
                            DiscoveryScreen()
                            // Temporarily adding a way to navigate to chat for testing
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                                Button(
                                    onClick = { currentScreen = MithaqScreen.CHAT_LIST },
                                    modifier = Modifier.padding(bottom = 80.dp)
                                ) {
                                    Text("انتقل للمحادثات (تجربة)")
                                }
                            }
                        }
                        MithaqScreen.CHAT_LIST -> {
                            ChatListScreen(onChatClick = { chatRoom ->
                                selectedChatRoomName = chatRoom.participantName
                                currentScreen = MithaqScreen.CHAT_ROOM
                            })
                        }
                        MithaqScreen.CHAT_ROOM -> {
                            ChatRoomScreen(
                                participantName = selectedChatRoomName,
                                onBackClick = { currentScreen = MithaqScreen.CHAT_LIST }
                            )
                        }
                    }
                }
            }
        }
    }
}
