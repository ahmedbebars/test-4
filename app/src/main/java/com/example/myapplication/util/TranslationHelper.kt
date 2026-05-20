package com.example.myapplication.util

object TranslationHelper {
    fun translate(text: String): String {
        // Mock translation logic
        return when {
            text.contains("السلام عليكم", true) -> "Peace be upon you"
            text.contains("Peace be upon you", true) -> "وعليكم السلام ورحمة الله"
            text.contains("How are you", true) -> "كيف حالك؟"
            text.contains("Software Engineer", true) -> "مهندس برمجيات"
            else -> "Translation: $text (Translated)"
        }
    }
}
