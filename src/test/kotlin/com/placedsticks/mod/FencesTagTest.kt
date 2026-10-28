package com.placedsticks.mod

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FencesTagTest {
    @Test
    fun placedRodsAreFencesForLeads() {
        val stream = javaClass.classLoader.getResourceAsStream("data/minecraft/tags/block/fences.json")
            ?: error("missing fences tag")
        val text = stream.bufferedReader().readText()
        assertTrue(text.contains("\"replace\": false"))
        assertTrue(text.contains("placedsticks:stick"))
        assertTrue(text.contains("placedsticks:bamboo"))
    }
}
