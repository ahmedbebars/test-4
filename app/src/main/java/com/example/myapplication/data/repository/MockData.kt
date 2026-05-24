package com.example.myapplication.data.repository

import com.example.myapplication.data.model.*

object MockData {
    val sampleUsers = listOf(
        User(
            userId = "1",
            basics = Basics(firstName = "سارة", age = 26, city = "القاهرة"),
            gender = Gender.FEMALE,
            socialStatus = SocialStatus.SINGLE,
            profession = "معلمة",
            location = "القاهرة",
            prayerFrequency = PrayerFrequency.ALWAYS,
            photoPrivacyType = PhotoPrivacy.BLURRED,
            verificationLevel = VerificationLevel.IDENTITY_VERIFIED,
            bio = "أبحث عن شخص جاد ومحترم يقدر الحياة الزوجية."
        ),
        User(
            userId = "2",
            basics = Basics(firstName = "أحمد", age = 31, city = "الرياض"),
            gender = Gender.MALE,
            socialStatus = SocialStatus.SINGLE,
            profession = "مهندس برمجيات",
            location = "الرياض",
            prayerFrequency = PrayerFrequency.SOMETIMES,
            photoPrivacyType = PhotoPrivacy.PUBLIC,
            verificationLevel = VerificationLevel.PHOTO_VERIFIED,
            bio = "مهتم بالثقافة والرياضة، وأسعى لبناء أسرة مستقرة."
        ),
        User(
            userId = "3",
            basics = Basics(firstName = "ليلى", age = 29, city = "دبي"),
            gender = Gender.FEMALE,
            socialStatus = SocialStatus.SINGLE,
            profession = "طبيبة",
            location = "دبي",
            prayerFrequency = PrayerFrequency.ALWAYS,
            photoPrivacyType = PhotoPrivacy.BLURRED,
            bio = "أبحث عن شريك متفهم وطموح."
        )
    )
}
