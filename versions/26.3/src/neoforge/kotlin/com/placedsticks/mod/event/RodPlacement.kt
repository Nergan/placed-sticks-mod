package com.placedsticks.mod.event

import com.placedsticks.mod.block.RodPlacementAction
import net.minecraft.world.InteractionResult
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent

object RodPlacement {

    @SubscribeEvent
    fun onUseItemOnBlock(event: UseItemOnBlockEvent) {
        if (event.usePhase != UseItemOnBlockEvent.UsePhase.ITEM_AFTER_BLOCK) return
        if (RodPlacementAction.tryPlace(event.useOnContext) == null) return
        val client = event.useOnContext.level.isClientSide
        val result = if (client) InteractionResult.SUCCESS else InteractionResult.CONSUME
        event.cancelWithResult(result)
    }
}
