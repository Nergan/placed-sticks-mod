package com.placedsticks.mod.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockState

class NeoRodBlock(
    properties: Properties,
    dropBamboo: Boolean,
    half: Double,
    private val igniteOdds: Int,
    private val burnOdds: Int,
) : RodBlock(properties, dropBamboo, half) {

    override fun getFlammability(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int =
        burnOdds

    override fun getFireSpreadSpeed(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int =
        igniteOdds
}
