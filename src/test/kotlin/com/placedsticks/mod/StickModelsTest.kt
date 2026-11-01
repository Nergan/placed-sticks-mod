package com.placedsticks.mod

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StickModelsTest {
    @Test
    fun crossingRodsNeverShareAFacePlane() {
        for (rod in listOf("stick", "bamboo")) {
            val blockstate = read("assets/placedsticks/blockstates/$rod.json")
            val models = Regex("\"model\": \"placedsticks:block/([a-z_]+)\"").findAll(blockstate).map { it.groupValues[1] }.toList()
            assertEquals(3, models.size, rod)
            val thickness = models.map { model ->
                val from = Regex("\"from\": \\[([0-9.]+),").find(read("assets/placedsticks/models/block/$model.json"))
                    ?: error("no element in $model")
                from.groupValues[1].toDouble()
            }
            assertEquals(3, thickness.toSet().size, "$rod axes $models share a thickness $thickness")
        }
    }

    private fun read(path: String): String =
        javaClass.classLoader.getResourceAsStream(path)?.bufferedReader()?.readText() ?: error("missing $path")
}
