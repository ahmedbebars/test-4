package com.example.myapplication.data.repository

import com.example.myapplication.data.model.*

object MockData {
    val sampleUsers = listOf(
        User(
            id = "1",
            firstName = "سارة",
            age = 26,
            gender = Gender.FEMALE,
            socialStatus = SocialStatus.SINGLE,
            profession = "معلمة",
            location = "القاهرة",
            religion = ReligiousCommitment.RELIGIOUS,
            photoPrivacyType = PhotoPrivacy.BLURRED,
            bio = "أبحث عن شخص جاد ومحترم يقدر الحياة الزوجية."
        ),
        User(
            id = "2",
            firstName = "أحمد",
            age = 31,
            gender = Gender.MALE,
            socialStatus = SocialStatus.SINGLE,
            profession = "مهندس برمجيات",
            location = "الرياض",
            religion = ReligiousCommitment.MODERATE,
            photoPrivacyType = PhotoPrivacy.PUBLIC,
            bio = "مهتم بالثقافة والرياضة، وأسعى لبناء أسرة مستقرة."
        ),
        User(
            id = "3",
            firstName = "ليلى",
            age = 29,
            gender = Gender.FEMALE,
            socialStatus = SocialStatus.SINGLE,
            profession = "طبيبة",
            location = "دبي",
            religion = ReligiousCommitment.VERY_RELIGIOUS,
            photoPrivacyType = PhotoPrivacy.BLURRED,
            bio = "أبحث عن شريك متفهم وطموح."
        )
    )
}
