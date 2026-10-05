package com.placedsticks.mod

import com.placedsticks.mod.block.ModBlocks
import com.placedsticks.mod.event.RodPlacement
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForge

@Mod(PlacedSticks.MOD_ID)
class PlacedSticksMod(modEventBus: IEventBus, modContainer: ModContainer) {

    init {
        PlacedSticks.LOGGER.info("Placed Sticks {} ({})", modContainer.modInfo.version, PlacedSticks.MOD_ID)
        ModBlocks.register(modEventBus)
        modEventBus.addListener { _: FMLCommonSetupEvent -> ModBlocks.onCommonSetup() }
        NeoForge.EVENT_BUS.register(RodPlacement)
    }
}
