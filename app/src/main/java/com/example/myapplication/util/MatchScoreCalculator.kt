package com.example.myapplication.util

import com.example.myapplication.data.model.User

object MatchScoreCalculator {
    fun calculateScore(user1: User, user2: User): Int {
        var score = 0
        var totalWeight = 0

        // 1. Religion/Prayer Frequency (Weight: 40)
        totalWeight += 40
        if (user1.prayerFrequency == user2.prayerFrequency) {
            score += 40
        } else {
            // Partial match logic
            score += 20 
        }

        // 2. Location (Weight: 20)
        totalWeight += 20
        if (user1.location == user2.location) {
            score += 20
        }

        // 3. Travel/Relocation (Weight: 20)
        totalWeight += 20
        if (user1.travelWillingness == user2.travelWillingness) {
            score += 20
        }

        // 4. Education/Status (Weight: 20)
        totalWeight += 20
        if (user1.education == user2.education) {
            score += 20
        } else {
            score += 10
        }

        return ((score.toFloat() / totalWeight) * 100).toInt()
    }
}
