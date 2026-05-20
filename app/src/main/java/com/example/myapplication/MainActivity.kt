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
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.firebase.FirebaseService
import com.example.myapplication.ui.screens.auth.EmailVerificationScreen
import com.example.myapplication.ui.screens.auth.LoginScreen
import com.example.myapplication.ui.screens.auth.SignUpScreen
import com.example.myapplication.ui.screens.chat.ChatListScreen
import com.example.myapplication.ui.screens.chat.ChatRoomScreen
import com.example.myapplication.ui.screens.discovery.DiscoveryScreen
import com.example.myapplication.ui.screens.profile.ProfileSetupScreen
import com.example.myapplication.ui.screens.safety.SafetyCenterScreen
import com.example.myapplication.ui.theme.ThemeManager
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

enum class MithaqScreen {
    LOGIN, SIGNUP, VERIFY_EMAIL, PROFILE_SETUP, DISCOVERY, CHAT_LIST, CHAT_ROOM, SAFETY_CENTER
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by ThemeManager.isDarkMode
            MyApplicationTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val auth = FirebaseAuth.getInstance()
                    var currentScreen by remember { 
                        val user = auth.currentUser
                        mutableStateOf(
                            if (user != null) {
                                if (user.isEmailVerified) MithaqScreen.DISCOVERY else MithaqScreen.VERIFY_EMAIL
                            } else MithaqScreen.LOGIN
                        )
                    }
                    var selectedChatRoomName by remember { mutableStateOf("") }

                    when (currentScreen) {
                        MithaqScreen.LOGIN -> {
                            LoginScreen(
                                onLoginSuccess = { currentScreen = MithaqScreen.DISCOVERY },
                                onNavigateToSignUp = { currentScreen = MithaqScreen.SIGNUP }
                            )
                        }
                        MithaqScreen.SIGNUP -> {
                            SignUpScreen(
                                onSignUpSuccess = { currentScreen = MithaqScreen.VERIFY_EMAIL },
                                onNavigateToLogin = { currentScreen = MithaqScreen.LOGIN }
                            )
                        }
                        MithaqScreen.VERIFY_EMAIL -> {
                            EmailVerificationScreen(
                                onVerified = { currentScreen = MithaqScreen.PROFILE_SETUP },
                                onLogout = {
                                    auth.signOut()
                                    currentScreen = MithaqScreen.LOGIN
                                }
                            )
                        }
                        MithaqScreen.PROFILE_SETUP -> {
                            ProfileSetupScreen { user ->
                                lifecycleScope.launch {
                                    FirebaseService.saveUserProfile(user)
                                    currentScreen = MithaqScreen.DISCOVERY
                                    Toast.makeText(this@MainActivity, "تم حفظ ملفك بنجاح!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        MithaqScreen.DISCOVERY -> {
                            DiscoveryScreen(
                                onSafetyClick = { currentScreen = MithaqScreen.SAFETY_CENTER },
                                onChatClick = { currentScreen = MithaqScreen.CHAT_LIST }
                            )
                        }
                        MithaqScreen.CHAT_LIST -> {
                            ChatListScreen(
                                onBackClick = { currentScreen = MithaqScreen.DISCOVERY },
                                onChatClick = { chatRoom ->
                                    selectedChatRoomName = chatRoom.participantName
                                    currentScreen = MithaqScreen.CHAT_ROOM
                                }
                            )
                        }
                        MithaqScreen.CHAT_ROOM -> {
                            ChatRoomScreen(
                                participantName = selectedChatRoomName,
                                onBackClick = { currentScreen = MithaqScreen.CHAT_LIST }
                            )
                        }
                        MithaqScreen.SAFETY_CENTER -> {
                            SafetyCenterScreen(onBackClick = { currentScreen = MithaqScreen.DISCOVERY })
                        }
                    }
                }
            }
        }
    }
}
