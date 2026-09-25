package com.placedsticks.mod.util

enum class RodAxis {
    X,
    Y,
    Z,
}

enum class ClickedFace {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST,
    ;

    fun toAxis(): RodAxis = when (this) {
        DOWN, UP -> RodAxis.Y
        WEST, EAST -> RodAxis.X
        NORTH, SOUTH -> RodAxis.Z
    }
}

data class RodAxes(
    val x: Boolean = false,
    val y: Boolean = false,
    val z: Boolean = false,
) {
    fun has(axis: RodAxis): Boolean = when (axis) {
        RodAxis.X -> x
        RodAxis.Y -> y
        RodAxis.Z -> z
    }

    fun with(axis: RodAxis): RodAxes = when (axis) {
        RodAxis.X -> copy(x = true)
        RodAxis.Y -> copy(y = true)
        RodAxis.Z -> copy(z = true)
    }

    val count: Int
        get() = (if (x) 1 else 0) + (if (y) 1 else 0) + (if (z) 1 else 0)

    companion object {
        val NONE = RodAxes()

        fun single(axis: RodAxis): RodAxes = NONE.with(axis)
    }
}

data class CellView(
    val sameKind: Boolean,
    val axes: RodAxes,
    val replaceable: Boolean,
)

enum class PlaceWhere {
    CLICKED,
    NEIGHBOR,
}

sealed class Placement {
    data class Insert(val where: PlaceWhere, val axes: RodAxes) : Placement()
    data object Pass : Placement()
}

fun planPlacement(clicked: CellView, neighbor: CellView, axis: RodAxis): Placement {
    if (clicked.sameKind && !clicked.axes.has(axis)) {
        return Placement.Insert(PlaceWhere.CLICKED, clicked.axes.with(axis))
    }
    if (!clicked.sameKind && clicked.replaceable) {
        return Placement.Insert(PlaceWhere.CLICKED, RodAxes.single(axis))
    }
    if (neighbor.sameKind && !neighbor.axes.has(axis)) {
        return Placement.Insert(PlaceWhere.NEIGHBOR, neighbor.axes.with(axis))
    }
    if (!neighbor.sameKind && neighbor.replaceable) {
        return Placement.Insert(PlaceWhere.NEIGHBOR, RodAxes.single(axis))
    }
    return Placement.Pass
}

/**
 * Обычный клик бамбуком по месту, где ваниль посадит росток, оставляем ванили.
 * Присед ставит декоративный стебель даже на землю, где бамбук растёт.
 */
fun deferBambooToVanilla(
    sneaking: Boolean,
    placingNewRod: Boolean,
    cellReplaceable: Boolean,
    belowSupportsBamboo: Boolean,
): Boolean = !sneaking && placingNewRod && cellReplaceable && belowSupportsBamboo
