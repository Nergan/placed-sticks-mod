package com.placedsticks.mod.event

import com.placedsticks.mod.block.RodPlacementAction
import net.minecraft.world.ItemInteractionResult
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent

object RodPlacement {

    @SubscribeEvent
    fun onUseItemOnBlock(event: UseItemOnBlockEvent) {
        if (event.usePhase != UseItemOnBlockEvent.UsePhase.ITEM_AFTER_BLOCK) return
        if (RodPlacementAction.tryPlace(event.useOnContext) == null) return
        event.cancelWithResult(ItemInteractionResult.sidedSuccess(event.useOnContext.level.isClientSide))
    }
}
