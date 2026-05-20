package com.example.myapplication.ui.screens.safety

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetyCenterScreen(onBackClick: () -> Unit) {
    BackHandler(onBack = onBackClick)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("مركز الأمان", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "أمانك هو أولويتنا القصوى في ميثاق",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                "نحن ملتزمون بتوفير بيئة جادة ومحترمة للبحث عن شريك الحياة. إليك بعض النصائح والأدوات لضمان رحلة آمنة:",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SafetyItem(
                icon = Icons.Default.Shield,
                title = "التعارف الجاد فقط",
                description = "ميثاق مخصص للزواج فقط. أي محاولة للمواعدة العابرة أو السلوك غير اللائق تؤدي لحظر الحساب فوراً."
            )

            SafetyItem(
                icon = Icons.Default.PrivacyTip,
                title = "حماية خصوصيتك",
                description = "لا تشارك معلوماتك الحساسة مثل عنوان السكن أو التفاصيل البنكية مع أي شخص. استخدم ميزة تمويه الصور للتحكم في من يراك."
            )

            SafetyItem(
                icon = Icons.Default.Groups,
                title = "اللقاء الرسمي",
                description = "عند الانتقال للقاء الواقعي، احرص أن يكون ذلك بعلم الأهل وفي مكان عام، ويفضل وجود طرف ثالث (محرم)."
            )

            SafetyItem(
                icon = Icons.Default.Report,
                title = "نظام التبليغ",
                description = "إذا واجهت أي سلوك مشبوه أو طلبات مالية، قم بالإبلاغ عن المستخدم فوراً من داخل بروفايله. فريقنا يراجع البلاغات على مدار الساعة."
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "جميع الحسابات الموثقة تحمل شارة التحقق لضمان مصداقية البيانات.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun SafetyItem(icon: ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
