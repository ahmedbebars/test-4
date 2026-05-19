package com.example.myapplication.data.model

data class User(
    val id: String = "",
    val firstName: String = "",
    val age: Int = 0,
    val gender: Gender = Gender.UNKNOWN,
    val socialStatus: SocialStatus = SocialStatus.SINGLE,
    val education: EducationLevel = EducationLevel.OTHER,
    val profession: String = "",
    val location: String = "",
    val religion: ReligiousCommitment = ReligiousCommitment.MODERATE,
    val height: Int = 0,
    val bio: String = "",
    val partnerExpectations: String = "",
    val profileImageUrl: String = "",
    val isVerified: Boolean = false,
    val photoPrivacyType: PhotoPrivacy = PhotoPrivacy.BLURRED,
    
    // Advanced Muslima-style fields
    val prayerFrequency: PrayerFrequency = PrayerFrequency.ALWAYS,
    val hijabStatus: HijabStatus = HijabStatus.NOT_APPLICABLE,
    val polygamyStance: PolygamyStance = PolygamyStance.REJECT,
    val travelWillingness: TravelWillingness = TravelWillingness.DISCUSSABLE,
    val smoke: Boolean = false,
    val wantChildren: Boolean = true
)

enum class PhotoPrivacy {
    PUBLIC, BLURRED, PRIVATE
}
