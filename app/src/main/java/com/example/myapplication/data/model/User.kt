package com.example.myapplication.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val age: Int = 0,
    val gender: String = "", // "Male" or "Female"
    val job: String = "",
    val location: String = "",
    val bio: String = "",
    val expectations: String = "", // ماذا يتوقع في شريك حياته
    val profileImageUrl: String = ""
)
