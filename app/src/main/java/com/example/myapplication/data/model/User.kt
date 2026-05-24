package com.example.myapplication.data.model

data class User(
    val userId: String = "",
    val profileCreator: String = "self", // self, parent, brother_sister, relative, friend
    
    val basics: Basics = Basics(),
    val appearance: Appearance = Appearance(),
    val lifestyle: Lifestyle = Lifestyle(),
    val islamicValues: IslamicValues = IslamicValues(),
    
    val bio: String = "",
    val profileImageUrl: String = "",
    val verificationLevel: VerificationLevel = VerificationLevel.NONE,
    val isPremium: Boolean = false,
    
    // Guardian Integration
    val guardianEmail: String? = null,
    val guardianStatus: GuardianStatus = GuardianStatus.NOT_INVITED,
    
    // Compatibility fields
    val id: String = userId,
    val firstName: String = basics.firstName,
    val age: Int = basics.age,
    val profession: String = lifestyle.occupation,
    val location: String = basics.city,
    val prayerFrequency: PrayerFrequency = islamicValues.toPrayerFrequency(),
    val photoPrivacyType: PhotoPrivacy = PhotoPrivacy.BLURRED,
    
    // Direct access for legacy code (using proper types)
    val gender: Gender = Gender.UNKNOWN,
    val socialStatus: SocialStatus = SocialStatus.SINGLE,
    val travelWillingness: TravelWillingness = TravelWillingness.DISCUSSABLE,
    val smoke: Boolean = false,
    val wantChildren: Boolean = true,
    val education: EducationLevel = EducationLevel.OTHER
)

data class Basics(
    val firstName: String = "",
    val age: Int = 18,
    val gender: String = "male",
    val country: String = "",
    val city: String = "",
    val nationality: String = ""
)

data class Appearance(
    val height: Int = 160,
    val weight: Int = 60,
    val bodyType: String = "average",
    val hairColor: String = "black",
    val eyeColor: String = "brown"
)

data class Lifestyle(
    val maritalStatus: String = "single",
    val haveChildren: String = "no",
    val occupation: String = "",
    val smoke: String = "dont_smoke",
    val drink: String = "dont_drink",
    val willingToRelocate: String = "not_sure"
)

data class IslamicValues(
    val religion: String = "islam_sunni",
    val religiousValues: String = "religious",
    val attendReligiousService: String = "daily",
    val readQuran: String = "daily",
    val wearHijab: String = "no",
    val polygamy: String = "dont_accept"
)

fun IslamicValues.toPrayerFrequency(): PrayerFrequency {
    return when(attendReligiousService) {
        "daily" -> PrayerFrequency.ALWAYS
        "sometimes" -> PrayerFrequency.SOMETIMES
        else -> PrayerFrequency.RARELY
    }
}

enum class PhotoPrivacy {
    PUBLIC, BLURRED, PRIVATE
}

enum class GuardianStatus {
    NOT_INVITED, PENDING, APPROVED, REJECTED
}
