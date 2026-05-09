package com.example.secondbrain

import org.junit.Assert.assertTrue
import org.junit.Test

class ExpiryUnitTest {
    @Test
    fun calculateExpiry_returnsFutureTimeForTemporaryNotes() {
        val before = System.currentTimeMillis()

        val oneDayExpiry = calculateExpiry("24h")
        val sevenDayExpiry = calculateExpiry("7d")

        assertTrue(oneDayExpiry >= before + 86_400_000)
        assertTrue(sevenDayExpiry >= before + 7 * 86_400_000)
    }

    @Test
    fun calculateExpiry_returnsMaxValueForForeverNotes() {
        assertTrue(calculateExpiry("forever") == Long.MAX_VALUE)
    }
}
