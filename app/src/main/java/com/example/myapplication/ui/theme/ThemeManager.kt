package com.example.myapplication.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

object ThemeManager {
    private val _isDarkMode = mutableStateOf(false)
    val isDarkMode: State<Boolean> = _isDarkMode

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }
}
