package com.example.myapplication.ui.screens.discovery

import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import com.example.myapplication.data.firebase.FirebaseService
import kotlinx.coroutines.launch
import com.example.myapplication.data.model.PhotoPrivacy
import com.example.myapplication.data.model.User
import com.example.myapplication.data.model.VerificationLevel
import com.example.myapplication.data.repository.MockData
import com.example.myapplication.ui.theme.ThemeManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(onSafetyClick: () -> Unit, onChatClick: () -> Unit, onProfileClick: () -> Unit) {
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
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "My Profile")
                    }
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
            item {
                ProfileCompletionCard()
            }
            items(MockData.sampleUsers) { user ->
                UserCard(user)
            }
        }
    }
}

@Composable
fun ProfileCompletionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("أكمل ملفك الشخصي", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text("وجود صور موثقة يزيد من فرصة قبولك بنسبة 300%", fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun UserCard(user: User) {
    var expanded by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val scale by animateFloatAsState(
        targetValue = if (isLiked) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "LikeAnimation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
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
                
                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                AnimatedVisibility(visible = expanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "المزيد من التفاصيل:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("التعليم: ${user.education.name}", style = MaterialTheme.typography.bodySmall)
                        Text("الصلاة: ${user.prayerFrequency.name}", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { 
                            scope.launch {
                                FirebaseService.sendMarriageRequest(user.id)
                                Toast.makeText(context, "تم إرسال طلب تواصل لـ ${user.firstName}", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("إرسال طلب")
                    }
                    
                    IconButton(
                        onClick = { 
                            isLiked = !isLiked
                            if (isLiked) {
                                scope.launch {
                                    FirebaseService.sendInterest(user.id)
                                    Toast.makeText(context, "تم إبداء الاهتمام بـ ${user.firstName}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.graphicsLayer(scaleX = scale, scaleY = scale)
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isLiked) Color.Red else Color.Gray
                        )
                    }
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
