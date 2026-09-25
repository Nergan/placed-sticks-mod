package com.placedsticks.mod

import com.placedsticks.mod.util.CellView
import com.placedsticks.mod.util.PlaceWhere
import com.placedsticks.mod.util.Placement
import com.placedsticks.mod.util.RodAxes
import com.placedsticks.mod.util.RodAxis
import com.placedsticks.mod.util.deferBambooToVanilla
import com.placedsticks.mod.util.planPlacement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RodPlacementPlanTest {

    private val air = CellView(sameKind = false, axes = RodAxes.NONE, replaceable = true)
    private val stone = CellView(sameKind = false, axes = RodAxes.NONE, replaceable = false)

    @Test
    fun placesANewRodInTheNeighborCell() {
        val plan = planPlacement(stone, air, RodAxis.Y)
        assertEquals(Placement.Insert(PlaceWhere.NEIGHBOR, RodAxes.single(RodAxis.Y)), plan)
    }

    @Test
    fun replacesTallGrassInTheClickedCell() {
        val plan = planPlacement(air, stone, RodAxis.X)
        assertEquals(Placement.Insert(PlaceWhere.CLICKED, RodAxes.single(RodAxis.X)), plan)
    }

    @Test
    fun addsAMissingAxisOnTheSameBlock() {
        val existing = CellView(true, RodAxes.single(RodAxis.Y), replaceable = false)
        val plan = planPlacement(existing, air, RodAxis.X)
        val insert = plan as Placement.Insert
        assertEquals(PlaceWhere.CLICKED, insert.where)
        assertEquals(RodAxes(x = true, y = true), insert.axes)
        assertEquals(2, insert.axes.count)
    }

    @Test
    fun occupiedAxisSpillsIntoTheNextCell() {
        val existing = CellView(true, RodAxes(x = true, y = true, z = true), replaceable = false)
        val plan = planPlacement(existing, air, RodAxis.Y)
        assertEquals(Placement.Insert(PlaceWhere.NEIGHBOR, RodAxes.single(RodAxis.Y)), plan)
    }

    @Test
    fun fullSolidNeighborRejectsTheClick() {
        val existing = CellView(true, RodAxes.single(RodAxis.Y), replaceable = false)
        assertTrue(planPlacement(existing, stone, RodAxis.Y) is Placement.Pass)
    }

    @Test
    fun bambooOnSoilWithoutSneakStaysVanilla() {
        assertTrue(
            deferBambooToVanilla(
                sneaking = false,
                placingNewRod = true,
                cellReplaceable = true,
                belowSupportsBamboo = true,
            ),
        )
    }

    @Test
    fun sneakOrBadSoilOrAddingAnAxisDoesNotDefer() {
        assertTrue(
            !deferBambooToVanilla(
                sneaking = true,
                placingNewRod = true,
                cellReplaceable = true,
                belowSupportsBamboo = true,
            ),
        )
        assertTrue(
            !deferBambooToVanilla(
                sneaking = false,
                placingNewRod = true,
                cellReplaceable = true,
                belowSupportsBamboo = false,
            ),
        )
        assertTrue(
            !deferBambooToVanilla(
                sneaking = false,
                placingNewRod = false,
                cellReplaceable = true,
                belowSupportsBamboo = true,
            ),
        )
    }
}
