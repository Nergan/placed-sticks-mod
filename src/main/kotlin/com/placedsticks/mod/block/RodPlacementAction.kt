package com.placedsticks.mod.block

import com.placedsticks.mod.util.CellView
import com.placedsticks.mod.util.PlaceWhere
import com.placedsticks.mod.util.Placement
import com.placedsticks.mod.util.RodAxes
import com.placedsticks.mod.util.deferBambooToVanilla
import com.placedsticks.mod.util.planPlacement
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

object PlacedRods {
    lateinit var stick: RodBlock
    lateinit var bamboo: RodBlock

    fun ready(): Boolean = ::stick.isInitialized && ::bamboo.isInitialized
}

object RodPlacementAction {

    fun tryPlace(context: UseOnContext): InteractionResult? {
        if (!PlacedRods.ready()) return null
        val player = context.player ?: return null
        if (!player.mayBuild()) return null

        val bamboo = context.itemInHand.`is`(Items.BAMBOO)
        val stick = context.itemInHand.`is`(Items.STICK)
        if (!bamboo && !stick) return null

        val block = if (bamboo) PlacedRods.bamboo else PlacedRods.stick
        val placeContext = BlockPlaceContext(context)
        val clickedPos = context.clickedPos
        val neighborPos = clickedPos.relative(context.clickedFace)
        val level = context.level
        val axis = RodBlock.axisOf(context.clickedFace)
        val plan = planPlacement(
            view(block, level.getBlockState(clickedPos), placeContext),
            view(block, level.getBlockState(neighborPos), placeContext),
            axis,
        )
        val insert = plan as? Placement.Insert ?: return null
        val targetPos = if (insert.where == PlaceWhere.CLICKED) clickedPos else neighborPos

        if (bamboo && deferBambooToVanilla(
                sneaking = player.isShiftKeyDown,
                placingNewRod = insert.axes.count == 1,
                cellReplaceable = RodBlock.isReplaceable(level.getBlockState(targetPos), placeContext),
                belowSupportsBamboo = RodBlock.belowSupportsVanillaBamboo(level.getBlockState(targetPos.below())),
            )
        ) {
            return null
        }
        if (!level.mayInteract(player, targetPos)) return null

        if (!level.isClientSide) {
            place(level, block, targetPos, insert.axes)
            if (!player.abilities.instabuild) {
                context.itemInHand.shrink(1)
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    private fun view(block: RodBlock, state: BlockState, context: BlockPlaceContext): CellView {
        val same = state.block === block
        return CellView(
            sameKind = same,
            axes = if (same) block.axesOf(state) else RodAxes.NONE,
            replaceable = RodBlock.isReplaceable(state, context),
        )
    }

    private fun place(level: Level, block: RodBlock, pos: BlockPos, axes: RodAxes) {
        level.setBlock(pos, block.withAxes(axes), 3)
        val sound = if (block === PlacedRods.bamboo) SoundEvents.BAMBOO_PLACE else SoundEvents.WOOD_PLACE
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0f, 1.0f)
    }
}
