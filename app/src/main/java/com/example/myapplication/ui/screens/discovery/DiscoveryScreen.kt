package com.example.myapplication.ui.screens.discovery

import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.example.myapplication.data.model.PhotoPrivacy
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.VerificationLevel
import com.example.myapplication.data.repository.MockData
import com.example.myapplication.ui.theme.ThemeManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(onSafetyClick: () -> Unit, onChatClick: () -> Unit) {
    var showLanguageDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { langCode ->
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
                AppCompatDelegate.setApplicationLocales(appLocale)
                showLanguageDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("اكتشاف الشركاء", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onSafetyClick) {
                        Icon(
                            imageVector = Icons.Default.Shield, 
                            contentDescription = "Safety Center", 
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showLanguageDialog = true }) {
                        Icon(Icons.Default.Language, contentDescription = "Change Language")
                    }
                    IconButton(onClick = { ThemeManager.toggleTheme() }) {
                        Icon(
                            imageVector = if (ThemeManager.isDarkMode.value) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onChatClick,
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Chat, contentDescription = "Chats")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(MockData.sampleUsers) { user ->
                UserCard(user)
            }
        }
    }
}

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (user.photoPrivacyType == PhotoPrivacy.BLURRED) 
                                Modifier.blur(20.dp) else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer))
                    if (user.photoPrivacyType == PhotoPrivacy.BLURRED) {
                        Text("الصورة محجوزة للخصوصية", fontSize = 12.sp)
                    }
                }

                Row(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    when (user.verificationLevel) {
                        VerificationLevel.IDENTITY_VERIFIED -> {
                            Badge(containerColor = Color(0xFF4CAF50)) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(2.dp)) {
                                    Icon(Icons.Default.Shield, null, modifier = Modifier.size(12.dp), tint = Color.White)
                                    Text("هوية موثقة", fontSize = 10.sp, color = Color.White)
                                }
                            }
                        }
                        VerificationLevel.PHOTO_VERIFIED -> {
                            Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(2.dp)) {
                                    Icon(Icons.Default.Verified, null, modifier = Modifier.size(12.dp), tint = Color.White)
                                    Text("صورة مؤكدة", fontSize = 10.sp, color = Color.White)
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text("${user.firstName}، ${user.age}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Text(user.location, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Text(" • ${user.profession}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(user.bio, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Favorite, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("إبداء اهتمام")
                }
            }
        }
    }
}

@Composable
fun LanguageSelectionDialog(onDismiss: () -> Unit, onLanguageSelected: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("اختر اللغة / Select Language") },
        text = {
            Column {
                TextButton(onClick = { onLanguageSelected("ar") }, modifier = Modifier.fillMaxWidth()) { Text("العربية") }
                TextButton(onClick = { onLanguageSelected("en") }, modifier = Modifier.fillMaxWidth()) { Text("English") }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("إغلاق / Close") } }
    )
}
