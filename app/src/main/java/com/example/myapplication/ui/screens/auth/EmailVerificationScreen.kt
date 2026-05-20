package com.example.myapplication.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun EmailVerificationScreen(onVerified: () -> Unit, onLogout: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    var isChecking by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("لقد أرسلنا رابط تفعيل إلى بريدك الإلكتروني. يرجى الضغط عليه لتفعيل حسابك.") }

    // Auto-check for verification every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            auth.currentUser?.reload()
            if (auth.currentUser?.isEmailVerified == true) {
                onVerified()
                break
            }
            delay(3000)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("تأكيد البريد الإلكتروني", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isChecking) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isChecking = true
                    auth.currentUser?.reload()?.addOnCompleteListener {
                        isChecking = false
                        if (auth.currentUser?.isEmailVerified == true) {
                            onVerified()
                        } else {
                            message = "لم يتم التفعيل بعد. يرجى التأكد من الضغط على الرابط في بريدك."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("لقد قمت بالتفعيل")
            }
        }

        TextButton(onClick = { 
            auth.currentUser?.sendEmailVerification()
            message = "تم إعادة إرسال رابط التفعيل مرة أخرى."
        }) {
            Text("إعادة إرسال الرابط")
        }

        TextButton(onClick = onLogout) {
            Text("الخروج / تسجيل ببريد آخر", color = MaterialTheme.colorScheme.error)
        }
    }
}
