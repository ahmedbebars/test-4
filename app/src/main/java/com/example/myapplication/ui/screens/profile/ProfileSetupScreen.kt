package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(onComplete: (User) -> Unit) {
    var step by remember { mutableStateOf(1) }
    
    // Form States
    var firstName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(Gender.UNKNOWN) }
    var selectedStatus by remember { mutableStateOf(SocialStatus.SINGLE) }
    var profession by remember { mutableStateOf("") }
    var selectedEducation by remember { mutableStateOf(EducationLevel.BACHELOR) }
    var bio by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ميثاق - إعداد الملف", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Progress Indicator
            LinearProgressIndicator(
                progress = step / 3f,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary
            )
            
            Text(
                text = "الخطوة $step من 3",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )

            when (step) {
                1 -> StepBasicInfo(
                    firstName, { firstName = it },
                    age, { age = it },
                    selectedGender, { selectedGender = it }
                )
                2 -> StepProfessionalInfo(
                    selectedStatus, { selectedStatus = it },
                    profession, { profession = it },
                    selectedEducation, { selectedEducation = it }
                )
                3 -> StepBio(bio, { bio = it })
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (step > 1) {
                    OutlinedButton(
                        onClick = { step-- },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("السابق")
                    }
                }
                
                Button(
                    onClick = {
                        if (step < 3) step++
                        else {
                            onComplete(User(
                                firstName = firstName,
                                age = age.toIntOrNull() ?: 0,
                                gender = selectedGender,
                                socialStatus = selectedStatus,
                                profession = profession,
                                education = selectedEducation,
                                bio = bio
                            ))
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (step == 3) "إتمام التسجيل" else "التالي")
                }
            }
        }
    }
}

@Composable
fun StepBasicInfo(
    name: String, onNameChange: (String) -> Unit,
    age: String, onAgeChange: (String) -> Unit,
    gender: Gender, onGenderChange: (Gender) -> Unit
) {
    Text("المعلومات الأساسية", style = MaterialTheme.typography.titleLarge)
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("الاسم الأول") },
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = age,
        onValueChange = onAgeChange,
        label = { Text("العمر") },
        modifier = Modifier.fillMaxWidth()
    )
    Text("الجنس", modifier = Modifier.fillMaxWidth())
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        GenderButton(Gender.MALE, gender == Gender.MALE) { onGenderChange(Gender.MALE) }
        GenderButton(Gender.FEMALE, gender == Gender.FEMALE) { onGenderChange(Gender.FEMALE) }
    }
}

@Composable
fun GenderButton(type: Gender, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(if (type == Gender.MALE) "ذكر" else "أنثى")
    }
}

@Composable
fun StepProfessionalInfo(
    status: SocialStatus, onStatusChange: (SocialStatus) -> Unit,
    job: String, onJobChange: (String) -> Unit,
    edu: EducationLevel, onEduChange: (EducationLevel) -> Unit
) {
    Text("الحالة الاجتماعية والعمل", style = MaterialTheme.typography.titleLarge)
    // Simplified selection for brevity
    Text("الحالة الاجتماعية")
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        SocialStatus.values().forEach { 
            FilterChip(
                selected = status == it,
                onClick = { onStatusChange(it) },
                label = { Text(it.name) }
            )
        }
    }
    OutlinedTextField(
        value = job,
        onValueChange = onJobChange,
        label = { Text("المهنة") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun StepBio(bio: String, onBioChange: (String) -> Unit) {
    Text("تحدث عن نفسك", style = MaterialTheme.typography.titleLarge)
    OutlinedTextField(
        value = bio,
        onValueChange = onBioChange,
        label = { Text("نبذة تعريفية") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 5
    )
}
