package com.example.myapplication.data.model

data class FilterCriteria(
    val minAge: Int = 18,
    val maxAge: Int = 60,
    val requiredPrayerFrequency: PrayerFrequency? = null,
    val requiredHijabStatus: HijabStatus? = null,
    val travelWillingness: TravelWillingness? = null,
    val socialStatus: SocialStatus? = null,
    val educationLevel: EducationLevel? = null
)
