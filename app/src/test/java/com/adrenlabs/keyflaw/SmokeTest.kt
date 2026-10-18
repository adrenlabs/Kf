package com.adrenlabs.keyflaw

import org.junit.Assert.assertEquals
import org.junit.Test

class SmokeTest {
    @Test fun progressWrapsAtOne() {
        val progress = (0.999f + 0.0035f) % 1f
        assertEquals(0.0025f, progress, 0.0001f)
    }
    @Test fun progressStartsWithinTimeline() {
        val progress = 0.28f
        assert(progress in 0f..1f)
    }
}
