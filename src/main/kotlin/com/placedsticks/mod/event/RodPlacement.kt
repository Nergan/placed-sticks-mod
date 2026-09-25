package com.placedsticks.mod.event

import com.placedsticks.mod.block.ModBlocks
import com.placedsticks.mod.block.RodBlock
import com.placedsticks.mod.util.CellView
import com.placedsticks.mod.util.PlaceWhere
import com.placedsticks.mod.util.Placement
import com.placedsticks.mod.util.RodAxes
import com.placedsticks.mod.util.deferBambooToVanilla
import com.placedsticks.mod.util.planPlacement
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent

object RodPlacement {

    @SubscribeEvent
    fun onUseItemOnBlock(event: UseItemOnBlockEvent) {
        if (event.usePhase != UseItemOnBlockEvent.UsePhase.ITEM_AFTER_BLOCK) return
        val context = event.useOnContext
        val player = context.player ?: return
        if (!player.mayBuild()) return

        val bamboo = context.itemInHand.`is`(Items.BAMBOO)
        val stick = context.itemInHand.`is`(Items.STICK)
        if (!bamboo && !stick) return

        val block = if (bamboo) ModBlocks.BAMBOO.get() else ModBlocks.STICK.get()
        val placeContext = BlockPlaceContext(context)
        val clickedPos = context.clickedPos
        val neighborPos = clickedPos.relative(context.clickedFace)
        val level = context.level
        val axis = RodBlock.axisOf(context.clickedFace)

        val clickedState = level.getBlockState(clickedPos)
        val neighborState = level.getBlockState(neighborPos)
        val plan = planPlacement(
            view(block, clickedState, placeContext),
            view(block, neighborState, placeContext),
            axis,
        )
        val insert = plan as? Placement.Insert ?: return
        val targetPos = if (insert.where == PlaceWhere.CLICKED) clickedPos else neighborPos

        if (bamboo && deferBambooToVanilla(
                sneaking = player.isShiftKeyDown,
                placingNewRod = insert.axes.count == 1,
                cellReplaceable = RodBlock.isReplaceable(level.getBlockState(targetPos), placeContext),
                belowSupportsBamboo = RodBlock.belowSupportsVanillaBamboo(level.getBlockState(targetPos.below())),
            )
        ) {
            return
        }

        if (!level.mayInteract(player, targetPos)) return

        if (!level.isClientSide) {
            place(level, block, targetPos, insert.axes)
            if (!player.abilities.instabuild) {
                context.itemInHand.shrink(1)
            }
        }

        event.cancelWithResult(ItemInteractionResult.sidedSuccess(level.isClientSide))
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
        val state = block.withAxes(axes)
        level.setBlock(pos, state, 3)
        val sound = if (block === ModBlocks.BAMBOO.get()) SoundEvents.BAMBOO_PLACE else SoundEvents.WOOD_PLACE
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0f, 1.0f)
    }
}
