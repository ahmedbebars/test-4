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
import com.example.myapplication.data.model.*
import com.example.myapplication.data.repository.MockData
import com.example.myapplication.ui.theme.ThemeManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(onSafetyClick: () -> Unit, onChatClick: () -> Unit, onProfileClick: () -> Unit) {
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var filterCriteria by remember { mutableStateOf(FilterCriteria()) }

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

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState
        ) {
            FilterBottomSheetContent(
                criteria = filterCriteria,
                onCriteriaChange = { filterCriteria = it },
                onApply = { showFilterSheet = false }
            )
        }
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
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(Icons.Default.Tune, contentDescription = "Filters")
                    }
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

 @ C o m p o s a b l e 
 f u n   F i l t e r B o t t o m S h e e t C o n t e n t ( 
         c r i t e r i a :   F i l t e r C r i t e r i a , 
         o n C r i t e r i a C h a n g e :   ( F i l t e r C r i t e r i a )   - >   U n i t , 
         o n A p p l y :   ( )   - >   U n i t 
 )   { 
         C o l u m n ( 
                 m o d i f i e r   =   M o d i f i e r 
                         . f i l l M a x W i d t h ( ) 
                         . p a d d i n g ( 2 4 . d p ) 
                         . v e r t i c a l S c r o l l ( r e m e m b e r S c r o l l S t a t e ( ) ) , 
                 v e r t i c a l A r r a n g e m e n t   =   A r r a n g e m e n t . s p a c e d B y ( 1 6 . d p ) 
         )   { 
                 T e x t ( \  
 AD'*1 
 'D(-+ 
 'DE*B/E)\ ,   s t y l e   =   M a t e r i a l T h e m e . t y p o g r a p h y . h e a d l i n e S m a l l ,   f o n t W e i g h t   =   F o n t W e i g h t . B o l d ) 
 
                 T e x t ( \ 'DE-'A8) 
 9DI 
 'D5D')\ ) 
                 R o w ( h o r i z o n t a l A r r a n g e m e n t   =   A r r a n g e m e n t . s p a c e d B y ( 8 . d p ) )   { 
                         P r a y e r F r e q u e n c y . v a l u e s ( ) . f o r E a c h   { 
                                 F i l t e r C h i p ( 
                                         s e l e c t e d   =   c r i t e r i a . r e q u i r e d P r a y e r F r e q u e n c y   = =   i t , 
                                         o n C l i c k   =   {   o n C r i t e r i a C h a n g e ( c r i t e r i a . c o p y ( r e q u i r e d P r a y e r F r e q u e n c y   =   i t ) )   } , 
                                         l a b e l   =   {   T e x t ( i t . n a m e )   } 
                                 ) 
                         } 
                 } 
 
                 T e x t ( \ 'D'3*9/'/ 
 DD3A1\ ) 
                 R o w ( h o r i z o n t a l A r r a n g e m e n t   =   A r r a n g e m e n t . s p a c e d B y ( 8 . d p ) )   { 
                         T r a v e l W i l l i n g n e s s . v a l u e s ( ) . f o r E a c h   { 
                                 F i l t e r C h i p ( 
                                         s e l e c t e d   =   c r i t e r i a . t r a v e l W i l l i n g n e s s   = =   i t , 
                                         o n C l i c k   =   {   o n C r i t e r i a C h a n g e ( c r i t e r i a . c o p y ( t r a v e l W i l l i n g n e s s   =   i t ) )   } , 
                                         l a b e l   =   {   T e x t ( i t . n a m e )   } 
                                 ) 
                         } 
                 } 
 
                 B u t t o n ( 
                         o n C l i c k   =   o n A p p l y , 
                         m o d i f i e r   =   M o d i f i e r . f i l l M a x W i d t h ( ) 
                 )   { 
                         T e x t ( \ *7(JB 
 'DAD'*1\ ) 
                 } 
         } 
 }  
 