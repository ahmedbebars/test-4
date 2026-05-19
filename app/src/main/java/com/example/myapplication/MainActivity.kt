package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import com.example.myapplication.ui.screens.discovery.DiscoveryScreen
import com.example.myapplication.ui.screens.profile.ProfileSetupScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

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
                    var showDiscovery by remember { mutableStateOf(false) }

                    if (showDiscovery) {
                        DiscoveryScreen()
                    } else {
                        ProfileSetupScreen { user ->
                            // After profile setup, show discovery
                            showDiscovery = true
                            Toast.makeText(this, "أهلاً بك يا ${user.firstName}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
